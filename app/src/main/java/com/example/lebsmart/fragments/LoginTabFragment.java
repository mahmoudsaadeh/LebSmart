package com.example.lebsmart.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.activities.LoginActivity;
import com.example.lebsmart.activities.MainScreenActivity;
import com.example.lebsmart.activities.SplashScreenActivity;
import com.example.lebsmart.others.ProgressButton;
import com.example.lebsmart.R;

public class LoginTabFragment extends Fragment {

    EditText email;
    EditText password;
    //Button loginButton;

    float alpha = 0;

    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Log.i("login", "created");
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        email = root.findViewById(R.id.emailLogin);
        password = root.findViewById(R.id.passwordLogin);
        //loginButton = root.findViewById(R.id.loginButton);
        view = root.findViewById(R.id.progressButtonInclude);

        /*email.setTranslationX(800);
        password.setTranslationX(800);
        loginButton.setTranslationX(800);*/

        email.setAlpha(alpha);
        password.setAlpha(alpha);
        //loginButton.setAlpha(alpha);
        view.setAlpha(alpha);

        email.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        //loginButton.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();
        view.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressButton progressButton = new ProgressButton(getContext(), view);
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
                                progressButton.resetDesign("Login");

                                Intent intent = new Intent(getActivity(), MainScreenActivity.class);
                                startActivity(intent);

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
