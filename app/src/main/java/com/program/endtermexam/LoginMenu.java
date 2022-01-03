package com.program.endtermexam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class LoginMenu extends AppCompatActivity {
    private Intent intent_dashboard;
    private FirebaseAuth firebaseAuth;
    private TextInputLayout textInputLayout_email, textInputLayout_password;
    private RelativeLayout relativeLayout_modal;
    private ConstraintLayout progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_menu);
        InitializeValues();
        InitializeIntents();
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
        intent_dashboard = new Intent(LoginMenu.this, Dashboard.class);
    }
    private void InitializeValues(){
        relativeLayout_modal = findViewById(R.id.modal_message);
        progressBar = findViewById(R.id.progressBar_login);


        ExtendedLayoutAccess.InitializeModal(relativeLayout_modal);

        firebaseAuth = FirebaseAuth.getInstance();

        textInputLayout_email = findViewById(R.id.textInputLayout_Email);
        textInputLayout_password = findViewById(R.id.textInputLayout_Password);
    }

    public void OnSignUp(View view) { ExtendedLayoutAccess.OnSignup(this); }

    private boolean IsEmailValid(CharSequence email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public void OnLogin(View view) {
        String email = textInputLayout_email.getEditText().getText().toString().trim();
        String password = textInputLayout_password.getEditText().getText().toString();

        if (email.isEmpty()) {
            textInputLayout_email.setError("Error! Email is empty!");
            textInputLayout_email.requestFocus();
            Toast.makeText(this, "Please check the input fields", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!IsEmailValid(email)) {
            textInputLayout_email.setError("Invalid Email Format!");
            textInputLayout_email.requestFocus();
            Toast.makeText(this, "Please check the email provided", Toast.LENGTH_SHORT).show();
            return;
        }else
            textInputLayout_email.setError(null);
        if (password.isEmpty()){
            textInputLayout_password.setError("Error! Password is empty");
            textInputLayout_password.requestFocus();
            Toast.makeText(this, "Please check the input fields", Toast.LENGTH_SHORT).show();
            return;
        }else
            textInputLayout_password.setError(null);
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            ExtendedLayoutAccess.CheckConnection(this);
            if (task.isSuccessful()){
                progressBar.setVisibility(View.GONE);
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    CurrentUser currentUser = new CurrentUser("User_".concat(user.getUid()), password);
                    currentUser.InitializeUserData(true);
                    HashMap userMap =  new HashMap();
                    userMap.put("password", currentUser.getUserPassword());
                    currentUser.getDatabaseReference_user().child(currentUser.getUserID()).updateChildren(userMap);
                    if (user.isEmailVerified())
                        startActivity(intent_dashboard);
                    else
                        startActivity(new Intent(LoginMenu.this, VerifyAccountMenu.class));
                    finish();
                }else
                    Toast.makeText(LoginMenu.this, "user not logged in!\nPlease contact the technical support team", Toast.LENGTH_LONG).show();
            }else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginMenu.this, "Account not found!\nPlease check your credentials/details", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void ForgotPassword(View view) {
        ExtendedLayoutAccess.ForgotPassword(this);
    }
}