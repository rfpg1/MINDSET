package com.application.MindSet.ui.game;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.application.MindSet.R;
import com.application.MindSet.databinding.FragmentCreateGameBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class CreateGameFragment extends Fragment {

    private FragmentCreateGameBinding binding;
    private DatePickerDialog datePickerDialog;
    private Button dateBTN, localBTN;
    private String sport, date, local;
    private ArrayList<String> participants;
    private final static int REQUEST_CODE = 101;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE:
                if(resultCode == -1){
                    Bundle extras = data.getExtras();
                    if (extras != null){
                        local = extras.getString("location");
                    }
                }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String [] sports = getResources().getStringArray(R.array.sports);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.drop_down_item, sports);
        binding.autoCompleteSelectSport.setAdapter(arrayAdapter);
        if (date != null){
            dateBTN.setText(date);
        }
        if (local != null){
            localBTN.setText(local);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCreateGameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String [] sports = getResources().getStringArray(R.array.sports);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.drop_down_item, sports);
        binding.autoCompleteSelectSport.setAdapter(arrayAdapter);

        initDatePicker(getContext());
        dateBTN = binding.dateBTN;
        dateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        localBTN = binding.localBTN;
        localBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapsActivity = new Intent(getContext(), MapsActivity.class);
                startActivityForResult(mapsActivity, REQUEST_CODE);
            }
        });

        return root;
    }

    private void initDatePicker(Context context) {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                date = day + " " + getMonthFormat(month) + " " + year;
                dateBTN.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(context, style, dateSetListener, year, month, day);
    }

    private String getMonthFormat(int month) {
        switch (month){
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