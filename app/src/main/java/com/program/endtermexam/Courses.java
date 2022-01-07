package com.program.endtermexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
    private Intent intent_viewTopic, intent_viewAttend;
    private Intent intent_viewQuiz, intent_takeQuiz;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private TextInputLayout textInputLayout_QuizNum, textInputLayout_QuizNumItems;

    private RelativeLayout relativeLayout_modal;
    private ConstraintLayout progressBar_layout;

    private HashMap<String, String> userDetails;
    private HashMap<String, String> gradesDetails;
    private HashMap<String, String> quizDetails;

    private CurrentUser currentUser;

    private FloatingActionButton floatingActionButton;
    private RelativeLayout modal_addAttendance, modal_addQuiz, modal_currentAction;
    private MaterialButton materialButton_add, materialButton_cancel;

    private int quizCount;
    private String action_bar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        ExtendedLayoutAccess.AccessAppBar(null, this, getResources().getString(R.string.app_courses));
        InitializeIntents();
        InitializeValues();
        InitiaizeData();
        InitializeContents();
    }
    @Override
    protected void onStart() {
        super.onStart();
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
    }

    private void InitializeIntents(){
        intent_viewTopic = new Intent(Courses.this, ViewTopic.class);
        intent_viewQuiz = new Intent(Courses.this, ViewQuiz.class);
        intent_takeQuiz = new Intent(Courses.this, TakeQuiz.class);
        intent_viewAttend = new Intent(Courses.this, ViewAttendance.class);
    }

    private void InitializeContents(){
        userDetails = currentUser.GetUserIndividualSession();
        quizDetails = currentUser.GetAllQuizData();

        quizCount = quizDetails.size();

        Log.d("quizDetails", Integer.toString(quizCount));

        LayoutInflater layoutInflater_cardButtons = (LayoutInflater)getSystemService(Courses.LAYOUT_INFLATER_SERVICE);

        String course_title = "";
        InitializePreferences();

        if (layout_cardsParent.getChildCount() <= 0 )
            layout_cardsParent.removeAllViews();

        if (layout_cardsParent.getChildCount() < quizCount) {
            if (action_bar_title.equals(getResources().getString(R.string.app_quizzes)))
                for (Map.Entry<String, String> entry : quizDetails.entrySet())
                    CreateCardButtons(layoutInflater_cardButtons, "Prelim", entry.getKey(), entry.getValue());
            else if (action_bar_title.equals(getResources().getString(R.string.app_attend))){
                int z = 0;
                for (Map.Entry<String, String> entry : quizDetails.entrySet()){
                    CreateCardButtons(layoutInflater_cardButtons, "Prelim", "Day ".concat(Integer.toString(z + 1)), "");
                    z++;
                }
            }
            else {
                SearchAllStudents(layoutInflater_cardButtons);
                CreateContent();
            }
        }


        String fullname = userDetails.get("firstName").concat(" " + userDetails.get("middleName").charAt(0) + ". " + userDetails.get("lastName"));

        ExtendedLayoutAccess.AccessNavBar(null, getString(R.string.app_courses), fullname, userDetails.get("email"), userDetails.get("location"), userDetails.get("academicProgram"), userDetails.get("type"));

        if (userDetails.get("type").equals(getResources().getString(R.string.type_stud)) || action_bar_title.equals(getResources().getString(R.string.app_grades)))
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
    public void CreateContent(){
        new Handler().postDelayed(() -> {
            if (progressBar_layout.getVisibility() == View.GONE)
                progressBar_layout.setVisibility(View.VISIBLE);
            Log.d("dataChange_studentsCount_zi", Integer.toString(zi));
            if (zi >= studentCount){
                canGetStudentData = true;
                Log.d("allStudentsID", allStudentsID.toString());
            }
            Log.d("canGetStudentData",Boolean.toString(canGetStudentData));
            if(canGetStudentData){
                canGetStudentData = false;
                Log.d("canGetStudentData",Boolean.toString(canGetStudentData));
//                for (int g = 0; g < allStudentsID.size(); g++) {
//                    String studenFName = studentGradeData.get(allStudentsID.get(g)).get("firstName");
//                    String studenMName = Character.toString(studentGradeData.get(allStudentsID.get(g)).get("middleName").charAt(0));
//                    String studenLName = studentGradeData.get(allStudentsID.get(g)).get("lastName");
//                    Log.d("allStudentsID_content", Integer.toString(allStudentsID.size()));
//                    CreateCardButtons(inflater, "Student ".concat(Integer.toString(g + 1)), studenLName + ", " + studenFName + studenMName, "");
//                }
                progressBar_layout.setVisibility(View.GONE);
            }
            else
                CreateContent();
        }, 200);
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


        action_bar_title = String.valueOf(bundle_current.getString("action_bar_name"));

        if (action_bar_title.equals(getResources().getString(R.string.app_quizzes)))
            modal_currentAction = modal_addQuiz;
        else
            modal_currentAction = modal_addAttendance;

        current_intent = bundle_current.getParcelable("next_intent");

        textView_current_activityTitle.setText(action_bar_title);

        InitializeDatabase();
    }

    private void InitializeDatabase(){
        rootNode = FirebaseDatabase.getInstance("https://endterm-exam-default-rtdb.firebaseio.com/");
        reference = rootNode.getReference("Quizzes");
    }

    private void CreateCardButtons(LayoutInflater inflater, String courseName, String cardName, String values){
        View view = inflater.inflate(R.layout.cardbutton_course, null);
        Button cardButton = (Button) view.findViewById(R.id.card_course);
        TextView courseTitle = (TextView) view.findViewById(R.id.textView_courseName);
        cardButton.setText(cardName);
        courseTitle.setText(courseName);
        cardButton.setOnClickListener(v -> {
            if (cardName.contains("Day "))
                TestAttendance();
            else
                NextActivity(cardName);
        });
        layout_cardsParent.addView(view);
    }
    private void CreateStudentCardButtons(LayoutInflater inflater, String courseName, String cardName, String studentID){
        View view = inflater.inflate(R.layout.cardbutton_course, null);
        Button cardButton = (Button) view.findViewById(R.id.card_course);
        TextView courseTitle = (TextView) view.findViewById(R.id.textView_courseName);
        cardButton.setText(cardName);
        courseTitle.setText(courseName);
        cardButton.setOnClickListener(v -> { EditGrades(studentID, cardName); });
        layout_cardsParent.addView(view);
    }

    public void EditGrades(String studentID, String fullname){
        Intent intent_editGrades = new Intent(this, EditGrades.class);
        intent_editGrades.putExtra("student_ID",studentID);
        intent_editGrades.putExtra("student_fullname", fullname);
        startActivity(intent_editGrades);
    }
    public void NextActivity(String quizID){
        next_intent = intent_viewQuiz;
        next_intent.putExtra("quiz_ID", quizID);
        next_intent.putExtra("user_type",userDetails.get("type"));
        next_intent.putExtra("user_ID", currentUser.getUserID());
        Log.d("quizID", quizID);
        Log.d("user_ID", currentUser.getUserID());
        startActivity(next_intent);
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
                        startActivity(intent);
                    }else
                        Toast.makeText(Courses.this, "Error! Something went wrong. ", Toast.LENGTH_SHORT).show();
                    progressBar_layout.setVisibility(View.GONE);
                }
        );
    }

    int studentCount = 0;
    public ArrayList<String> allStudentsID;
    public boolean canShowAllStudents = false;
    private SharedPreferences session_studentsGrades;
    private SharedPreferences.Editor editor_studentsGrades;
    public void InitializePreferences(){
        session_studentsGrades = getApplicationContext().getSharedPreferences("StudentGradesSession", Context.MODE_PRIVATE);
        editor_studentsGrades = session_studentsGrades.edit();
    }

    HashMap<String, HashMap<String, String>> studentGradeData = new HashMap<>();
    int studentsCount = 0;
    boolean canGetStudentData = false;
    int zi = 0;
    public void SearchAllStudents(LayoutInflater inflater){
        allStudentsID = new ArrayList<>();
        allStudentsID.clear();
        studentGradeData.clear();

        reference = rootNode.getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    String key = data.getKey();
                    String values = data.getValue().toString();
                    if(values.contains("type=Student")) {
                        Log.d("Student Found", "Student Found");
                        allStudentsID.add(key);
                        Log.d("Student Found", Integer.toString(allStudentsID.size()));
                        studentCount++;
                        Query query = reference.child(key);
                        query.addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    HashMap<String, String> userData = new HashMap<>();
                                    int userDataCount = (int) snapshot.getChildrenCount();
                                    int g = 0;
                                    for (DataSnapshot data: snapshot.getChildren()){
                                        String innerKey = data.getKey();
                                        String innerValue = data.getValue().toString();
                                        userData.put(innerKey, innerValue);
                                        Log.d("dataChange_outerKey", key);
                                        Log.d("dataChange_outerValues", values);
                                        Log.d("dataChange_innerKey", innerKey);
                                        Log.d("dataChange_innerValue", innerValue);
                                        g++;
                                    }
                                    if (g>=userDataCount){
                                        studentGradeData.put(key, userData);
                                        String studenFName = studentGradeData.get(allStudentsID.get(zi)).get("firstName");
                                        String studenMName = Character.toString(studentGradeData.get(allStudentsID.get(zi)).get("middleName").charAt(0));
                                        String studenLName = studentGradeData.get(allStudentsID.get(zi)).get("lastName");
                                        String cardCategory = "Student ".concat(Integer.toString(zi + 1));
                                        Log.d("allStudentsID_content", Integer.toString(allStudentsID.size()));
                                        CreateStudentCardButtons(inflater, studenLName, studenLName + ", " + studenFName + studenMName, key);
                                        Log.d("dataChange_studentsCount_g", Integer.toString(g));
                                        Log.d("dataChange_studentsCount_zi", Integer.toString(zi));
                                        Log.d("dataChange_studentCount_", Integer.toString(studentCount));
                                        Log.d("dataChange_studentGradeData", studentGradeData.toString());
                                        zi++;

                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(Courses.this, "Error! Something went wrong. ", Toast.LENGTH_SHORT).show();

                                }
                            }
                        );
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Courses.this, "Error! Something went wrong. ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void Gettudents(){
//        for
    }

    private void CancelAction(TextInputLayout textInputLayout1, TextInputLayout textInputLayout2){
        modal_currentAction.setVisibility(View.GONE);
        textInputLayout1.getEditText().getText().clear();
        textInputLayout2.getEditText().getText().clear();
    }

    public void TestAttendance(){
        Intent intent_attendanceTest = new Intent(Courses.this, ViewAttendance.class);
        intent_attendanceTest.putExtra("user_type", userDetails.get("type"));
        startActivity(intent_attendanceTest);
    }
}
















