package com.program.endtermexam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Courses extends AppCompatActivity {
    private int numberOfCardCourse = 3;
    private Bundle bundle_current;
    private Toolbar actionbar_pacefy;
    private TextView textView_current_activityTitle;
    private LinearLayout layout_cardsParent;
    private Intent current_intent;
    private Intent next_intent;
    private Intent intent_viewTopic;
    private Intent intent_viewQuiz;
    private Intent intent_viewAttend;
    private Intent intent_viewGrades;

    private RelativeLayout relativeLayout_modal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        InitializeIntents();
        InitializeValues();

        ExtendedLayoutAccess.AccessAppBar(null, this, null);
    }
    private void InitializeIntents(){
        intent_viewTopic = new Intent(Courses.this, ViewTopic.class);
        intent_viewQuiz = new Intent(Courses.this, ViewQuiz.class);
        intent_viewAttend = new Intent(Courses.this, ViewAttendance.class);

    }
    private void InitializeValues(){
        bundle_current = getIntent().getExtras();

        layout_cardsParent = findViewById(R.id.layout_cardsParent);
        actionbar_pacefy = findViewById(R.id.actionbar_pacefy);
        textView_current_activityTitle = actionbar_pacefy.findViewById(R.id.textView_current_activityTitle);

        relativeLayout_modal = findViewById(R.id.modal_message);

        ExtendedLayoutAccess.InitializeModal(relativeLayout_modal);

        String card_title = String.valueOf(bundle_current.getString("card_name"));
        String course_title = String.valueOf(bundle_current.getString("course_name"));
        String action_bar_title = String.valueOf(bundle_current.getString("action_bar_name"));

        current_intent = bundle_current.getParcelable("next_intent");
        LayoutInflater layoutInflater_cardButtons = (LayoutInflater)getSystemService(Courses.LAYOUT_INFLATER_SERVICE);

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
        cardButton.setOnClickListener(v -> NextActivity((Button) v, courseName));
        layout_cardsParent.addView(view);
    }

    public void NextActivity(Button button, String course_name){
        String buttonCard_title = null;
        String buttonCardLayout_header = null;
        String[] buttonTexts = new String[]{
                getString(R.string.hint_modulenums),
                getString(R.string.hint_quizzesnums),
                getString(R.string.hint_attendPerc),
                getString(R.string.hint_gradesnums),
        };

        String text = button.getText().toString();
        if (buttonTexts[0].equals(text)) {
            buttonCard_title = getString(R.string.hint_topic);
            buttonCardLayout_header = getString(R.string.hint_module);
            next_intent = intent_viewTopic;
        } else if (buttonTexts[1].equals(text)) {
            buttonCard_title = getString(R.string.hint_quiz);
            buttonCardLayout_header = getString(R.string.hint_module);
            next_intent = intent_viewQuiz;
        } else if (buttonTexts[2].equals(text)) {
            buttonCard_title = getString(R.string.hint_day);
            buttonCardLayout_header = getString(R.string.hint_week);
            next_intent = intent_viewAttend;
        } else if (buttonTexts[3].equals(text)) {
            buttonCard_title = "";
            buttonCardLayout_header = "";
            next_intent = intent_viewGrades;
        }
        current_intent.putExtra("button_card_name", buttonCard_title);
        current_intent.putExtra("button_card_layout_header", buttonCardLayout_header);
        current_intent.putExtra("course_name", course_name);
        current_intent.putExtra("next_intent", next_intent);


        startActivity(current_intent);
    }
}
















