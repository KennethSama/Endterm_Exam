package com.program.endtermexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class ViewCourse extends AppCompatActivity {
    private Intent next_intent;
    private int numberOfModules = new Random().nextInt(5);
    private LinearLayout linearLayout_courseModulesParent;
    private Button button_noTouchCourse;
    private Bundle bundle_current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);
        InitializeValues();
        InitializeIntents();
        ExtendedLayoutAccess.AccessAppBar(null, this, getString(R.string.app_courses));
    }
    private void InitializeIntents(){ }
    private void InitializeValues() {
        bundle_current = getIntent().getExtras();
        button_noTouchCourse = findViewById(R.id.button_noTouchCourse);

        String buttonCard_title = String.valueOf(bundle_current.getString("button_card_name"));
        String buttonCardLayout_header = String.valueOf(bundle_current.getString("button_card_layout_header"));
        String course_name = String.valueOf(bundle_current.getString("course_name"));

        button_noTouchCourse.setText(course_name);

        next_intent = bundle_current.getParcelable("next_intent");

        linearLayout_courseModulesParent = findViewById(R.id.linearLayout_courseModulesParent);

        LayoutInflater layoutInflater_cardButtons = (LayoutInflater) getSystemService(Courses.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < numberOfModules; i++)
            CreateCardButtons(layoutInflater_cardButtons, buttonCard_title, buttonCardLayout_header);
    }
    public void CreateCardButtons(LayoutInflater inflater, String buttonCard_title, String buttonCardLayout_header){
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.cardbutton_layout, null);
        TextView textView_buttonCard_header = view.findViewById(R.id.textView_buttonCard_header);
        LinearLayout linearLayout_moduleTopics = view.findViewById(R.id.linearLayout_moduleTopics);

        // TODO Add the new intent to the button_topic Extra

        int rand = new Random().nextInt(5);
        int randTopics =  rand == 0 ? 1 : rand;

        for (int n = 0; n < randTopics; n++) {
            RelativeLayout parent_button_topic = (RelativeLayout) inflater.inflate(R.layout.topic_button, null);
            Button button_topic = parent_button_topic.findViewById(R.id.button_topic);
            button_topic.setText(buttonCard_title);
            button_topic.setOnClickListener(v ->  startActivity(next_intent));
            linearLayout_moduleTopics.addView(parent_button_topic);
        }

        textView_buttonCard_header.setText(buttonCardLayout_header);
        linearLayout_courseModulesParent.addView(view);


    }
}