package com.program.endtermexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ViewCourse extends AppCompatActivity {
    private Intent intent_viewTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);
        InitializeIntents();
        new com.program.endtermexam.GoBack(null, this);
    }
    private void InitializeIntents(){
        intent_viewTopic = new Intent(ViewCourse.this, ViewTopic.class);
    }
    public void ViewTopic(View view) {
        startActivity(intent_viewTopic);
    }
}