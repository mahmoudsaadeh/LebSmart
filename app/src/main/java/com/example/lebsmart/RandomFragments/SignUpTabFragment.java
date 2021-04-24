package com.example.lebsmart.RandomFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.Activities.BuildingsListActivity;
import com.example.lebsmart.Database.FirebaseDatabaseMethods;
import com.example.lebsmart.Others.ProgressButton;
import com.example.lebsmart.R;
import com.example.lebsmart.TheftsFragments.Thefts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpTabFragment extends Fragment {

    EditText email;
    EditText password;
    private EditText confirmPassword;
    private EditText fullNameSignUp;
    private EditText phoneNumberSignUp;
    private RadioGroup radioGroupSignUp;
    private RadioButton radioButtonCommittee;
    private RadioButton radioButtonNormalResident;
    public static TextView selectABuildingTV;

    String personTypeString = "";
    //public static String buildingChosen;

    float alpha = 0;

    View view;

    ProgressButton progressButton;

    FirebaseAuth mAuth;

    public static final int PERSON_TYPE_NOT_SELECTED = -1;

    public static int BUILDING_LIST_REQUEST_CODE = 1;


    String fullName;
    String phone;
    String mail ;
    String passwordd ;
    String confirmPass;
    String selectABuilding;
    int personType;

    User newUser;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_fragment, container, false);

        fullNameSignUp = root.findViewById(R.id.fullNameSignUp);
        phoneNumberSignUp = root.findViewById(R.id.phoneNumberSignUp);
        email = root.findViewById(R.id.emailSignUp);
        password = root.findViewById(R.id.passwordSignUp);
        confirmPassword = root.findViewById(R.id.confirmPassword);
        radioGroupSignUp = root.findViewById(R.id.radioGroupSignUp);
        radioButtonCommittee = root.findViewById(R.id.radioButtonCommittee);
        radioButtonNormalResident = root.findViewById(R.id.radioButtonNormalResident);
        selectABuildingTV = root.findViewById(R.id.selectABuildingTV);
        view = root.findViewById(R.id.includeSignUp);

        selectABuildingTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BuildingsListActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, BUILDING_LIST_REQUEST_CODE);
            }
        });

        /*if (getArguments() != null) {
            buildingChosen = getArguments().getString("bN");
            selectABuildingTV.setText(buildingChosen);
        }*/

        mAuth = FirebaseAuth.getInstance();

        progressButton = new ProgressButton(getContext(), view);
        progressButton.resetDesign("Sign Up");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressButton.buttonActivated();

                view.setEnabled(false);

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
                                progressButton.resetDesign("Sign Up");
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

                signUp();

            }
        });

        return root;

    }

    public void signUp () {
        fullName = fullNameSignUp.getText().toString();
        phone = phoneNumberSignUp.getText().toString().trim();
        mail = email.getText().toString().trim();
        passwordd = password.getText().toString().trim();
        confirmPass = confirmPassword.getText().toString().trim();
        selectABuilding = selectABuildingTV.getText().toString();
        personType = radioGroupSignUp.getCheckedRadioButtonId();


        if(fullName.isEmpty()) {
            CommonMethods.warning(fullNameSignUp, "Username is required!");
            progressButton.resetDesign("Sign Up");
            view.setEnabled(true);
            return;
        }

        if (phone.isEmpty()) {
            CommonMethods.warning(phoneNumberSignUp, "Phone number is required!");
            progressButton.resetDesign("Sign Up");
            view.setEnabled(true);
            return;
        }
        else if (phone.length() < 8) {
            CommonMethods.warning(phoneNumberSignUp, "Phone number should be of 8 numbers!");
            progressButton.resetDesign("Sign Up");
            view.setEnabled(true);
            return;
        }
        else {
            char[] phoneArray = phone.toCharArray();
            String areaCode = String.valueOf(phoneArray[0]) + "" + String.valueOf(phoneArray[1]);
            //Log.i("areacode", areaCode);
            String number = "";
            for (int i=2; i<phoneArray.length; i++) {
                number = number + phoneArray[i];
            }
            //Log.i("num", number);
            phone = areaCode + "-" + number;
        }

        if(CommonMethods.checkIfEmpty(mail)) {
            CommonMethods.warning(email, "Email is required!");
            progressButton.resetDesign("Sign Up");
            view.setEnabled(true);
            return;
        }

        if(CommonMethods.isNotAnEmail(mail)) {
            CommonMethods.warning(email, "Please provide a correct email address!");
            progressButton.resetDesign("Sign Up");
            view.setEnabled(true);
            return;
        }

        if(CommonMethods.checkIfEmpty(passwordd)) {
            CommonMethods.warning(password, "Password is required!");
            progressButton.resetDesign("Sign Up");
            view.setEnabled(true);
            return;
        }

        if(CommonMethods.checkIfPassLengthNotValid(passwordd)) {
            CommonMethods.warning(password, "Minimum password length is 6 characters!");
            progressButton.resetDesign("Sign Up");
            view.setEnabled(true);
            return;
        }

        if(CommonMethods.checkIfEmpty(confirmPass)) {
            CommonMethods.warning(confirmPassword, "You need to confirm your password before you proceed!");
            progressButton.resetDesign("Sign Up");
            view.setEnabled(true);
            return;
        }
        else if(!CommonMethods.checkIfConfirmPassMatchesPass(confirmPass, passwordd)) {
            CommonMethods.warning(confirmPassword, "Your passwords do not match! Please recheck.");
            progressButton.resetDesign("Sign Up");
            view.setEnabled(true);
            return;
        }

        if (selectABuilding.isEmpty() || selectABuilding.equals("Select a Building")) {
            Toast.makeText(getActivity(), "Please select a building before you proceed!", Toast.LENGTH_SHORT).show();
            progressButton.resetDesign("Sign Up");
            view.setEnabled(true);
            return;
        }

        if(personType == PERSON_TYPE_NOT_SELECTED) {
            Toast.makeText(getActivity(), "You should choose a 'user type' before you continue!", Toast.LENGTH_SHORT).show();
            progressButton.resetDesign("Sign Up");
            view.setEnabled(true);
            return;
        }
        else {
            setPersonType(personType);
        }

        if (personTypeString.equals("Committee member")) {
            Log.i("1", "entered");
            checkNumberOfCommitteeMembers();
        }
        else {
            Log.i("2", "entered");
            signUserUp();
        }


    } // end sign up  method


    public void signUserUp () {

        Log.i("signuserup", "entered");

        mAuth.createUserWithEmailAndPassword(mail, passwordd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        /*Log.i("mail", mail);
                        Log.i("pass", passwordd);
                        Log.i("task", task.toString());*/

                        if (task.isSuccessful()) {
                            // add chosen building
                            Log.i("signuserup", "task 1 success");

                            newUser = new User(fullName, phone, mail, personTypeString, selectABuilding);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //Toast.makeText(getActivity(), "Sign up Successful!", Toast.LENGTH_SHORT).show();
                                        Log.i("signuserup", "task 2 success");

                                        progressButton.buttonFinished();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressButton.resetDesign("Sign Up");
                                                view.setEnabled(true);
                                            }
                                        }, 1777);

                                        addResidentToAssociatedBuilding();

                                    }
                                    else {
                                        Toast.makeText(getActivity(), "Failed to sign up, please try again.", Toast.LENGTH_SHORT).show();
                                        progressButton.resetDesign("Sign Up");
                                        view.setEnabled(true);
                                    }
                                }
                            });



                        }
                        else {
                            Log.i("signuserup", "task 1 failed");
                            Toast.makeText(getActivity(), "Account already exists! Please try again with a different email address.", Toast.LENGTH_SHORT).show();
                            progressButton.resetDesign("Sign Up");
                            view.setEnabled(true);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("failed", e.getMessage() + "");
            }
        });

    }


    public void setPersonType(int personType) {
        if(personType == R.id.radioButtonNormalResident) {
            personTypeString = radioButtonNormalResident.getText().toString();
            //Log.d("radiobtnn:", radioButtonStudent.getText().toString());
        }
        else if(personType == R.id.radioButtonCommittee) {
            personTypeString = radioButtonCommittee.getText().toString();
            //Log.d("radiobtnn:", radioButtonTeacher.getText().toString());
        }
    }



    public void addResidentToAssociatedBuilding () {

        Log.i("addResidentToAssociatedBuilding", "entered");

        FirebaseDatabase.getInstance().getReference("Buildings")
                .child(selectABuilding).child(personTypeString)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("adding resident to associated building", "success");
                    Toast.makeText(getActivity(), "Resident added to associated building successfully!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.i("adding resident to associated building", "failed");
                    Toast.makeText(getActivity(), "Failed to add resident to associated building!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // should be less than 3 to add new one
    public void checkNumberOfCommitteeMembers () {

        Log.i("checkNumberOfCommitteeMembers", "entered");

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Buildings");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(selectABuilding).exists()) {
                    Log.i("checkNumberOfCommitteeMembers", "if1 entered");
                    Log.i("childrenCount committee", String.valueOf(snapshot.child(selectABuilding).child(personTypeString).getChildrenCount()));
                    if (snapshot.child(selectABuilding).child(personTypeString).getChildrenCount() < 3) {
                        Log.i("checkNumberOfCommitteeMembers", "if2 entered");
                        signUserUp();
                    }
                    else {
                        Log.i("checkNumberOfCommitteeMembers", "else2 entered");
                        Toast.makeText(getActivity(), "Only 3 committee members are allowed per building! You can register as a normal resident instead.", Toast.LENGTH_LONG).show();
                        progressButton.resetDesign("Sign Up");
                        view.setEnabled(true);
                    }
                }
                else {
                    Log.i("checkNumberOfCommitteeMembers", "else1 entered");
                    signUserUp();
                }

                reference.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("error", error.getMessage());
                //CommonMethods.dismissLoadingScreen(progressDialog);
            }
        });

    }

}
