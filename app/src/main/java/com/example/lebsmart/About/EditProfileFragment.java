package com.example.lebsmart.About;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.Activities.LoginActivity;
import com.example.lebsmart.Activities.MainScreenActivity;
import com.example.lebsmart.ApartmentsFragments.CheckApartmentsFragment;
import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.example.lebsmart.RandomFragments.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileFragment extends Fragment {

    ViewGroup viewGroup;

    Button editProfileBtn;

    EditText fullNameEP, phoneEP, emailEP, passwordEP, confirmPasswordEP, oldPasswordEP;
    RadioGroup radioGroupEditProfile;
    RadioButton radioButtonCommitteeEP, radioButtonNormalResidentEP;

    String getName, getPhone, getMail, getUserType, getBuilding;
    String fullName, phone, mail, password, confirmPassword, userTypeString, oldPass;
    int userType;

    public static final int NO_RADIO_BUTTON_SELECTED = -1;

    ProgressDialog progressDialog;

    boolean gotInfo;
    boolean authUpdatedMail, authUpdatedPass;

    FirebaseAuth mAuth;

    String currentUserId;

    User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.edit_profile_fragment, container, false);

        progressDialog = new ProgressDialog(getActivity());

        // get existing user data and set edittexts with these
        // when button clicked, check for any change, if yes, update db
        // else, toast: no changes were recognized.

        editProfileBtn = viewGroup.findViewById(R.id.editProfileBtn);

        fullNameEP = viewGroup.findViewById(R.id.fullNameEP);
        phoneEP = viewGroup.findViewById(R.id.phoneEP);
        emailEP = viewGroup.findViewById(R.id.emailEP);
        passwordEP = viewGroup.findViewById(R.id.passwordEP);
        confirmPasswordEP = viewGroup.findViewById(R.id.confirmPasswordEP);
        oldPasswordEP = viewGroup.findViewById(R.id.oldPasswordEP);

        radioGroupEditProfile = viewGroup.findViewById(R.id.radioGroupEditProfile);

        radioButtonCommitteeEP = viewGroup.findViewById(R.id.radioButtonCommitteeEP);
        radioButtonNormalResidentEP = viewGroup.findViewById(R.id.radioButtonNormalResidentEP);

        getCurrentUserInfo();

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile1();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        return viewGroup;
    }


    public void editProfile1() {
        fullName = fullNameEP.getText().toString();
        phone = phoneEP.getText().toString();
        mail = emailEP.getText().toString();
        oldPass = oldPasswordEP.getText().toString();
        password = passwordEP.getText().toString();
        confirmPassword = confirmPasswordEP.getText().toString();
        userType = radioGroupEditProfile.getCheckedRadioButtonId();

        if(fullName.isEmpty()) {
            CommonMethods.warning(fullNameEP, "Username is required!");
            return;
        }

        if (phone.isEmpty()) {
            CommonMethods.warning(phoneEP, "Phone number is required!");
            return;
        }
        else if (phone.length() < 8) {
            CommonMethods.warning(phoneEP, "Phone number should be of 8 numbers!");
            return;
        }
        else {
            if (!phone.contains("-")) {
                char[] phoneArray = phone.toCharArray();
                String areaCode = phoneArray[0] + "" + phoneArray[1];
                String number = "";
                for (int i=2; i<phoneArray.length; i++) {
                    number = number + phoneArray[i];
                }
                phone = areaCode + "-" + number;
            }
        }

        if(CommonMethods.checkIfEmpty(mail)) {
            CommonMethods.warning(emailEP, "Email is required!");
            return;
        }

        if(CommonMethods.isNotAnEmail(mail)) {
            CommonMethods.warning(emailEP, "Please provide a correct email address!");
            return;
        }

        if(CommonMethods.checkIfEmpty(oldPass)) {
            CommonMethods.warning(oldPasswordEP, "Old Password is required!");
            return;
        }

        if(CommonMethods.checkIfPassLengthNotValid(oldPass)) {
            CommonMethods.warning(oldPasswordEP, "Minimum password length is 6 characters!");
            return;
        }

        if(CommonMethods.checkIfEmpty(password)) {
            CommonMethods.warning(passwordEP, "Password is required!");
            return;
        }

        if(CommonMethods.checkIfPassLengthNotValid(password)) {
            CommonMethods.warning(passwordEP, "Minimum password length is 6 characters!");
            return;
        }

        if(CommonMethods.checkIfEmpty(confirmPassword)) {
            CommonMethods.warning(confirmPasswordEP, "You need to confirm your password before you proceed!");
            return;
        }
        else if(!CommonMethods.checkIfConfirmPassMatchesPass(confirmPassword, password)) {
            CommonMethods.warning(confirmPasswordEP, "Your passwords do not match! Please recheck.");
            return;
        }

        if(userType == NO_RADIO_BUTTON_SELECTED) {
            Toast.makeText(getActivity(), "You should choose a 'user type' before you proceed!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            setPersonType(userType);
        }

        if (userTypeString.equals("Committee member")) {
            checkNumberOfCommitteeMembers();
        }
        else {
            editProfile2();
        }

    }

    public void editProfile2() {

        CommonMethods.displayLoadingScreen(progressDialog);

        authUpdatedMail = false;
        authUpdatedPass = false;

        AuthCredential credential = EmailAuthProvider
                .getCredential(getMail, oldPass);

        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();

        if (user1 == null) {
            Log.i("user", "null");
            CommonMethods.dismissLoadingScreen(progressDialog);
            return;
        }

        user1.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updateEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("mail update", "User email address updated.");
                                authUpdatedMail = true;
                            }
                            else {
                                authUpdatedMail = false;
                                Log.d("mail update", "failed");
                            }
                        }
                    });/*.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("mail fail", e.getMessage());
                        }
                    });*/
                    user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("password update", "User password updated.");
                                authUpdatedPass = true;
                            }
                            else {
                                authUpdatedPass = false;
                                Log.d("password update", "failed");
                            }
                        }
                    });
                }
            }
        });



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (authUpdatedMail && authUpdatedPass) {
                    editProfile3();
                }
                else {
                    CommonMethods.dismissLoadingScreen(progressDialog);
                    Toast.makeText(getActivity(), "Failed to update your credentials!", Toast.LENGTH_SHORT).show();
                }
            }
        }, 3777);

    }


    public void editProfile3() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        user = new User(fullName, phone, mail, userTypeString, getBuilding);
        reference.child(currentUserId)
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("Users node", "updated!");
                    //CommonMethods.dismissLoadingScreen(progressDialog);
                    Toast.makeText(getActivity(), "Your profile was successfully updated!", Toast.LENGTH_LONG).show();
                    resignInUser();
                }
                else {
                    CommonMethods.dismissLoadingScreen(progressDialog);
                    Toast.makeText(getActivity(), "Failed to update your profile! Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void resignInUser() {
        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i("resign in", "success");
                    CheckApartmentsFragment.getUserData();
                    updateResidentInfoInBuilding();
                }
                else {
                    Log.i("resign in", "failed");
                    Toast.makeText(getActivity(), "You'll be logged out to verify your changes.", Toast.LENGTH_LONG).show();
                    logout();
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getCurrentUserInfo();
                CommonMethods.dismissLoadingScreen(progressDialog);
            }
        }, 3555);
    }


    public void setPersonType(int personType) {
        if(personType == R.id.radioButtonNormalResidentEP) {
            userTypeString = radioButtonNormalResidentEP.getText().toString();
            //Log.d("radiobtnn:", radioButtonStudent.getText().toString());
        }
        else if(personType == R.id.radioButtonCommitteeEP) {
            userTypeString = radioButtonCommitteeEP.getText().toString();
            //Log.d("radiobtnn:", radioButtonTeacher.getText().toString());
        }
    }

    public void checkNumberOfCommitteeMembers () {

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Buildings");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(getBuilding).exists()) {
                    if (snapshot.child(getBuilding).child(userTypeString).getChildrenCount() < 3) {
                        editProfile2();
                    }
                    else {
                        Toast.makeText(getActivity(), "Only 3 committee members are allowed per building! You can register as a normal resident instead.", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    editProfile2();
                }

                reference.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("error", error.getMessage());
                CommonMethods.dismissLoadingScreen(progressDialog);
            }
        });

    }



    public void getCurrentUserInfo() {

        CommonMethods.displayLoadingScreen(progressDialog);

        gotInfo = true;

        DatabaseReference reference;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUserId = user.getUid();
            getMail = user.getEmail();
            reference = FirebaseDatabase.getInstance()
                    .getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    getName = snapshot.child("fullName").getValue().toString();
                    getPhone = snapshot.child("phone").getValue().toString();
                    getUserType = snapshot.child("userType").getValue().toString();
                    getBuilding = snapshot.child("buildingChosen").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("error", error.getMessage());
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
            });
        } else {
            // No user is signed in
            Log.i("getUserInfo", "failed");
            CommonMethods.dismissLoadingScreen(progressDialog);
            //Toast.makeText(getActivity(), "Failed to get current user!", Toast.LENGTH_SHORT).show();

            gotInfo = false;
        }



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(gotInfo) {

                    getPhone = getPhone.replace("-", "");

                    fullNameEP.setText(getName);
                    phoneEP.setText(getPhone);
                    emailEP.setText(getMail);

                    if (getUserType.equals("Committee member")) {
                        radioButtonCommitteeEP.setChecked(true);
                    }
                    else {
                        radioButtonNormalResidentEP.setChecked(true);
                    }

                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
            }
        }, 2777);

    }


    public void logout() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            LoginActivity.sp.edit().putBoolean("loggedin", false).apply();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //finishAffinity();

            startActivity(intent);
            //finish();
            //finishAndRemoveTask();
        }
        else {
            Toast.makeText(getActivity(), "You're already logged out!", Toast.LENGTH_SHORT).show();

            /*FirebaseAuth.getInstance().signOut();
            LoginActivity.sp.edit().putBoolean("loggedin", false).apply();
            Intent intent = new Intent(MainScreenActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //finishAffinity();

            startActivity(intent);*/
        }
    }


    public void updateResidentInfoInBuilding () {

        if (!userTypeString.equals(getUserType)) {
            FirebaseDatabase.getInstance().getReference("Buildings")
                    .child(getBuilding).child(userTypeString)
                    .child(currentUserId)
                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.i("user update in building", "success");
                    }
                    else {
                        Log.i("user update in building", "failed");
                    }
                }
            });

            FirebaseDatabase.getInstance().getReference("Buildings")
                    .child(getBuilding).child(getUserType)
                    .child(currentUserId)
                    .removeValue();
        }
        else {
            Log.i("userType", "still same");
        }

    }

}
