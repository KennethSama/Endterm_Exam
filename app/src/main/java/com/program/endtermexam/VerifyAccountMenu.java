package com.program.endtermexam;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VerifyAccountMenu extends AppCompatActivity {
    private Intent intent_dashboard, intent_teacherDashboard;    private FirebaseAuth firebaseAuth;

    private FirebaseUser current_user;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private RelativeLayout relativeLayout_modal;
    private ConstraintLayout progressBar;

    private Bundle bundle_current;
    private int sentEmail_number;

    private CurrentUser currentUser;
    private HashMap<String, String> userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account_menu);
        InitializeIntents();
        InitializeValues();
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

    private void InitializeIntents(){
        intent_dashboard = new Intent(VerifyAccountMenu.this, Dashboard.class);
        intent_teacherDashboard = new Intent(VerifyAccountMenu.this, TeacherDashboard.class);
    }

    private void InitializeValues(){
        firebaseAuth = FirebaseAuth.getInstance();
        current_user = firebaseAuth.getCurrentUser();

        rootNode = FirebaseDatabase.getInstance("https://endterm-exam-default-rtdb.firebaseio.com/");
        reference = rootNode.getReference("Users");

        relativeLayout_modal = findViewById(R.id.modal_message);
        progressBar = findViewById(R.id.progressBar3);

        ExtendedLayoutAccess.InitializeModal(relativeLayout_modal);

        bundle_current = getIntent().getExtras();
        sentEmail_number = 0;
    }


    public void OnResend(View view) {
        current_user.reload();
        progressBar.setVisibility(View.VISIBLE);
        if (sentEmail_number < 3) {
            sentEmail_number += 1;
            current_user.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(VerifyAccountMenu.this, "Please check your email to verify account. This method will help us confirm if your email is legitimate.", Toast.LENGTH_LONG).show();
                }
            });
        }else
            Toast.makeText(VerifyAccountMenu.this, "You have reached the total number of email request today. Kindly check your email", Toast.LENGTH_SHORT).show();
    }

    public void OnVerify(View view) {
        current_user.reload();
        progressBar.setVisibility(View.VISIBLE);

        if (current_user.isEmailVerified()) {

            InitiaizeData();
            InitializeContents();
//            startActivity(new Intent(VerifyAccountMenu.this, Dashboard.class));
//            finish();
        }else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(VerifyAccountMenu.this, "Sorry the account is not yet verified. Please check your email to verify account", Toast.LENGTH_SHORT).show();
        }
    }

    private void InitiaizeData(){
        progressBar.setVisibility(View.VISIBLE);
        currentUser = new CurrentUser();
        currentUser.InitializeUserData(true);
        currentUser.InitializeUserID();
        currentUser.InitializePreferences(this);
        currentUser.SetUserData(this);
    }
    private void InitializeContents(){
        new Handler().postDelayed(() -> {
            if(currentUser.canUpdate){
                progressBar.setVisibility(View.GONE);
                currentUser.canUpdate = false;
                userDetails = currentUser.GetUserSession();
                Log.d("userDetails", userDetails.toString());
                String fullname = userDetails.get("firstName").concat(" " + userDetails.get("middleName").charAt(0) + ". " + userDetails.get("lastName"));
                Log.d("userDetails", fullname);
                if (userDetails.get("type").equals(getResources().getString(R.string.type_stud)))
                    startActivity(intent_dashboard);
                else
                    startActivity(intent_dashboard);
                finish();
            }
        }, 2000);
    }
}