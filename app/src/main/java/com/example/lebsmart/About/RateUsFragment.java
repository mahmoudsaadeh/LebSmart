package com.example.lebsmart.About;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.R;

public class RateUsFragment extends Fragment {

    ViewGroup root;

    Button submit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.rate_us_fragment, container, false);

        submit = root.findViewById(R.id.submitReviewButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // we should first check if storing the data was successful in db
                // on suceess, we show the dialog

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.thank_you_dialog, null);

                builder.setView(view);

                //ImageView check = view.findViewById(R.id.checkImage);
                //Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();

                final AlertDialog dialog = builder.create();

                ImageView cross = view.findViewById(R.id.crossImage);
                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //builder.setView(view);
                dialog.show();
            }
        });

        return root;
    }
}
