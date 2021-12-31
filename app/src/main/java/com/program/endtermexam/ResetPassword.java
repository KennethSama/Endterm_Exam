package com.program.endtermexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
    private TextInputLayout textInputLayout_email;
    private FirebaseAuth firebaseAuth;
    private ConstraintLayout progressBar;
    private RelativeLayout relativeLayout_modal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        InitializeValues();
    }

    private void InitializeValues(){
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar3);
        relativeLayout_modal = findViewById(R.id.modal_message);
        textInputLayout_email = findViewById(R.id.textInputLayout_Email);
    }

    public void OnLogin(View view) { ExtendedLayoutAccess.OnLogin(this); }

    public void OnSingup(View view) { ExtendedLayoutAccess.OnSignup(this);}

    private boolean IsEmailValid(CharSequence email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void OnResetPassword(View view) {
        String email = textInputLayout_email.getEditText().getText().toString().trim();
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
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               ExtendedLayoutAccess.OnLogin(ResetPassword.this);
               Toast.makeText(this, "We've sent you an email! Kindly check it to reset your password", Toast.LENGTH_SHORT).show();
           }else{
               Toast.makeText(this, "Reset password failed! Please Try again", Toast.LENGTH_SHORT).show();
           }
        });
    }
}