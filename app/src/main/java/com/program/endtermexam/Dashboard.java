package com.program.endtermexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {
    private Intent intent_viewAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        InitializeIntents();
    }
    private void InitializeIntents(){
        intent_viewAll = new Intent(Dashboard.this, courses.class);
    }
    public void ViewAll(View view) {
        Toast.makeText(this, String.format("View ID: %s", view.getId()), Toast.LENGTH_SHORT).show();
        startActivity(intent_viewAll);
//        Log.d("View All ID", String.format("View ID: %s", view.getId()));
    }
}