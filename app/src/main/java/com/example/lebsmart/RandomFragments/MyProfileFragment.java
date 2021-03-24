package com.example.lebsmart.RandomFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.lebsmart.Others.deleteProfileDialog;
import com.example.lebsmart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfileFragment extends Fragment {

    ViewGroup root;

    ProgressDialog progressDialog;
    TextView fullNameContent, emailContent, phoneMPContent, buildingContent, userTypeContent;
    Button deleteProfile;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.my_profile_fragment, container, false);

        fullNameContent = root.findViewById(R.id.fullNameContent);
        emailContent = root.findViewById(R.id.emailContent);
        phoneMPContent = root.findViewById(R.id.phoneMPContent);
        buildingContent = root.findViewById(R.id.buildingContent);
        userTypeContent = root.findViewById(R.id.userTypeContent);
        deleteProfile=(Button) root.findViewById(R.id.deleteProfileBtn);
        progressDialog = new ProgressDialog(getActivity());

        getProfileInfo();

        return root;
    }

    public void getProfileInfo () {
        CommonMethods.displayLoadingScreen(progressDialog);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fullNameContent.setText(snapshot.child("fullName").getValue().toString());
                    emailContent.setText(snapshot.child("email").getValue().toString());
                    phoneMPContent.setText(snapshot.child("phone").getValue().toString());
                    buildingContent.setText(snapshot.child("buildingChosen").getValue().toString());
                    userTypeContent.setText(snapshot.child("userType").getValue().toString());

                    CommonMethods.dismissLoadingScreen(progressDialog);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    deleteProfile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openDialog();
        }
    });
    }
    public void openDialog(){
        deleteProfileDialog dialog = new deleteProfileDialog();

        dialog.show(getFragmentManager(),"Delete Profile Dialog");




    }
}
