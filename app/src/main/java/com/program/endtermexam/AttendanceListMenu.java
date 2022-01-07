package com.program.endtermexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AttendanceListMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list_menu);

        ExtendedLayoutAccess.AccessAppBar(null, this, getString(R.string.app_attend));
    }

    public void TestAttendance(View view){
        Intent intent_attendanceTest = new Intent(AttendanceListMenu.this, ViewAttendance.class);
        startActivity(intent_attendanceTest);
    }
}