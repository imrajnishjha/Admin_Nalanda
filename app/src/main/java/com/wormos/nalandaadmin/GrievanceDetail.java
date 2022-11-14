package com.wormos.nalandaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class GrievanceDetail extends AppCompatActivity {

    TextView grievanceId,grievanceName,grievanceSubject,grievanceDescription;
    AppCompatButton grievanceDetailBackBtn,grievanceDetailForwardBtn;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievance_detail);

        SharedPreferences adminHostel = getApplicationContext().getSharedPreferences("adminHostel", Context.MODE_PRIVATE);
        String hostelName = adminHostel.getString("hostelName"," ");

        grievanceDetailBackBtn = findViewById(R.id.grievanceDetail_back_btn);
        grievanceDetailBackBtn.setOnClickListener(view -> finish());
        grievanceDetailForwardBtn = findViewById(R.id.grievance_detail_forward_ahead_btn);
        grievanceId = findViewById(R.id.grievanceDetailId);
        grievanceName = findViewById(R.id.grievanceDetailName);
        grievanceSubject = findViewById(R.id.grievanceDetailSubject);
        grievanceDescription = findViewById(R.id.grievanceDetailDescription);

        String grievanceDetailKey = getIntent().getStringExtra("GrievanceKey");

        DatabaseReference grievanceRef = FirebaseDatabase.getInstance().getReference("Grievance").child(hostelName).child(grievanceDetailKey);

        grievanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String grievanceMailStr = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                String grievanceSubjectStr = Objects.requireNonNull(snapshot.child("subject").getValue()).toString();
                String grievanceDescriptionStr = Objects.requireNonNull(snapshot.child("description").getValue()).toString();

                DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("Students").child(grievanceMailStr);
                studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String studentNameStr = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                        String studentIdStr = Objects.requireNonNull(snapshot.child("id").getValue()).toString();
                        grievanceName.setText(studentNameStr);
                        grievanceId.setText(studentIdStr);
                        grievanceSubject.setText(grievanceSubjectStr);
                        grievanceDescription.setText(grievanceDescriptionStr);

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

        grievanceDetailForwardBtn.setOnClickListener(view -> {
            grievanceDetailForwardBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0F9F1D")));
            grievanceDetailForwardBtn.setText("Forwarded");
        });

    }
}