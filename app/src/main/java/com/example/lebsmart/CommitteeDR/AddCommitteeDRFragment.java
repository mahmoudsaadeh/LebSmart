package com.example.lebsmart.CommitteeDR;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.R;

public class AddCommitteeDRFragment extends Fragment {

    ViewGroup root;

    RadioButton radioButtonDecision;
    RadioButton radioButtonReminder;

    Button addAnnouncementButton;

    LinearLayout addCommitteeDrLL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.add_committee_dr, container, false);

        radioButtonDecision = root.findViewById(R.id.radioButtonDecision);
        radioButtonReminder = root.findViewById(R.id.radioButtonReminder);

        addAnnouncementButton = root.findViewById(R.id.addAnnouncementButton);

        addCommitteeDrLL = root.findViewById(R.id.addCommitteeDrLL);

        radioButtonDecision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(getActivity());
            }
        });

        radioButtonReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(getActivity());
            }
        });

        addAnnouncementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(getActivity());
            }
        });

        addCommitteeDrLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(getActivity());
            }
        });

        return root;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}
