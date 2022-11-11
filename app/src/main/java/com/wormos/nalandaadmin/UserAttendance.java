package com.wormos.nalandaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class UserAttendance extends AppCompatActivity {

    AppCompatButton attendanceSubmitBtn,attendanceBackBtn;
    RecyclerView attendanceRV;
    FirebaseRecyclerOptions<StudentHostelModel> options;
    AttendanceAdapter attendanceAdapter;
    TextView attendanceTakenTV;
    RelativeLayout progressBar;
    DatabaseReference attendanceRef= FirebaseDatabase.getInstance().getReference("Attendance");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_attendance);

        attendanceBackBtn= findViewById(R.id.attendance_back_btn);
        attendanceSubmitBtn = findViewById(R.id.attendance_submit_btn);
        progressBar = findViewById(R.id.attendance_loading_progressBarRL);
        attendanceRV = findViewById(R.id.attendanceRV);
        attendanceTakenTV = findViewById(R.id.AttendanceTakenText);
        attendanceSubmitBtn = findViewById(R.id.attendance_submit_btn);
        attendanceBackBtn.setOnClickListener(view -> finish());

        //Attendance setup
        attendanceRef.child(todaysDateFormatter("YYYY-MM-dd")).child("taken").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    progressBar.setVisibility(View.GONE);
                    attendanceTakenTV.setVisibility(View.VISIBLE);
                    attendanceSubmitBtn.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    attendanceRV.setVisibility(View.VISIBLE);
                    attendanceRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    options = new FirebaseRecyclerOptions.Builder<StudentHostelModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference("Hostel").child("Chanakaya"), StudentHostelModel.class)
                            .build();
                    attendanceAdapter = new AttendanceAdapter(options);
                    attendanceRV.setAdapter(attendanceAdapter);
                    attendanceAdapter.startListening();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //attendance submission

        attendanceSubmitBtn.setOnClickListener(view ->{
            progressBar.setVisibility(View.VISIBLE);
            DatabaseReference hostelRef = FirebaseDatabase.getInstance().getReference("Hostel");
            hostelRef.child("Chanakaya").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Long studentCount = snapshot.getChildrenCount();
                    attendanceRef.child(todaysDateFormatter("YYYY-MM-dd")).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot attendanceSnapshot) {
                            Long attendanceCount = attendanceSnapshot.getChildrenCount();
                            if(studentCount.equals(attendanceCount)){
                                progressBar.setVisibility(View.GONE);
                                HashMap<String,Object> takenMap = new HashMap<>();
                                takenMap.put("taken","true");
                                attendanceRef.child(todaysDateFormatter("YYYY-MM-dd")).updateChildren(takenMap).addOnSuccessListener(success->{
                                    attendanceRV.setVisibility(View.GONE);
                                    attendanceTakenTV.setVisibility(View.VISIBLE);
                                    attendanceSubmitBtn.setVisibility(View.GONE);
                                }).addOnFailureListener(failure ->{
                                    Toast.makeText(UserAttendance.this, "Please try again", Toast.LENGTH_SHORT).show();
                                });
                            } else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(UserAttendance.this, "Please mark all student", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } );

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public static String todaysDateFormatter(String format){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        return df.format(date);
    }
}