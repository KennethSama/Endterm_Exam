package com.program.endtermexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Handler _handler = new Handler();
        _handler.postDelayed(() -> {
            Intent intent_homeScreen = new Intent(SplashScreenActivity.this, LoginMenu.class);
            startActivity(intent_homeScreen);
            finish();
        }, 4000);
    }
}