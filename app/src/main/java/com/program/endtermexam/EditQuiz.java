package com.program.endtermexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class EditQuiz extends AppCompatActivity {
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private LinearLayout linearLayout_questions;
    private TextView textView_quizNum;

    private HashMap<String, String> userDetails;
    private CurrentUser currentUser;

    private HashMap<String, HashMap<String, String>> quizMap;
    private ArrayList<String> itemKey;
    private int questionCount;

    private ConstraintLayout progressBar_layout;
    private String quizId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quiz);

        ExtendedLayoutAccess.AccessAppBar(null, this, getResources().getString(R.string.app_quizzes));
        InitializeDatabase();
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

        ExtendedLayoutAccess.AccessNavBar(null, getString(R.string.app_quizzes)
                , fullname, userDetails.get("email"), userDetails.get("location"), userDetails.get("academicProgram"), userDetails.get("type"));
    }
    private void InitiaizeData(){
        currentUser = new CurrentUser();
        currentUser.InitializeUserData(false);
        currentUser.InitializeUserID();
        currentUser.InitializePreferences(this);
    }
    private void InitializeValues(){
        Bundle bundle = getIntent().getExtras();
        int quizNumber = Integer.parseInt(bundle.getString("quiz_number"));
        int numberOfItems = Integer.parseInt(bundle.getString("number_of_items"));
        quizId = String.valueOf(bundle.get("quiz_Id"));

        Log.d("numberOfItems String", getIntent().getExtras().getString("quiz_number"));
        Log.d("numberOfItems Int", Integer.toString(numberOfItems));

        quizMap = new HashMap<>();
        itemKey = new ArrayList<>();

        linearLayout_questions = findViewById(R.id.linearLayout_questions);
        textView_quizNum = findViewById(R.id.textView_quizNum);

        progressBar_layout = findViewById(R.id.progressBar_layout);


        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Courses.LAYOUT_INFLATER_SERVICE);
        textView_quizNum.setText(String.format("Quiz %d", quizNumber));

        for (int c = 0; c< numberOfItems; c++)
            CreateQuestions(layoutInflater, "Item ".concat(Integer.toString(c + 1)));

        questionCount = linearLayout_questions.getChildCount();
    }
    private void InitializeDatabase(){
        rootNode = FirebaseDatabase.getInstance("https://endterm-exam-default-rtdb.firebaseio.com/");
        reference = rootNode.getReference("Quizzes");
    }
    private void CreateQuestions(LayoutInflater inflater, String itemNo){
        View view = inflater.inflate(R.layout.edit_filndblanks_quest, null);
        TextView textView_itemNum = (TextView) view.findViewById(R.id.itemNumber);
        textView_itemNum.setText(itemNo);
        itemKey.add(itemNo);
        linearLayout_questions.addView(view);
    }

    public void OnPost(View view) {
        for (int c = 0; c < questionCount; c++) {
            EditText question = (EditText) linearLayout_questions.getChildAt(c).findViewById(R.id.editText_fillNDblanks_question);
            TextInputLayout answer = (TextInputLayout) linearLayout_questions.getChildAt(c).findViewById(R.id.textInputLayout_fillNDblanks_Answer);
            String quest = question.getText().toString().trim();
            String ans = answer.getEditText().getText().toString().trim().toUpperCase();
            if (quest.isEmpty()){
                question.requestFocus();
                question.setError("Question is Empty");
                return;
            }
            else if (ans.isEmpty()){
                answer.getEditText().requestFocus();
                answer.getEditText().setError("Answer is Empty");
                return;
            }
            else {
                quizMap.put(itemKey.get(c),
                        new HashMap<String, String>() {{
                            put("answer", ans);
                            put("question", quest);
                            put("score", "1");
                        }}
                );
            }
        }
        Log.d("quizMap", quizMap.toString());
        progressBar_layout.setVisibility(View.VISIBLE);
        reference.child(quizId).setValue(quizMap).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(this, "Quiz Posted to Every Student!\nSubscribe to Premium Account to edit the Viewers and Takers", Toast.LENGTH_LONG).show();
                        quizMap.clear();
                        finish();
                    }else {
                        progressBar_layout.setVisibility(View.GONE);
                        Toast.makeText(EditQuiz.this, "Error! Something went wrong.", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}