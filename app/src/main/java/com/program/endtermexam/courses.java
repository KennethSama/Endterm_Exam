package com.program.endtermexam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class courses extends AppCompatActivity {
    private int numberOfCardCourse = 3;
    private Bundle bundle_current;
    private Toolbar actionbar_pacefy;
    private TextView textView_current_activityTitle;
    private LinearLayout layout_cardsParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        InitializeValues();

        new com.program.endtermexam.GoBack(actionbar_pacefy, this);
    }

    private void InitializeValues(){
        bundle_current = getIntent().getExtras();
        layout_cardsParent = findViewById(R.id.layout_cardsParent);
        actionbar_pacefy = findViewById(R.id.actionbar_pacefy);
        textView_current_activityTitle = actionbar_pacefy.findViewById(R.id.textView_current_activityTitle);

        String card_title = String.valueOf(bundle_current.getString("card_name"));
        String course_title = String.valueOf(bundle_current.getString("course_name"));
        String action_bar_title = String.valueOf(bundle_current.getString("action_bar_name"));

        LayoutInflater layoutInflater_cardButtons = (LayoutInflater)getSystemService(courses.LAYOUT_INFLATER_SERVICE);
        for(int i=0; i < numberOfCardCourse; i++)
            CreateCardButtons(layoutInflater_cardButtons, course_title, card_title);
        textView_current_activityTitle.setText(action_bar_title);
    }

    private void CreateCardButtons(LayoutInflater inflater, String courseName, String cardName){
        View view = inflater.inflate(R.layout.cardbutton_course, null);
        Button cardButton = (Button) view.findViewById(R.id.card_course);
        TextView courseTitle = (TextView) view.findViewById(R.id.textView_courseName);
        cardButton.setText(cardName);
        courseTitle.setText(courseName);
        layout_cardsParent.addView(view);
    }
}