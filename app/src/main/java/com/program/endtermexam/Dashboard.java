package com.program.endtermexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

public class Dashboard extends AppCompatActivity {
    private Intent intent_viewAll, intent_viewCourse;
    private RelativeLayout relativeLayout_modal;
    private TextView textView_user;

    private HashMap<String, String> userDetails;
    private CurrentUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ExtendedLayoutAccess.AccessAppBar(null, this, getString(R.string.app_dash));
        InitializeIntents();
        InitializeValues();
        InitiaizeData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        InitializeContents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ExtendedLayoutAccess.CheckConnection(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ExtendedLayoutAccess.RemoveHandler();
    }

    private void InitializeValues(){

        relativeLayout_modal = findViewById(R.id.modal_message);
        textView_user = findViewById(R.id.textView_user);

        ExtendedLayoutAccess.InitializeModal(relativeLayout_modal);
    }
    private void InitializeContents(){
        userDetails = currentUser.GetUserIndividualSession();
        String fullname = userDetails.get("firstName").concat(" " + userDetails.get("middleName").charAt(0) + ". " + userDetails.get("lastName"));

        ExtendedLayoutAccess.AccessNavBar(null, getString(R.string.app_dash)
                , fullname, userDetails.get("email"), userDetails.get("location"), userDetails.get("academicProgram"), userDetails.get("type"));

        textView_user.setText(userDetails.get("firstName"));
    }
    private void InitiaizeData(){
        currentUser = new CurrentUser();
        currentUser.InitializeUserData(false);
        currentUser.InitializeUserID();
        currentUser.InitializePreferences(this);
    }

    private void InitializeIntents(){
        intent_viewAll = new Intent(Dashboard.this, Courses.class);
        intent_viewCourse = new Intent(Dashboard.this, ViewCourse.class);
    }
    public void ViewAll(View view) {
        String action_bar_title = null;
        String card_title = null;
        Intent intent_next = null;
        intent_next = intent_viewCourse;
        switch (view.getId()){
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
//        intent_viewAll.putExtra("course_name", getString(R.string.hint_coursename));
        intent_viewAll.putExtra("next_intent", intent_next);

        startActivity(intent_viewAll);
    }
}