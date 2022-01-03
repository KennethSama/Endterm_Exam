package com.program.endtermexam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileMenu extends AppCompatActivity {
    private ConstraintLayout progressBar_profile;
    private RelativeLayout relativeLayout_modal;
    private CurrentUser currentUser;
    private HashMap<String, String> userDetails;
    private TextView textView_fullname, textView_email, textView_program, textView_location, textView_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_menu);

        ExtendedLayoutAccess.AccessAppBar(null, this, getString(R.string.app_prof));
        InitializeValues();
        InitiaizeData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        InitializeContents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ExtendedLayoutAccess.CheckConnection(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ExtendedLayoutAccess.RemoveHandler();
    }
    private void InitializeValues(){
        relativeLayout_modal = findViewById(R.id.modal_message);
        progressBar_profile = findViewById(R.id.progressBar_profile);
        textView_fullname = findViewById(R.id.textView_fullname);
        textView_email = findViewById(R.id.textView_email);
        textView_program = findViewById(R.id.textView_program);
        textView_location = findViewById(R.id.textView_location);
        textView_type = findViewById(R.id.textView_type);

        ExtendedLayoutAccess.InitializeModal(relativeLayout_modal);
    }

    private void InitializeContents(){
        userDetails = currentUser.GetUserIndividualSession();
        String fullname = userDetails.get("firstName").concat(" " + userDetails.get("middleName").charAt(0) + ". " + userDetails.get("lastName"));
        ExtendedLayoutAccess.AccessNavBar(null, getString(R.string.app_prof)
                , fullname
                , userDetails.get("email"), userDetails.get("location"), userDetails.get("academicProgram"));

        textView_fullname.setText(fullname);
        textView_email.setText(userDetails.get("email"));
        textView_program.setText(userDetails.get("academicProgram"));
        textView_location.setText(userDetails.get("location"));
        textView_type.setText(userDetails.get("type"));
    }
    private void InitiaizeData(){
        currentUser = new CurrentUser();
        currentUser.InitializeUserData(false);
        currentUser.InitializeUserID();
        currentUser.InitializePreferences(this);
//        currentUser.SetUserData(this);
    }

}