package com.wormos.nalandaadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class UserAttendance extends AppCompatActivity {

    AppCompatButton attendanceSubmitBtn,attendanceBackBtn;
    RecyclerView attendanceRV;
    FirebaseRecyclerOptions<StudentHostelModel> options;
    AttendanceAdapter attendanceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_attendance);

        attendanceBackBtn= findViewById(R.id.attendance_back_btn);
        attendanceSubmitBtn = findViewById(R.id.attendance_submit_btn);
        attendanceBackBtn.setOnClickListener(view -> finish());

        //Attendance setup
        attendanceRV = findViewById(R.id.attendanceRV);
        attendanceRV.setLayoutManager(new LinearLayoutManager(this));
        options = new FirebaseRecyclerOptions.Builder<StudentHostelModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Hostel").child("Chanakaya"), StudentHostelModel.class)
                .build();
        attendanceAdapter = new AttendanceAdapter(options);
        attendanceRV.setAdapter(attendanceAdapter);

        //attendance submission
        //attendanceSubmitBtn = findViewById(R.id.attendance_submit_btn);
        //attendanceSubmitBtn.setOnClickListener(view ->{} );

    }

    @Override
    protected void onStart() {
        super.onStart();
        attendanceAdapter.startListening();
    }
}