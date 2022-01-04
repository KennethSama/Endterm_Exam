package com.program.endtermexam;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AttendanceAdapter extends ArrayAdapter<StudentInfo> {
    private ArrayList<StudentInfo> studentList;


    public AttendanceAdapter(@NonNull Context context, int resource, ArrayList<StudentInfo> studentList) {
        super(context, resource, studentList);
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int phraseIndex = position;
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,
                    parent, false);
        }

        TextView studentName = convertView.findViewById(R.id.label_student_name);
        TextView studentEmail = convertView.findViewById(R.id.label_student_email);

        studentName.setText(studentList.get(position).getName());
        studentEmail.setText(studentList.get(position).getEmail());

        return convertView;
    }


}
