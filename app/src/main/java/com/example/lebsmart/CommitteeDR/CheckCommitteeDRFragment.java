package com.example.lebsmart.CommitteeDR;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.Activities.AnnouncementsActivity;
import com.example.lebsmart.R;

public class CheckCommitteeDRFragment extends Fragment {

    ViewGroup root;

    CardView decisionsCard;
    CardView remindersCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.check_committee_dr, container, false);

        decisionsCard = root.findViewById(R.id.decisionsCard);
        remindersCard = root.findViewById(R.id.remindersCard);

        decisionsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AnnouncementsActivity.class);
                intent.putExtra("announcementType", "Committee Decisions");
                startActivity(intent);
            }
        });

        remindersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AnnouncementsActivity.class);
                intent.putExtra("announcementType", "Committee Reminders");
                startActivity(intent);
            }
        });

        return root;
    }
}
