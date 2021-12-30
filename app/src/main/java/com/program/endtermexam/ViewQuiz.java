package com.program.endtermexam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ViewQuiz extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_quiz);
        ExtendedLayoutAccess.AccessAppBar(null, this, getString(R.string.hint_quiz));
    }
}