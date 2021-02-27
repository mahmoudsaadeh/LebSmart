package com.example.lebsmart.MeetingsFragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.ApartmentsFragments.CheckApartmentsFragment;
import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.example.lebsmart.RandomFragments.DatePickerFragment;
import com.example.lebsmart.RandomFragments.TimePickerFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class ScheduleMeetingFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    ViewGroup root;

    Button setMeetingDate;
    Button setMeetingTime;

    TextView showMeetingDate;
    TextView showMeetingTime;

    Button schedule;

    EditText meetingTitle, meetingDescriptionET, meetingPlaceET;

    String title, description, location, meetingDate, meetingTime;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.schedule_meeting_fragment, container, false);

        meetingTitle = root.findViewById(R.id.meetingTitle);
        meetingDescriptionET = root.findViewById(R.id.meetingDescriptionET);
        meetingPlaceET = root.findViewById(R.id.meetingPlaceET);

        setMeetingDate = root.findViewById(R.id.meetingDateBtn);
        setMeetingTime = root.findViewById(R.id.meetingTimeBtn);

        showMeetingDate = root.findViewById(R.id.meetingDateTV);
        showMeetingTime = root.findViewById(R.id.meetingTimeTV);

        schedule = root.findViewById(R.id.schedule);

        setMeetingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.setTargetFragment(ScheduleMeetingFragment.this, 0);
                datePicker.show(getFragmentManager(), "date picker");
            }
        });

        setMeetingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.setTargetFragment(ScheduleMeetingFragment.this, 0);
                timePicker.show(getFragmentManager(), "time picker");
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleAMeeting();
            }
        });

        progressDialog = new ProgressDialog(getActivity());

        return root;
    }

    public void scheduleAMeeting() {
        title = meetingTitle.getText().toString();
        description = meetingDescriptionET.getText().toString();
        location = meetingPlaceET.getText().toString();
        meetingDate = showMeetingDate.getText().toString();
        meetingTime = showMeetingTime.getText().toString();

        //Log.i("meeting date", meetingDate);
        //Log.i("meeting time", meetingTime);

        if (title.isEmpty()) {
            CommonMethods.warning(meetingTitle, "Meeting title is required!");
            return;
        }
        if (description.isEmpty()) {
            CommonMethods.warning(meetingDescriptionET, "Meeting description is required!");
            return;
        }
        if (location.isEmpty()) {
            CommonMethods.warning(meetingPlaceET, "Meeting location/place is required!");
            return;
        }
        if (meetingDate.isEmpty() || meetingDate.equals("Meeting Date")) {
            Toast.makeText(getActivity(), "Meeting date is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (meetingTime.isEmpty() || meetingTime.equals("Meeting Time")) {
            Toast.makeText(getActivity(), "Meeting time is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        CommonMethods.displayLoadingScreen(progressDialog);
        MeetingAdd meetingAdd = new MeetingAdd(meetingTime, meetingDate, location, title, description);

        // no need for categorization since user can all check or add meetings within his building
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Meetings");
        reference = reference.child(CheckApartmentsFragment.getUserBuilding)
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        reference.setValue(meetingAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Meeting scheduled successfully!", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
                else {
                    Toast.makeText(getActivity(), "Failed to schedule the meeting! Please try again.", Toast.LENGTH_SHORT).show();
                    Log.i("apartmentAdd", "failed");
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Log.i("failed to add meeting", Objects.requireNonNull(e.getMessage()));
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
        showMeetingDate.setText(currentDate);
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

        showMeetingTime.setText(hr + ":" + min);
    }

}
