package com.program.endtermexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginMenu extends AppCompatActivity {
    private Intent intent_signup;
    private Intent intent_dashboard;
    private FirebaseAuth firebaseAuth;
    private TextInputLayout textInputLayout_email, textInputLayout_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_menu);
        InitializeValues();
        InitializeIntents();
    }
    private void InitializeIntents(){
        intent_signup = new Intent(LoginMenu.this, SignupMenu.class);
        intent_dashboard = new Intent(LoginMenu.this, Dashboard.class);
    }
    private void InitializeValues(){
        firebaseAuth = FirebaseAuth.getInstance();

        textInputLayout_email = findViewById(R.id.textInputLayout_Email);
        textInputLayout_password = findViewById(R.id.textInputLayout_Password);
    }

    public void OnSignUp(View view) {
        startActivity(intent_signup);
        finish();
    }

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

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                startActivity(intent_dashboard);
                finish();
            }else
                Toast.makeText(LoginMenu.this, "Account not found!\nPlease check your credentials/details", Toast.LENGTH_LONG).show();
        });
    }

    public void ForgotPassword(View view) {

    }
}