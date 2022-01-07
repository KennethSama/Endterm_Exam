package com.program.endtermexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewQuiz extends AppCompatActivity {
    private String quizId;
    private HashMap<String, String> userDetails;
    private CurrentUser currentUser;
    private LinearLayout linearLayout_questions;

    private HashMap<String, String> quizMap;
    private ArrayList<String> itemKey;
    private TextView textView_quizNum2;
    private String type;
    private String userID;

    private ConstraintLayout progressBar;
    private DatabaseReference databaseReference_grades;

    private Button button_submit;

    private int current_score = 0;
    private int total_score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_quiz);

        ExtendedLayoutAccess.AccessAppBar(null, this, "Quiz");
        InitiaizeData();
        InitializeValues();

    }

    @Override
    protected void onStart() {
        super.onStart();
        InitializeContents();
        CreateContent();
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

//        String fullname = userDetails.get("firstName").concat(" " + userDetails.get("middleName").charAt(0) + ". " + userDetails.get("lastName"));
//
//        ExtendedLayoutAccess.AccessNavBar(null, getString(R.string.app_quizzes)
//                , fullname, userDetails.get("email"), userDetails.get("location"), userDetails.get("academicProgram"), userDetails.get("type"));
    }
    private void InitiaizeData(){
        currentUser = new CurrentUser();
        currentUser.InitializeUserData(false);
        currentUser.InitializeUserID();
        currentUser.InitializePreferences(this);

        currentUser.InitializeQuizData();
        currentUser.InitializeQuizPreferences(this);

    }
    private void InitializeValues(){
        Bundle bundle = getIntent().getExtras();
        quizId = String.valueOf(bundle.getString("quiz_ID"));
        type = String.valueOf(bundle.getString("user_type"));
        userID = String.valueOf(bundle.getString("user_ID"));
        Log.d("user_ID", userID);

        textView_quizNum2 = findViewById(R.id.textView_quizNum2);
        linearLayout_questions = findViewById(R.id.linearLayout_questions);
        progressBar = findViewById(R.id.progressBar_login);
        button_submit = findViewById(R.id.button_submit);


        textView_quizNum2.setText(quizId.replace("_", " "));
        currentUser.SetSpecificQuizData(this, quizId);
        userDetails = currentUser.GetUserIndividualSession();
        quizMap = currentUser.GetSingleQuizData();

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://endterm-exam-default-rtdb.firebaseio.com/");

        databaseReference_grades = rootNode.getReference("Grades").child(userID);

        SearchData(quizId);
    }
    public HashMap<String, String> currentQuiz = new HashMap<>();
    boolean canUpdate = false;
    private int itemCount = 0;
    LayoutInflater layoutInflater;
    public void SearchData(String quizID){
        progressBar.setVisibility(View.VISIBLE);
        layoutInflater = (LayoutInflater) getSystemService(Courses.LAYOUT_INFLATER_SERVICE);
        DatabaseReference quiz = FirebaseDatabase.getInstance("https://endterm-exam-default-rtdb.firebaseio.com/").getReference("Quizzes").child(quizID);
        quiz.addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    itemCount = (int) dataSnapshot.getChildrenCount();
                    Log.d("itemCount", Integer.toString(itemCount));
                    for (int x = 0; x < itemCount; x++) {
                        String itemNo = "Item "+(x+1);
                        Query query = quiz.child(itemNo);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                currentQuiz.clear();
                                int count = (int) dataSnapshot.getChildrenCount();
                                Log.d("SearchDataCount", Integer.toString(count));
                                int x=0;
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String key = snapshot.getKey();
                                    String value = snapshot.getValue().toString();
                                    currentQuiz.put(key, value);
                                    x++;
                                }
                                if(x >= count) {
                                    itemCount++;
                                    CreateQuizLayout(layoutInflater, itemNo, currentQuiz.get("question"), currentQuiz.get("answer"), currentQuiz.get("score"));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                progressBar.setVisibility(View.VISIBLE);
                                Toast.makeText(ViewQuiz.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(ViewQuiz.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                }
            }
        );
        if (itemCount >= itemCount)
            canUpdate = true;
    }
    private void CreateQuizLayout(LayoutInflater inflater, String itemNo, String question, String hiddenAns, String score){
        View view = inflater.inflate(R.layout.take_filndblanks_quiz, null);
        TextView textView_itemNum = (TextView) view.findViewById(R.id.textView_fillNDblanks_itemNo);
        TextView textView_fillNDblanks_Question = (TextView) view.findViewById(R.id.textView_fillNDblanks_Question);
        TextView hidden_answer = (TextView) view.findViewById(R.id.hidden_answer);
        TextView hidden_score = (TextView) view.findViewById(R.id.hidden_score);
        textView_itemNum.setText(itemNo);
        textView_fillNDblanks_Question.setText(question);
        hidden_answer.setText(hiddenAns);
        hidden_score.setText(score);
        if (type.equals(getResources().getString(R.string.type_stud))) {
            hidden_answer.setVisibility(View.GONE);
            hidden_score.setVisibility(View.GONE);
            button_submit.setVisibility(View.VISIBLE);
        }
        linearLayout_questions.addView(view);
    }
    public void CreateContent(){
        new Handler().postDelayed(() -> {
            if(canUpdate){
                canUpdate = false;
                progressBar.setVisibility(View.GONE);
            }else
                CreateContent();
        }, 100);
    }
    public void OnSubmit(View view) {
        int child = linearLayout_questions.getChildCount();
        HashMap grade_map = new HashMap<>();
        for(int x = 0; x < child; x++){
            View view_item = linearLayout_questions.getChildAt(x);
            TextInputLayout textInputLayout_fillNDblanks_Answer = (TextInputLayout) view_item.findViewById(R.id.textInputLayout_fillNDblanks_Answer);
            TextView hidden_answer = (TextView) view_item.findViewById(R.id.hidden_answer);
            TextView hidden_score = (TextView) view_item.findViewById(R.id.hidden_score);
            String userAnswer = textInputLayout_fillNDblanks_Answer.getEditText().getText().toString().trim().toUpperCase();
            String hidden_score_value = hidden_score.getText().toString();
            if (userAnswer.equals(hidden_answer.getText().toString()))
                current_score += Integer.parseInt(hidden_score_value);
            total_score += Integer.parseInt(hidden_score_value);
        }
        String score = String.format("%s out of %s", current_score,  total_score);
        grade_map.put(quizId, score);
        databaseReference_grades.updateChildren(grade_map);
        Toast.makeText(this, "".concat(score), Toast.LENGTH_LONG).show();
        Log.d("score", score);
        Log.d("user_ID", userID);

        finish();
    }

}