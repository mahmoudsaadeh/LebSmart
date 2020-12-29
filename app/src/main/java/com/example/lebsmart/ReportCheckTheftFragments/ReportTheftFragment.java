package com.example.lebsmart.ReportCheckTheftFragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lebsmart.R;
import com.example.lebsmart.fragments.DatePickerFragment;
import com.example.lebsmart.fragments.TimePickerFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReportTheftFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    ViewGroup root;

    Button setDate;
    Button setTime;
    Button getCurrentLocation;

    TextView showDate;
    TextView showTime;
    TextView theftLocation;

    FusedLocationProviderClient fusedLocationProviderClient;

    double lat, lon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.report_theft_fragment, container, false);

        showDate = root.findViewById(R.id.theftDateTV);
        showTime = root.findViewById(R.id.theftTimeTV);
        theftLocation = root.findViewById(R.id.theftLocationTV);

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


        return root;
        //return super.onCreateView(inflater, container, savedInstanceState);
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
