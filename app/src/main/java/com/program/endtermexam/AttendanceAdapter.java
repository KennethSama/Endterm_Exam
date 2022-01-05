package com.program.endtermexam;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    ArrayList<StudentInfo> studentList;
    Context context;

    public AttendanceAdapter(Context context, ArrayList<StudentInfo> studentList){
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        holder.name.setText(studentList.get(position).getName());
        holder.email.setText(studentList.get(position).getEmail());
        holder.status.setText(studentList.get(position).getStatus());

        holder.attendanceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditStudentStatus.class);
                intent.putExtra("name", studentList.get(holder.getAdapterPosition()).getName());
                intent.putExtra("userID", studentList.get(holder.getAdapterPosition()).getUserID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {

        TextView name, email, status;
        ConstraintLayout attendanceLayout;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.label_student_name);
            email = itemView.findViewById(R.id.label_student_email);
            status = itemView.findViewById(R.id.label_student_status);
            attendanceLayout = itemView.findViewById(R.id.attendance_layout);
        }
    }
}
