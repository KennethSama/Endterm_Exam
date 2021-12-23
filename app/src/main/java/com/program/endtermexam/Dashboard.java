package com.program.endtermexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {
    private Intent intent_viewAll;
    private Intent intent_viewCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        new com.program.endtermexam.GoBack(null, this);
        InitializeIntents();
    }

    private void InitializeIntents(){
        intent_viewAll = new Intent(Dashboard.this, courses.class);
        intent_viewCourse = new Intent(Dashboard.this, ViewCourse.class);
    }
    public void ViewAll(View view) {
        Toast.makeText(this, String.format("View ID: %s", view.getId()), Toast.LENGTH_SHORT).show();
        String action_bar_title = null;
        String card_title = null;
        switch (view.getId()){
            case R.id.button_viewAllCourses: {
                action_bar_title = getString(R.string.app_courses);
                card_title = getString(R.string.hint_modulenums);
            } break;
            case R.id.button_viewAllQuizzes:
            case R.id.button_viewAllQuizzes2: {
                action_bar_title = getString(R.string.app_quizzes);
                card_title = getString(R.string.hint_quizzesnums);
            } break;
            case R.id.button_viewAllAttendance:
            case R.id.button_viewAllAttendance2: {
                action_bar_title = getString(R.string.app_attend);
                card_title = getString(R.string.hint_attendPerc);
            } break;
            case R.id.button_viewAllGrades:
            case R.id.button_viewAllGrades2: {
                action_bar_title = getString(R.string.app_grades);
                card_title = getString(R.string.hint_gradesnums);
            } break;
        }
        intent_viewAll.putExtra("card_name", card_title);
        intent_viewAll.putExtra("action_bar_name", action_bar_title);
        intent_viewAll.putExtra("course_name", getString(R.string.hint_coursename));
        startActivity(intent_viewAll);
    }

    public void ViewCourse(View view) {
        int id = view.getId();
        Toast.makeText(this, String.format("View ID: %s", id), Toast.LENGTH_SHORT).show();
        startActivity(intent_viewCourse);
    }
}