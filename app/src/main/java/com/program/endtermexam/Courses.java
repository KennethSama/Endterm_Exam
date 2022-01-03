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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;


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

    private HashMap<String, String> userDetails;
    private CurrentUser currentUser;

    private FloatingActionButton floatingActionButton;
    private RelativeLayout modal_addAttendance;
    private MaterialButton materialButton_add, materialButton_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        ExtendedLayoutAccess.AccessAppBar(null, this, getResources().getString(R.string.app_courses));
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
    private void InitializeContents(){
        userDetails = currentUser.GetUserIndividualSession();
        String fullname = userDetails.get("firstName").concat(" " + userDetails.get("middleName").charAt(0) + ". " + userDetails.get("lastName"));

        ExtendedLayoutAccess.AccessNavBar(null, getString(R.string.app_dash)
                , fullname, userDetails.get("email"), userDetails.get("location"), userDetails.get("academicProgram"), userDetails.get("type"));

        if (userDetails.get("type").equals(getResources().getString(R.string.type_stud)))
            floatingActionButton.setVisibility(View.GONE);
        else {
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setOnClickListener(v-> modal_addAttendance.setVisibility(View.VISIBLE));
            materialButton_add.setOnClickListener(v-> AddAttendance());
            materialButton_cancel.setOnClickListener(v-> modal_addAttendance.setVisibility(View.GONE));
        }
    }
    private void InitiaizeData(){
        currentUser = new CurrentUser();
        currentUser.InitializeUserData(false);
        currentUser.InitializeUserID();
        currentUser.InitializePreferences(this);
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
        floatingActionButton = findViewById(R.id.floatingActionButton);
        textView_current_activityTitle = actionbar_pacefy.findViewById(R.id.textView_current_activityTitle);

        relativeLayout_modal = findViewById(R.id.modal_message);
        modal_addAttendance = findViewById(R.id.modal_addAttendance);
        materialButton_add = findViewById(R.id.materialButton_add);
        materialButton_cancel = findViewById(R.id.materialButton_cancel);

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
    private void AddAttendance(){
        modal_addAttendance.setVisibility(View.GONE);
    }
}
















