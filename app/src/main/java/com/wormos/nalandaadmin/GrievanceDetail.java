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
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class GrievanceDetail extends AppCompatActivity {

    TextView grievanceId,grievanceName,grievanceSubject,grievanceDescription;
    AppCompatButton grievanceDetailBackBtn,grievanceDetailForwardBtn;
    String grievanceMailStr,grievanceSubjectStr,grievanceDescriptionStr,grievanceStatusStr,grievanceRelationStr,grievanceDateStr;
    DatabaseReference grievanceRef;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievance_detail);

        SharedPreferences adminHostel = getApplicationContext().getSharedPreferences("adminHostel", Context.MODE_PRIVATE);
        String hostelName = adminHostel.getString("hostelName"," ");
        String adminName = adminHostel.getString("adminType"," ");

        grievanceDetailBackBtn = findViewById(R.id.grievanceDetail_back_btn);
        grievanceDetailBackBtn.setOnClickListener(view -> finish());
        grievanceDetailForwardBtn = findViewById(R.id.grievance_detail_forward_ahead_btn);
        grievanceId = findViewById(R.id.grievanceDetailId);
        grievanceName = findViewById(R.id.grievanceDetailName);
        grievanceSubject = findViewById(R.id.grievanceDetailSubject);
        grievanceDescription = findViewById(R.id.grievanceDetailDescription);

        String grievanceDetailKey = getIntent().getStringExtra("GrievanceKey");

    //Super Admin and Admin  part functionality implementation
    if(adminName.equals("superadmin")){
        grievanceRef = FirebaseDatabase.getInstance().getReference("Grievance").child(hostelName).child(grievanceDetailKey);
        grievanceDetailForwardBtn.setVisibility(View.GONE);
    } else {
        grievanceRef = FirebaseDatabase.getInstance().getReference("Grievance Forwarded").child(grievanceDetailKey);
    }

        grievanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    grievanceMailStr = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    grievanceSubjectStr = Objects.requireNonNull(snapshot.child("subject").getValue()).toString();
                    grievanceDescriptionStr = Objects.requireNonNull(snapshot.child("description").getValue()).toString();
                    grievanceStatusStr = Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                    grievanceRelationStr = Objects.requireNonNull(snapshot.child("relation").getValue()).toString();
                    grievanceDateStr = Objects.requireNonNull(snapshot.child("date").getValue()).toString();

                    if(grievanceStatusStr.equals("Forwarded")){
                        grievanceDetailForwardBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0F9F1D")));
                        grievanceDetailForwardBtn.setText("Forwarded");
                        grievanceDetailForwardBtn.setFocusableInTouchMode(false);
                    }

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        grievanceDetailForwardBtn.setOnClickListener(view -> {
            grievanceDetailForwardBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0F9F1D")));
            grievanceDetailForwardBtn.setText("Forwarded");
            grievanceDetailForwardBtn.setFocusableInTouchMode(false);
            HashMap<String,Object> grievanceUpdateMap = new HashMap<>();
            grievanceUpdateMap.put("status","Forwarded");
            HashMap<String,Object> grievanceFrowardMap = new HashMap<>();
            grievanceFrowardMap.put("date",grievanceDateStr);
            grievanceFrowardMap.put("email",grievanceMailStr);
            grievanceFrowardMap.put("relation",grievanceRelationStr);
            grievanceFrowardMap.put("status","Forwarded");
            grievanceFrowardMap.put("subject",grievanceSubjectStr);
            grievanceFrowardMap.put("description",grievanceDescriptionStr);
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
            dbRef.child("Grievance Forwarded").child(grievanceDetailKey).updateChildren(grievanceFrowardMap);
            dbRef.child("Grievance").child(hostelName).child(grievanceDetailKey).updateChildren(grievanceUpdateMap);
            dbRef.child("Grievance by Issue").child(hostelName).child(grievanceRelationStr).child(grievanceDetailKey).updateChildren(grievanceUpdateMap);
            dbRef.child("Grievance by User").child(grievanceMailStr).child(grievanceDetailKey).updateChildren(grievanceUpdateMap);

        });

    }
}