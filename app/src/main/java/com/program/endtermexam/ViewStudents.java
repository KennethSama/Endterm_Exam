package com.program.endtermexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import javax.net.ssl.SSLEngineResult;

public class ViewStudents extends AppCompatActivity {
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private String studentID;

    private TextView textView_ElabValue, textView_EnggValue, textView_EvalValue, textView_ExplnValue, textView_ExplrValue;
    private String ElabValue, EnggValue, EvalValue, ExplnValue, ExplrValue;
    private HashMap<String, String> grades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);
        InitializeDatabase();
        InitializeValues();
        SearchData();
        CreateContent();
    }
    private void InitializeDatabase(){
        rootNode = FirebaseDatabase.getInstance("https://endterm-exam-default-rtdb.firebaseio.com/");
        reference = rootNode.getReference("FinalGrades");
        Bundle bundle = getIntent().getExtras();
        if(getIntent().hasExtra("user_id"))
            studentID = bundle.getString("user_id");
        else
            studentID = "";
    }
    private void InitializeValues(){
        textView_ElabValue = findViewById(R.id.textView_ElabValue);
        textView_EnggValue = findViewById(R.id.textView_EnggValue);
        textView_EvalValue = findViewById(R.id.textView_EvalValue);
        textView_ExplnValue = findViewById(R.id.textView_ExplnValue);
        textView_ExplrValue = findViewById(R.id.textView_ExplrValue);

        grades = new HashMap<>();
    }
    boolean canUpdateTextViews = false;
    private void SearchData(){
        Query query = reference.child(studentID);
        query.addValueEventListener(
            new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        int childCount = (int) snapshot.getChildrenCount();
                        int gi = 0;
                        for (DataSnapshot data: snapshot.getChildren()) {
                            grades.put(data.getKey(), data.getValue().toString());
                            Log.d("ViewStudents", grades.toString());
                            gi++;
                        }
                        if(gi >= childCount){
                            canUpdateTextViews = true;
                        }
                    }else{
                        ElabValue = "Unavailable";
                        EnggValue = "Unavailable";
                        EvalValue = "Unavailable";
                        ExplnValue = "Unavailable";
                        ExplrValue = "Unavailable";
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }
        );
    }
    public void CreateContent(){
        new Handler().postDelayed(() -> {
            if(canUpdateTextViews){
                canUpdateTextViews = false;

                ElabValue = grades.get("elaborate");
                EnggValue = grades.get("engage");
                EvalValue = grades.get("evaluate");
                ExplnValue = grades.get("explain");
                ExplrValue = grades.get("explore");

                textView_ElabValue.setText(ElabValue);
                textView_EnggValue.setText(EnggValue);
                textView_EvalValue.setText(EvalValue);
                textView_ExplnValue.setText(ExplnValue);
                textView_ExplrValue.setText(ExplrValue);
            }else
                CreateContent();
        }, 100);
    }
}