package com.example.lebsmart.RandomFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.Activities.BuildingsListActivity;
import com.example.lebsmart.Activities.LoginActivity;
import com.example.lebsmart.Activities.MainScreenActivity;
import com.example.lebsmart.Activities.ResetPasswordActivity;
import com.example.lebsmart.Others.ProgressButton;
import com.example.lebsmart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Context.MODE_PRIVATE;

public class LoginTabFragment extends Fragment {

    EditText email;
    EditText password;
    //Button loginButton;

    float alpha = 0;

    View view;

    ProgressButton progressButton;

    FirebaseAuth mAuth;
    TextView forgotPassword;
    //public static SharedPreferences sp;

    FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        email = root.findViewById(R.id.emailLogin);
        password = root.findViewById(R.id.passwordLogin);
        view = root.findViewById(R.id.progressButtonInclude);
        floatingActionButton = root.findViewById(R.id.floatingActionButton);
        forgotPassword=root.findViewById(R.id.forgotPassword);
        email.setAlpha(alpha);
        password.setAlpha(alpha);
        view.setAlpha(alpha);

        email.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        view.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();


        mAuth = FirebaseAuth.getInstance();

        LoginActivity.sp = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        if(LoginActivity.sp.getBoolean("loggedin",false)){
            goToMainActivity();
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.contact_us_dialog, null);

                builder.setView(view);

                //ImageView check = view.findViewById(R.id.checkImage);
                //Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();

                final AlertDialog dialog = builder.create();

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                ImageView cross = view.findViewById(R.id.crossImage2);
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

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressButton = new ProgressButton(getContext(), view);
                progressButton.buttonActivated();

                view.setEnabled(false);

                login();

                //replace it with onSuccess method of database
                /*Handler handler = new Handler();
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

                                //Intent intent = new Intent(getActivity(), MainScreenActivity.class);
                                //startActivity(intent);

                                //in case the user/resident already chose a building
                                //redirect directly to main screen
                                Intent intent = new Intent(getActivity(), BuildingsListActivity.class);
                                startActivity(intent);

                            }
                        }, 1777);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                view.setEnabled(true);
                            }
                        }, 3751);


                    }
                }, 2100);*/



            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), ResetPasswordActivity.class);
                startActivity(intent1);
            }
        });

        return root;

    } // end onCreateView

    public void login() {
        CommonMethods.hideSoftKeyboard(getActivity());
        String email2 = CommonMethods.getEmail(email);
        String pass = CommonMethods.getPassword(password);

        if(CommonMethods.checkIfEmpty(email2)) {
            CommonMethods.warning(email, "Email is required!");
            progressButton.resetDesign("Login");
            view.setEnabled(true);
            return;
        }

        if(CommonMethods.isNotAnEmail(email2)) {
            //username.setError("Please enter a valid email!");
            CommonMethods.warning(email, "Please provide a correct email address!");
            progressButton.resetDesign("Login");
            view.setEnabled(true);
            return;
        }

        if(CommonMethods.checkIfEmpty(pass)) {
            //password.setError("Password is required!");
            CommonMethods.warning(password, "Password is required!");
            progressButton.resetDesign("Login");
            view.setEnabled(true);
            return;
        }

        //not necessary
        if(CommonMethods.checkIfPassLengthNotValid(pass)) {
            //password.setError("Minimum password length is 6 characters!");
            CommonMethods.warning(password, "Minimum password length is 6 characters!");
            progressButton.resetDesign("Login");
            view.setEnabled(true);
            return;
        }

        mAuth.signInWithEmailAndPassword(email2, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    assert user != null;
                    //if (user.isEmailVerified()) {
                        progressButton.buttonFinished();

                        // add the option where the user should choose a building when signing up
                        // he can add only one additional building in case he didn't find his
                        //

                    goToMainActivity();
                    LoginActivity.sp.edit().putBoolean("loggedin",true).apply();
                    //}
                    /*else {
                        user.sendEmailVerification();
                        Toast.makeText(getActivity(), "Please check your email to verify your account.", Toast.LENGTH_SHORT).show();
                    }*/
                }
                else {
                    Toast.makeText(getActivity(), "Failed to login! Please check your credentials.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        progressButton.resetDesign("Login");
        view.setEnabled(true);

    } // end login method

    public void goToMainActivity() {
        Intent intent = new Intent(getActivity(), MainScreenActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        getActivity().finish();
    }

}
