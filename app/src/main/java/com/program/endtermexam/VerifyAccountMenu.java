package com.program.endtermexam;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class VerifyAccountMenu extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser current_user;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private RelativeLayout relativeLayout_modal;
    private ConstraintLayout progressBar;
    private List<String> userData;

    private Bundle bundle_current;
    private int sentEmail_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account_menu);
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
            startActivity(new Intent(VerifyAccountMenu.this, Dashboard.class));
            finish();
        }else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(VerifyAccountMenu.this, "Sorry the account is not yet verified. Please check your email to verify account", Toast.LENGTH_SHORT).show();
        }
    }


//    public void OnVerify(View view) {
//        current_user.reload();
//        progressBar.setVisibility(View.VISIBLE);
//
//        if (current_user.isEmailVerified()) {
//            userData = new ArrayList<>();
//
//            userData.add(String.valueOf(bundle_current.getString("extra_email")));
//            userData.add(String.valueOf(bundle_current.getString("extra_fname")));
//            userData.add(String.valueOf(bundle_current.getString("extra_mname")));
//            userData.add(String.valueOf(bundle_current.getString("extra_lname")));
//            userData.add(String.valueOf(bundle_current.getString("extra_pass")));
//            userData.add(String.valueOf(bundle_current.getString("extra_program")));
//            userData.add(String.valueOf(bundle_current.getString("extra_location")));
//            userData.add(String.valueOf(bundle_current.getString("extra_type")));
//
//            UserHelper userHelper = new UserHelper(userData.get(0), userData.get(1), userData.get(2), userData.get(3), userData.get(4), userData.get(5), userData.get(6), userData.get(7));
//
//            reference.child("User_".concat(FirebaseAuth.getInstance().getCurrentUser().getUid())).setValue(userHelper).addOnCompleteListener(task2 -> {
//                if (task2.isSuccessful()) {
//                    Toast.makeText(VerifyAccountMenu.this, "User Has been registered!", Toast.LENGTH_SHORT).show();
//                    ExtendedLayoutAccess.HideModal();
//                    startActivity(new Intent(VerifyAccountMenu.this, Dashboard.class));
//                    finish();
//                } else
//                    Toast.makeText(VerifyAccountMenu.this, "Invalid Authentication! User has not been registered", Toast.LENGTH_SHORT).show();
//            });
//        }else {
//            progressBar.setVisibility(View.GONE);
//            Toast.makeText(VerifyAccountMenu.this, "Sorry the account is not yet verified. Please check your email to verify account", Toast.LENGTH_SHORT).show();
//        }
//    }
}