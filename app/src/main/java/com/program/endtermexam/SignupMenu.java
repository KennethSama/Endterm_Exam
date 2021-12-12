package com.program.endtermexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SignupMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_menu);
    }

    public void OnLogin(View view) {
        Intent intent_login = new Intent(SignupMenu.this, LoginMenu.class);
        startActivity(intent_login);
        finish();
    }
}