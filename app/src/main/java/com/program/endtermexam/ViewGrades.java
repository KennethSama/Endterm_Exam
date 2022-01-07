package com.program.endtermexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.math.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
public class ViewGrades extends AppCompatActivity {

    private ConstraintLayout progressBar;
    private HashMap<String, String> userDetails;

    private CurrentUser currentUser;
    private LinearLayout linearLayout_gradesList;

    private ArrayList<Integer> allScore_user;
    private ArrayList<Integer> allScore_total;
    private TextView textView_prelimAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_grades);

        ExtendedLayoutAccess.AccessAppBar(null, this, getResources().getString(R.string.app_grades));

        InitializeValues();
        InitiaizeData();
        InitializeContents();

        String userID = String.valueOf(getIntent().getExtras().getString("user_id"));

        String userType = String.valueOf(getIntent().getExtras().getString("user_type"));

        SearchData(userID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        CreateContent();
    }

    public boolean canUpdate = false;
    private LayoutInflater layoutInflater;
    private void InitializeValues(){
        linearLayout_gradesList = findViewById(R.id.gradesList);
        progressBar = findViewById(R.id.progressBar_login);
        textView_prelimAverage = findViewById(R.id.textView_prelimAverage);
        layoutInflater = (LayoutInflater) getSystemService(Courses.LAYOUT_INFLATER_SERVICE);

        allScore_user = new ArrayList<>();
        allScore_total = new ArrayList<>();
        allScore_user.clear();
        allScore_total.clear();
    }
    public int childCount;
    private void SearchData(String userID){
        String substring = " out of ";
        DatabaseReference grades = FirebaseDatabase.getInstance("https://endterm-exam-default-rtdb.firebaseio.com/").getReference("Grades").child(userID);

        grades.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int z = 0;
                childCount = (int) snapshot.getChildrenCount();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String value = dataSnapshot.getValue().toString();
                    String[] values = dataSnapshot.getValue().toString().split(substring);
                    String key = dataSnapshot.getKey();
                    String status ="";
                    Log.d("divide", values[0]);
                    Log.d("divide", values[1]);
                    Log.d("divide", key);
                    allScore_total.add(Integer.parseInt(values[1]));
                    allScore_user.add(Integer.parseInt(values[0]));

                    int score = Integer.parseInt(values[0]);
                    int total = Integer.parseInt(values[1]);
                    Log.d("divide data", Integer.toString(score));
                    Log.d("divide data", Float.toString(Percentage( 75, total)));
                    if (score >= (int) Percentage( 75, total) )
                        status = "Pass";
                    else
                        status = "Fail";
                    CreateList(layoutInflater, key, value, status);
                    z++;
                }
                if (z >= childCount){
                    progressBar.setVisibility(View.GONE);
                    canUpdate = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(ViewGrades.this, "Something went wrong", Toast.LENGTH_SHORT).show(); }
        });

    }

    private HashMap<String, String> currentGrades = new HashMap<>();

    float Percentage(float percent, float whole){ return (percent * whole) / 100; }

    public void CreateContent(){
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            if(canUpdate){
                canUpdate = false;
                textView_prelimAverage.setText(Float.toString(GetPrelimGrade()));
            }else
                CreateContent();
        }, 100);
    }
    private void InitiaizeData(){
        currentUser = new CurrentUser();
        currentUser.InitializeUserData(false);
        currentUser.InitializeUserID();
        currentUser.InitializePreferences(this);
    }
    private void InitializeContents(){
        userDetails = currentUser.GetUserIndividualSession();
        String fullname = userDetails.get("firstName").concat(" " + userDetails.get("middleName").charAt(0) + ". " + userDetails.get("lastName"));

        ExtendedLayoutAccess.AccessNavBar(null, getString(R.string.app_dash)
                , fullname, userDetails.get("email"), userDetails.get("location"), userDetails.get("academicProgram"), userDetails.get("type"));
    }


    private void CreateList(LayoutInflater inflater, String category, String description, String value){
        View view = inflater.inflate(R.layout.list_buttons,null);
        TextView textView_category = view.findViewById(R.id.textView_category);
        TextView textView_description = view.findViewById(R.id.textView_description);
        TextView textView_value = view.findViewById(R.id.textView_value);

        textView_category.setText(category);
        textView_description.setText(description);
        textView_value.setText(value);
        linearLayout_gradesList.addView(view);
    }

    private float GetPrelimGrade(){
        float average = Summation(allScore_user);
        float total = Summation(allScore_total);
        Log.d("average",Float.toString(average));
        Log.d("average",Float.toString(total));

        return average / childCount;
    }
//    private float GetAverage(){
//        return average / childCount;
//    }
    private float Summation(ArrayList<Integer> scores ){
        int totalScore = 0;
        for (int score : scores)
            totalScore += score;
        return totalScore;
    }

}