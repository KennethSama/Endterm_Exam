package com.program.endtermexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAttendance extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<StudentInfo> studentList = new ArrayList<>();
    private String userType;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        ExtendedLayoutAccess.AccessAppBar(null, this, getString(R.string.app_attend));

        recyclerView = findViewById(R.id.listView_attendance);
        userType = String.valueOf(getIntent().getExtras().getString("user_type"));
        AddStudents();
    }

    public void AddStudents(){
        AttendanceAdapter attendanceAdapter = new AttendanceAdapter(this, studentList, userType);
        recyclerView.setAdapter(attendanceAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
                int position = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String userID = snapshot.getKey();
                    StudentInfoFirebase info = snapshot.getValue(StudentInfoFirebase.class);
                    if (info.getType().equals("Student")){
                        String name = info.getFirstName() + " " + info.getLastName();
                        String email = info.getEmail();
                        String status = info.getStatus();
                        StudentInfo student = new StudentInfo(name, email, status, userID);
                        studentList.add(student);
                        position++;
                        attendanceAdapter.notifyItemChanged(position);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewAttendance.this, "Ops! Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        count++;

        refresh(1000);
    }

    private void refresh(int milliseconds){
        final Handler handler = new Handler();

        final Runnable runnable = () -> AddStudents();

        handler.postDelayed(runnable, milliseconds);
    }
}