package com.program.endtermexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


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

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private TextInputLayout textInputLayout_QuizNum, textInputLayout_QuizNumItems;

    private RelativeLayout relativeLayout_modal;
    private ConstraintLayout progressBar_layout;

    private HashMap<String, String> userDetails;
    private HashMap<String, ?> quizDetails;

    private CurrentUser currentUser;

    private FloatingActionButton floatingActionButton;
    private RelativeLayout modal_addAttendance, modal_addQuiz, modal_currentAction;
    private MaterialButton materialButton_add, materialButton_cancel;

    private int quizCount;

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
    private void InitiaizeData(){
        currentUser = new CurrentUser();
        currentUser.InitializeUserData(false);
        currentUser.InitializeUserID();
        currentUser.InitializePreferences(this);

        currentUser.InitializeQuizData();
        currentUser.InitializeQuizPreferences(this);
//        currentUser.SetAllQuizData(this);
    }
    private void InitializeIntents(){
        intent_viewTopic = new Intent(Courses.this, ViewTopic.class);
        intent_viewQuiz = new Intent(Courses.this, ViewQuiz.class);
        intent_viewAttend = new Intent(Courses.this, ViewAttendance.class);
    }

    private void InitializeContents(){
        userDetails = currentUser.GetUserIndividualSession();
        quizDetails = currentUser.GetAllQuizData();
        Log.d("quizDetails", currentUser.GetAllQuizData().toString());
        Log.d("quizDetails Class", currentUser.GetAllQuizData().getClass().toString());
        Log.d("quizDetails", quizDetails.toString());
        Log.d("quizDetails", quizDetails.getClass().toString());

        Log.d("quizDetails", quizDetails.values().toString());
        Log.d("quizDetails", (quizDetails.values()).getClass().toString());

        quizCount = quizDetails.size();

        Log.d("quizDetails", Integer.toString(quizCount));

        LayoutInflater layoutInflater_cardButtons = (LayoutInflater)getSystemService(Courses.LAYOUT_INFLATER_SERVICE);

        String course_title = "";


        for (Map.Entry <String, ?> entry: quizDetails.entrySet())
            CreateCardButtons(layoutInflater_cardButtons, "",entry.getKey());

        String fullname = userDetails.get("firstName").concat(" " + userDetails.get("middleName").charAt(0) + ". " + userDetails.get("lastName"));

        ExtendedLayoutAccess.AccessNavBar(null, getString(R.string.app_courses)
                , fullname, userDetails.get("email"), userDetails.get("location"), userDetails.get("academicProgram"), userDetails.get("type"));

        if (userDetails.get("type").equals(getResources().getString(R.string.type_stud)))
            floatingActionButton.setVisibility(View.GONE);
        else {
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setOnClickListener(v-> modal_currentAction.setVisibility(View.VISIBLE));
            materialButton_add.setOnClickListener(v-> {
                        if (modal_currentAction.equals(modal_addAttendance))
                            AddAttendance();
                        else
                            AddQuiz();
                    }
            );

            materialButton_cancel.setOnClickListener(v-> CancelAction(textInputLayout_QuizNum, textInputLayout_QuizNumItems));
        }
    }

    private void InitializeValues(){
        bundle_current = getIntent().getExtras();

        layout_cardsParent = findViewById(R.id.layout_cardsParent);
        actionbar_pacefy = findViewById(R.id.actionbar_pacefy);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        textView_current_activityTitle = actionbar_pacefy.findViewById(R.id.textView_current_activityTitle);

        relativeLayout_modal = findViewById(R.id.modal_message);
        progressBar_layout = findViewById(R.id.progressBar_layout);

        modal_addAttendance = findViewById(R.id.modal_addAttendance);
        modal_addQuiz = findViewById(R.id.modal_addQuiz);

        materialButton_add = modal_addQuiz.findViewById(R.id.materialButton_add);
        materialButton_cancel = modal_addQuiz.findViewById(R.id.materialButton_cancel);
        textInputLayout_QuizNum = findViewById(R.id.textInputLayout_QuizNum);
        textInputLayout_QuizNumItems = findViewById(R.id.textInputLayout_QuizNumItems);

        ExtendedLayoutAccess.InitializeModal(relativeLayout_modal);


        String action_bar_title = String.valueOf(bundle_current.getString("action_bar_name"));

        if (action_bar_title.equals(getResources().getString(R.string.app_quizzes)))
            modal_currentAction = modal_addQuiz;
        else
            modal_currentAction = modal_addAttendance;

        current_intent = bundle_current.getParcelable("next_intent");

        // TODO FIXED HERE/ CHECK COURSE.java when reading quizdata
//        int quizCount = quizDetails.size();
//        Log.d("quizDetails", quizDetails.toString());


        textView_current_activityTitle.setText(action_bar_title);

        InitializeDatabase();
    }

    private void InitializeDatabase(){
        rootNode = FirebaseDatabase.getInstance("https://endterm-exam-default-rtdb.firebaseio.com/");
        reference = rootNode.getReference("Quizzes");
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
        }
        if (buttonTexts[1].equals(text)) {
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
    private void AddQuiz(){
        String quizNumber = textInputLayout_QuizNum.getEditText().getText().toString();
        String numberOfItems = textInputLayout_QuizNumItems.getEditText().getText().toString();

        UserHelper userQuiz = new UserHelper();
        userQuiz.CreateQuiz(" ", " ");

        if(quizNumber == "0" || quizNumber.isEmpty()){
            textInputLayout_QuizNum.requestFocus();
            textInputLayout_QuizNum.setError("Quiz Number is Empty or Zero");
            return;
        }else
            textInputLayout_QuizNum.setError(null);
        if(numberOfItems == "0" || numberOfItems.isEmpty()){
            textInputLayout_QuizNumItems.requestFocus();
            textInputLayout_QuizNumItems.setError("Number of items is Empty or Zero");
            return;
        }else
            textInputLayout_QuizNumItems.setError(null);

        progressBar_layout.setVisibility(View.VISIBLE);
        String quizId = "Quiz_".concat(quizNumber);
        reference.child(quizId).setValue(userQuiz).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(Courses.this, "Quiz Added", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent( Courses.this, EditQuiz.class);
                        intent.putExtra("quiz_number", quizNumber);
                        intent.putExtra("number_of_items", numberOfItems);
                        intent.putExtra("quiz_Id", quizId);
                        startActivity(intent);
                    }else{
                        Toast.makeText(Courses.this, "Error! Something went wrong. ", Toast.LENGTH_SHORT).show();
                    }
                    progressBar_layout.setVisibility(View.GONE);
                }
        );
    }
    private void CancelAction(TextInputLayout textInputLayout1, TextInputLayout textInputLayout2){
        modal_currentAction.setVisibility(View.GONE);
        textInputLayout1.getEditText().getText().clear();
        textInputLayout2.getEditText().getText().clear();
    }
}
















