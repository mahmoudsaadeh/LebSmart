package com.example.lebsmart.Others;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lebsmart.R;

public class ProgressButton {

    CardView cardView;
    ConstraintLayout constraintLayout;
    ProgressBar progressBar;
    TextView textView;

    Animation fade_in;

    public ProgressButton(Context context, View view) {

        cardView = view.findViewById(R.id.button_card_view);
        constraintLayout = view.findViewById(R.id.inner_constraint_layout);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.progressButtonTV);

    }

    public void buttonActivated(){
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("Please wait ...");
    }

    public void buttonFinished(){
        constraintLayout.setBackgroundColor(cardView.getResources().getColor(R.color.green));
        progressBar.setVisibility(View.GONE);
        textView.setText("Done");
    }

    public void resetDesign(String textViewText){
        constraintLayout.setBackgroundColor(cardView.getResources().getColor(R.color.colorPrimary));
        progressBar.setVisibility(View.GONE);
        textView.setText(textViewText);
    }

}
