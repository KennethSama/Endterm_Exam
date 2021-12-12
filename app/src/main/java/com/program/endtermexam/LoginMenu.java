package com.program.endtermexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginMenu extends AppCompatActivity {
    private Intent intent_signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_menu);
        InitializeIntents();
    }
    private void InitializeIntents(){ intent_signup = new Intent(LoginMenu.this, SignupMenu.class); }
    public void OnSignUp(View view) {
        startActivity(intent_signup);
        finish();
    }
}