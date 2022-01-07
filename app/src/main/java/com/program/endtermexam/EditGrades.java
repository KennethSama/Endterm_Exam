package com.program.endtermexam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditGrades extends AppCompatActivity {
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private ConstraintLayout progressBar_login;

    private String studentId;
    private String fullName;
    private TextInputLayout textInputLayout_Elab, textInputLayout_Engg, textInputLayout_Eval, textInputLayout_Expln, textInputLayout_Explr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_grades);
        InitializeDatabase();
        InitializeValues();
    }
    class GradesData{
        String elaborate;
        String engage;

        public String getElaborate() {
            return elaborate;
        }

        public void setElaborate(String elaborate) {
            this.elaborate = elaborate;
        }

        public String getEngage() {
            return engage;
        }

        public void setEngage(String engage) {
            this.engage = engage;
        }

        public String getEvaluate() {
            return evaluate;
        }

        public void setEvaluate(String evaluate) {
            this.evaluate = evaluate;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getExplore() {
            return explore;
        }

        public void setExplore(String explore) {
            this.explore = explore;
        }

        String evaluate;
        String explain;
        String explore;

        public GradesData(String elaborate, String engage, String evaluate, String explain, String explore) {
            this.elaborate = elaborate;
            this.engage = engage;
            this.evaluate = evaluate;
            this.explain = explain;
            this.explore = explore;
        }
    }
    private void InitializeValues(){
        Bundle bundle = getIntent().getExtras();
        TextView textView_studentName = findViewById(R.id.textView_studentName);

        progressBar_login = findViewById(R.id.progressBar_login);

        textInputLayout_Elab = findViewById(R.id.textInputLayout_Elab);
        textInputLayout_Engg = findViewById(R.id.textInputLayout_Engage);
        textInputLayout_Eval = findViewById(R.id.textInputLayout_Eval);
        textInputLayout_Expln = findViewById(R.id.textInputLayout_Explain);
        textInputLayout_Explr = findViewById(R.id.textInputLayout_Explore);

        studentId = String.valueOf(bundle.getString("student_ID"));
        fullName = String.valueOf(bundle.getString("student_fullname"));
        textView_studentName.setText(fullName);
    }

    private void InitializeDatabase(){
        rootNode = FirebaseDatabase.getInstance("https://endterm-exam-default-rtdb.firebaseio.com/");
        reference = rootNode.getReference("FinalGrades");
    }

    public void OnSubmit(View view) {
        String elab = textInputLayout_Elab.getEditText().getText().toString().trim();
        String engg = textInputLayout_Engg.getEditText().getText().toString().trim();
        String eval = textInputLayout_Eval.getEditText().getText().toString().trim();
        String expln = textInputLayout_Expln.getEditText().getText().toString().trim();
        String explr = textInputLayout_Explr.getEditText().getText().toString().trim();

        if(elab == "0" && elab.isEmpty()){
            textInputLayout_Elab.setError("Must not equal to zero or Empty");
            textInputLayout_Elab.requestFocus();
            return;
        }else
            textInputLayout_Elab.setError(null);

        if(engg == "0" && elab.isEmpty()){
            textInputLayout_Engg.setError("Must not equal to zero or Empty");
            textInputLayout_Engg.requestFocus();
            return;
        }else
            textInputLayout_Engg.setError(null);

        if(eval == "0" && elab.isEmpty()){
            textInputLayout_Eval.setError("Must not equal to zero or Empty");
            textInputLayout_Eval.requestFocus();
            return;
        }else
            textInputLayout_Eval.setError(null);


        if(expln == "0" && elab.isEmpty()){
            textInputLayout_Expln.setError("Must not equal to zero or Empty");
            textInputLayout_Expln.requestFocus();
            return;
        }else
            textInputLayout_Expln.setError(null);


        if(explr == "0" && elab.isEmpty()){
            textInputLayout_Explr.setError("Must not equal to zero or Empty");
            textInputLayout_Explr.requestFocus();
            return;
        }else
            textInputLayout_Explr.setError(null);

        progressBar_login.setVisibility(View.VISIBLE);
        GradesData grades = new GradesData(elab, engg, expln, explr, eval);
        reference.child(studentId).setValue(grades).addOnCompleteListener(
            task -> {
                if (task.isSuccessful()){
                    Toast.makeText(this, "Grades has been posted", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    progressBar_login.setVisibility(View.GONE);
                    Toast.makeText(this, "Error! Something went wrong.", Toast.LENGTH_LONG).show();
                }
            }
        );
    }

    public void OnReset(View view) {
    }
}