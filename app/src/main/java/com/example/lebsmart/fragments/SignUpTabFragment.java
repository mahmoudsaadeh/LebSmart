package com.example.lebsmart.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.others.ProgressButton;
import com.example.lebsmart.R;

public class SignUpTabFragment extends Fragment {

    private EditText email;
    private EditText password;
    private EditText confirmPassword;

    float alpha = 0;

    View view;

    ProgressButton progressButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_fragment, container, false);

        email = root.findViewById(R.id.emailSignUp);
        password = root.findViewById(R.id.passwordSignUp);
        confirmPassword = root.findViewById(R.id.confirmPassword);
        view = root.findViewById(R.id.includeSignUp);

        //causing problems
        /*email.setTranslationX(800);
        password.setTranslationX(800);
        loginButton.setTranslationX(800);*/

        progressButton = new ProgressButton(getContext(), view);
        progressButton.resetDesign("Sign Up");

        /*email.setAlpha(alpha);
        password.setAlpha(alpha);
        confirmPassword.setAlpha(alpha);
        view.setAlpha(alpha);

        email.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        confirmPassword.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();
        view.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(800).start();*/

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressButton.buttonActivated();

                //replace it with onSuccess method of database
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressButton.buttonFinished();

                        //return design as is after success
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressButton.resetDesign("Sign Up");
                            }
                        }, 3000);

                    }
                }, 3000);



            }
        });

        return root;

        //return super.onCreateView(inflater, container, savedInstanceState);
    }

}
