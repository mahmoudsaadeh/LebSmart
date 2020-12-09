package com.example.lebsmart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class LoginTabFragment extends Fragment {

   /* EditText email;
    EditText password;
    Button loginButton;

    float alpha = 0;*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Log.i("login", "created");
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);
/*
        email = root.findViewById(R.id.emailLogin);
        password = root.findViewById(R.id.passwordLogin);
        loginButton = root.findViewById(R.id.loginButton);

        email.setTranslationX(800);
        password.setTranslationX(800);
        loginButton.setTranslationX(800);

        email.setAlpha(alpha);
        password.setAlpha(alpha);
        loginButton.setAlpha(alpha);

        email.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        loginButton.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();
*/
        return root;

        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
