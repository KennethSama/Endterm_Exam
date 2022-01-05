package com.program.endtermexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditStudentStatus extends AppCompatActivity {

    private TextView name;
    private RadioGroup studentStatus;
    private RadioButton selectedStudentStatus;
    private String studentName, userID;

    private DatabaseReference reference;
    private ViewAttendance viewAttendance = new ViewAttendance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_status);

        reference = FirebaseDatabase.getInstance().getReference("Users");

        studentStatus = findViewById(R.id.student_attendance);
        name = findViewById(R.id.focused_student_name);

        getData();
        setData();
    }

    private void getData(){
        if(getIntent().hasExtra("name")) {
            studentName = getIntent().getStringExtra("name");
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }

        if(getIntent().hasExtra("userID")){
            userID = getIntent().getStringExtra("userID");
        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(){
        name.setText(studentName);
    }

    public void OnEnterStatusPressed(View view){
        int selectedStatus = studentStatus.getCheckedRadioButtonId();
        selectedStudentStatus = studentStatus.findViewById(selectedStatus);

        reference.child("User_06AgBlWXZDdKVfn7E16YYBLUVl12").child("status").setValue(selectedStudentStatus.getText().toString());
        Toast.makeText(this, "Successfully changed student's attendance status", Toast.LENGTH_SHORT).show();
       // viewAttendance.AddStudents();
    }
}