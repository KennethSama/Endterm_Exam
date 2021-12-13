package com.program.endtermexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginMenu extends AppCompatActivity {
    private Intent intent_signup;
    private Intent intent_dashboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_menu);
        InitializeIntents();
    }
    private void InitializeIntents(){
        intent_signup = new Intent(LoginMenu.this, SignupMenu.class);
        intent_dashboard = new Intent(LoginMenu.this, Dashboard.class);
    }
    public void OnSignUp(View view) {
        startActivity(intent_signup);
        finish();
    }

    public void OnDashboard(View view) {
        startActivity(intent_dashboard);
        finish();
    }
}