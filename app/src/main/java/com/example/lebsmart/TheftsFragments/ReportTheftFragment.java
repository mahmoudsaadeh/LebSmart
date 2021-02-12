package com.example.lebsmart.TheftsFragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.ApartmentsFragments.CheckApartmentsFragment;
import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.example.lebsmart.RandomFragments.DatePickerFragment;
import com.example.lebsmart.RandomFragments.TimePickerFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ReportTheftFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    ViewGroup root;

    Button setDate;
    Button setTime;
    Button getCurrentLocation;

    TextView showDate;
    TextView showTime;
    TextView theftLocation;

    RadioGroup theftWithinRadioGroup;
    RadioButton radioButtonYourBuildingTheft, radioButtonSmartCityTheft;

    FusedLocationProviderClient fusedLocationProviderClient;

    double lat, lon;

    Button reportTheftButton;
    EditText theftTitle, theftMessage;

    ProgressDialog progressDialog;

    String title, description, date, time, location, theftWithinString;
    int theftWithin;

    TheftsAdd theftsAdd;

    public static final int NO_RADIO_BUTTON_SELECTED = -1;

    DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.report_theft_fragment, container, false);

        theftWithinRadioGroup = root.findViewById(R.id.theftWithinRadioGroup);
        radioButtonYourBuildingTheft = root.findViewById(R.id.radioButtonYourBuildingTheft);
        radioButtonSmartCityTheft = root.findViewById(R.id.radioButtonSmartCityTheft);

        showDate = root.findViewById(R.id.theftDateTV);
        showTime = root.findViewById(R.id.theftTimeTV);
        theftLocation = root.findViewById(R.id.theftLocationTV);

        theftTitle = root.findViewById(R.id.theftTitle);
        theftMessage = root.findViewById(R.id.theftMessage);
        reportTheftButton = root.findViewById(R.id.reportTheftButton);

        setDate = root.findViewById(R.id.setTheftDate);
        setTime = root.findViewById(R.id.setTheftTime);
        getCurrentLocation = root.findViewById(R.id.getCurrentLocationTheft);

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.setTargetFragment(ReportTheftFragment.this, 0);
                datePicker.show(getFragmentManager(), "date picker");
            }
        });

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.setTargetFragment(ReportTheftFragment.this, 0);
                timePicker.show(getFragmentManager(), "time picker");
            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        getCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check condition
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                    // when both permissions are granted, call method to get current location
                    getMyCurrentLocation();
                }
                else {
                    // when permission is not granted, request it
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[] {Manifest.permission.ACCESS_FINE_LOCATION
                                    , Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
            }
        });

        reportTheftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportTheft();
            }
        });

        progressDialog = new ProgressDialog(getActivity());

        return root;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }


    public void reportTheft () {

        theftWithin = theftWithinRadioGroup.getCheckedRadioButtonId();

        title = theftTitle.getText().toString();
        description = theftMessage.getText().toString();

        date = showDate.getText().toString();
        time = showTime.getText().toString();
        location = theftLocation.getText().toString();

        if (theftWithin == NO_RADIO_BUTTON_SELECTED) {
            Toast.makeText(getActivity(), "Please fill the first entry!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (theftWithin == R.id.radioButtonYourBuildingTheft) {
                theftWithinString = radioButtonYourBuildingTheft.getText().toString();
            } else if (theftWithin == R.id.radioButtonSmartCityTheft) {
                theftWithinString = radioButtonSmartCityTheft.getText().toString();
            }
        }

        if (title.isEmpty()) {
            CommonMethods.warning(theftTitle, "Title is required!");
            return;
        }

        if (description.isEmpty()) {
            CommonMethods.warning(theftTitle, "Description is required!");
            return;
        }

        if (date.isEmpty() || date.equals("Theft Date")) {
            Toast.makeText(getActivity(), "Date is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (time.isEmpty() || time.equals("Theft Time")) {
            Toast.makeText(getActivity(), "Time is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (location.isEmpty() || location.equals("Theft Location")) {
            Toast.makeText(getActivity(), "Location is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        CommonMethods.displayLoadingScreen(progressDialog);
        theftsAdd = new TheftsAdd(title, date, time, description, location);

        reference = FirebaseDatabase.getInstance().getReference("TheftsCategorized");

        if (theftWithinString.equals("Your Building")) {
            reference = reference.child(theftWithinString).child(CheckApartmentsFragment.getUserBuilding)
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        }
        else {
            reference = reference.child(theftWithinString)
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        }

        reference.setValue(theftsAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Log.i("theftReport", "success");
                    Toast.makeText(getActivity(), "Theft reported successfully!", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
                else {
                    //Log.i("theftReport", "failed");
                    Toast.makeText(getActivity(), "Failed to report the theft! Please try again.", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                CommonMethods.dismissLoadingScreen(progressDialog);
            }
        });

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        showDate.setText(currentDate);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String min = String.valueOf(minute);
        String hr = String.valueOf(hourOfDay);

        if (min.length() == 1) {
            min = "0" + min;
        }
        if (hr.length() == 1) {
            hr = "0" + hr;
        }

        showTime.setText(hr + ":" + min);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults.length > 0
                &&
                (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED))
        {
            getMyCurrentLocation();
        }
        else {
            // when permission is denied
            Toast.makeText(getActivity(), "Permission denied!", Toast.LENGTH_SHORT).show();
        }
    }


    public void getMyCurrentLocation() {
        // initialize location manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull final Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        lat = location.getLatitude();
                        lon = location.getLongitude();
                        //theftLocation.setText(String.valueOf(lat) + " / " + String.valueOf(lon));
                        // save to db
                        String address = getAddress(lat, lon);
                        theftLocation.setText(address);
                    } else {
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                //theftLocation.setText(String.valueOf(location1.getLatitude()) + " / " + String.valueOf(location1.getLongitude()));
                                theftLocation.setText(getAddress(location1.getLatitude(), location1.getLongitude()));
                            }
                        };
                        // request location updates
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        }
        else {
            // when location service is not enabled, open location settings
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    } //  end method


    public String getAddress (double lat, double lon) {
        String address = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            Address address1 = addresses.get(0);
            address = address1.getAddressLine(0);
            Log.i("lat", String.valueOf(lat));
            Log.i("lon", String.valueOf(lon));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

}
