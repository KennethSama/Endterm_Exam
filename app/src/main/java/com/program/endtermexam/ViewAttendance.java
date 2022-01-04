package com.program.endtermexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    private ListView listView;
    private RadioGroup studentRadioGroup;
    private RadioButton studentRadioButtonType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
        ExtendedLayoutAccess.AccessAppBar(null, this, getString(R.string.app_attend));

        listView = findViewById(R.id.listView_attendance);
        studentRadioGroup = findViewById(R.id.student_attendance);
        AddStudents();
    }

    private void AddStudents(){
        ArrayList<StudentInfo> studentList = new ArrayList<>();
        final Integer[] values = new Integer[]{1,2,1,2};

        AttendanceAdapter adapter = new AttendanceAdapter(getApplicationContext(), R.layout.list_item, studentList);
        listView.setAdapter(adapter);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    StudentInfoFirebase info = snapshot.getValue(StudentInfoFirebase.class);
                    String name = info.getFirstName() + " " + info.getLastName();
                    String email = info.getEmail();
                    StudentInfo student = new StudentInfo(name, email);
                    studentList.add(student);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void OnAttendanceStatusPressed(View view){

        int selectedType = studentRadioGroup.getCheckedRadioButtonId();
        studentRadioButtonType = studentRadioGroup.findViewById(selectedType);
        String type = studentRadioButtonType.getText().toString();

        Toast toast = Toast.makeText(getApplicationContext(), type, Toast.LENGTH_SHORT);
        toast.show();
    }
}