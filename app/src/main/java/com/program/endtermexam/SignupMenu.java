package com.program.endtermexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SignupMenu extends AppCompatActivity {
    private Intent intent_login;
    private Intent intent_dashboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_menu);
        InitializeIntents();
    }
    private void InitializeIntents(){
        intent_login = new Intent(SignupMenu.this, LoginMenu.class);
        intent_dashboard = new Intent(SignupMenu.this, Dashboard.class);
    }

    public void OnLogin(View view) {
        startActivity(intent_login);
        finish();
    }

    public void OnDashboard(View view) {
        startActivity(intent_dashboard);
        finish();
    }
}