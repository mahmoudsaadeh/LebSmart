package com.example.lebsmart;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.fragments.DatePickerFragment;
import com.example.lebsmart.fragments.TimePickerFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class ScheduleMeetingFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    ViewGroup root;

    Button setMeetingDate;
    Button setMeetingTime;

    TextView showMeetingDate;
    TextView showMeetingTime;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.schedule_meeting_fragment, container, false);

        setMeetingDate = root.findViewById(R.id.meetingDateBtn);
        setMeetingTime = root.findViewById(R.id.meetingTimeBtn);

        showMeetingDate = root.findViewById(R.id.meetingDateTV);
        showMeetingTime = root.findViewById(R.id.meetingTimeTV);

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

        return root;
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
