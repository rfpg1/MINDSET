package com.application.MindSet.ui.game;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.application.MindSet.R;
import com.application.MindSet.databinding.FragmentCreateGameBinding;
import com.application.MindSet.dto.Game;
import com.application.MindSet.dto.Message;
import com.application.MindSet.location.LocationService;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CreateGameFragment extends Fragment {

    private FragmentCreateGameBinding binding;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private AddPlayersFragment addPFrag;
    private Button dateBTN, timeBTN, localBTN, createBTN;
    private ImageView addPlayerBTN;
    private String sport, dateDTO, localName;
    private LatLng local;
    private Calendar date = Calendar.getInstance();
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
                        localName = (String) extras.get("localName");
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
            localBTN.setText(localName);
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

        initTimePicker(getContext());
        timeBTN = binding.timeBTN;
        timeBTN.setOnClickListener(view -> timePickerDialog.show());

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
                        namesInGame.add((String) v.getTag(R.string.playerId));
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

                //Caso hajam jogadores já adicionados, ir buscá-los


                Game g = new Game(uID, sport, date.getTime(), local, new ArrayList<>(), localName);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Games").add(g).addOnSuccessListener(documentReference -> {
                    String gameId = documentReference.getId();
                    Log.i("InvitePlayer", gameId);
                    if (addPFrag != null) {
                        List<String> invitedPlayersIDs = this.addPFrag.getInvitedPlayersIDs();
                        for(String invitedPlayer : invitedPlayersIDs) {
                            Message m = new Message(uID, invitedPlayer, invitedPlayer, "Invited for game",
                                    true, gameId, g.getSport());
                            db.collection("Messages").add(m).addOnSuccessListener(documentReference1 -> {
                                Log.i("InvitePlayer", "Message created");
                            }).addOnFailureListener(fail -> {
                                Log.i("InvitePlayer", "Message failed");
                            });
                        }
                    }
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

    private void initTimePicker(Context context) {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                date.set(Calendar.HOUR_OF_DAY, i);
                date.set(Calendar.MINUTE, i1);
                timeBTN.setText(getHourFormat(i,i1));
            }
        };
        Calendar cal = Calendar.getInstance();

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        timePickerDialog = new TimePickerDialog(context, style, timeSetListener, hour, minute, android.text.format.DateFormat.is24HourFormat(context));
    }

    private String getHourFormat(int i, int i1) {
        String format = "";
        if (i < 10){
            format+= "0" + i;
        }else{
            format+= i;
        }
        format+=":";
        if (i1 < 10){
            format+= "0" + i1;
        }else{
            format+= i1;
        }
        return format;
    }

    private void initDatePicker(Context context) {

        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            date.set(year , month, day);
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
            case 0:
                return "JAN";
            case 1:
                return "FEV";
            case 2:
                return "MAR";
            case 3:
                return "ABR";
            case 4:
                return "MAY";
            case 5:
                return "JUN";
            case 6:
                return "JUL";
            case 7:
                return "AUG";
            case 8:
                return "SET";
            case 9:
                return "OCT";
            case 10:
                return "NOV";
            case 11:
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