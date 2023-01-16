package com.application.MindSet.ui.game;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.application.MindSet.R;
import com.application.MindSet.databinding.FragmentCreateGameBinding;
import com.application.MindSet.dto.Game;
import com.application.MindSet.location.LocationService;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CreateGameFragment extends Fragment {

    private FragmentCreateGameBinding binding;
    private DatePickerDialog datePickerDialog;
    private AddPlayersFragment addPFrag;
    private Button dateBTN, localBTN, createBTN;
    private MaterialCardView addPlayerBTN;
    private String sport, dateDTO;
    private LatLng local;
    private Date date;
    private LinearLayout playerInGame;
    private final static int REQUEST_CODE = 101;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == -1) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        local = (LatLng) extras.get("location");
                    }
                }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String[] sports = getResources().getStringArray(R.array.sports);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.drop_down_item, sports);
        binding.autoCompleteSelectSport.setAdapter(arrayAdapter);
        if (dateDTO != null) {
            dateBTN.setText(dateDTO);
        }
        if (local != null) {
            localBTN.setText(local.toString());
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCreateGameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String[] sports = getResources().getStringArray(R.array.sports);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.drop_down_item, sports);
        binding.autoCompleteSelectSport.setAdapter(arrayAdapter);
        binding.autoCompleteSelectSport.setOnItemClickListener((parent, view, position, id) -> {
            sport = arrayAdapter.getItem(position);
        });
        initDatePicker(getContext());
        dateBTN = binding.dateBTN;
        dateBTN.setOnClickListener(view -> datePickerDialog.show());

        localBTN = binding.localBTN;
        localBTN.setOnClickListener(view -> {
            Intent mapsActivity = new Intent(getContext(), MapsActivity.class);
            startActivityForResult(mapsActivity, REQUEST_CODE);
        });

        playerInGame = binding.playersLL;

        addPlayerBTN = binding.addPlayerBTN;
        addPlayerBTN.setOnClickListener(view -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Profiles").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //Array com todos os players que já estão adicionar ao jogo
                    List<String> namesInGame = new ArrayList<>();
                    for (int i = 0; i < playerInGame.getChildCount(); i++) {
                        View v = playerInGame.getChildAt(i);
                        namesInGame.add((String) v.getTag());
                    }
                    //Array com todos os nomes que temos na BD que podem ser convidados
                    List<String> names = new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        names.add((String) queryDocumentSnapshot.get("name"));
                    }
                    //Array com o ID de todos os players que temos na BD
                    List<String> playersIDs = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        playersIDs.add(documentSnapshot.getId());
                    }
                    //Hashmap que junta as juntas listas anteriores
                    ConcurrentHashMap<String, String> players = new ConcurrentHashMap<>();
                    for (int i = 0; i < names.size(); i++) {
                        players.put(playersIDs.get(i), names.get(i));
                    }

                    //Se o jogador já estiver adicionar removê-lo da lista a convidar
                    for (String entry : players.keySet()) {
                        if (namesInGame.contains(entry)) {
                            players.remove(entry);
                        }
                    }
                                                    //Nome dos jogadores a convidar
                    addPFrag = new AddPlayersFragment(new ArrayList<>(players.values()),
                                                    //IDs dos jogadores a convidar
                            playerInGame, new ArrayList<>(players.keySet()));
                    addPFrag.show(getChildFragmentManager(), "ADD PLAYER");
                }
            });
        });

        createBTN = binding.createBTN;
        createBTN.setOnClickListener(view -> {
            if (sport != null && dateDTO != null & local != null) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser mUser = mAuth.getCurrentUser();
                String uID = mUser.getUid();
                List<String> playersIDs;
                //Caso hajam jogadores já adicionados, ir buscá-los
                if (addPFrag != null)
                    playersIDs = this.addPFrag.getPlayersInGameIDs();
                else
                    playersIDs = new ArrayList<>();
                Game g = new Game(uID, sport, date, local, playersIDs);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Games").add(g).addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity(), "Game created", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_home);

                    double latitude = local.latitude;
                    double longitude = local.longitude;
                    createNotificationForGame(new LatLng(latitude, longitude), g);
                });
            } else {
                Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void createNotificationForGame(LatLng l, Game g) {
        Intent intent = new Intent(getContext(), LocationService.class);
        intent.putExtra("Location", l);
        intent.putExtra("Date", g.getDate());
        new LocationService(intent, getContext());
    }


    private void initDatePicker(Context context) {

        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month += 1;
            date = Date.valueOf(year + "-" + month + "-" + day);
            dateDTO = day + " " + getMonthFormat(month) + " " + year;
            dateBTN.setText(dateDTO);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(context, style, dateSetListener, year, month, day);
    }

    private String getMonthFormat(int month) {
        switch (month) {
            case 1:
                return "JAN";
            case 2:
                return "FEV";
            case 3:
                return "MAR";
            case 4:
                return "ABR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SET";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}