package com.wormos.nalandaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserVerification extends AppCompatActivity {

    AppCompatButton verificationBackBtn;
    TextView noOfNewStudentTv;
    RecyclerView verificationRV;
    UserVerificationAdapter userVerificationAdapter;
    FirebaseRecyclerOptions<UserVerificationModel> options;
    DatabaseReference newRegRef = FirebaseDatabase.getInstance().getReference("New Registration").child("Chanakaya");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verification);

        verificationBackBtn = findViewById(R.id.verification_user_back_btn);
        verificationBackBtn.setOnClickListener(view -> finish());

        //no if new Registration
        noOfNewStudentTv = findViewById(R.id.verification_user_text);
        newRegRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long newRegCount = snapshot.getChildrenCount();
                noOfNewStudentTv.setText(String.format("Total no of Student - %1$s",newRegCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Verification Setup
        verificationRV = findViewById(R.id.verificationUserRV);
        verificationRV.setLayoutManager(new LinearLayoutManager(this));
        options = new FirebaseRecyclerOptions.Builder<UserVerificationModel>()
                .setQuery(newRegRef, UserVerificationModel.class)
                .build();
        userVerificationAdapter = new UserVerificationAdapter(options);
        verificationRV.setAdapter(userVerificationAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        userVerificationAdapter.startListening();
    }

    public static String sharingType(int seater){
        switch (seater){
            case 1:
                return "Single Sharing";
            case 2:
                return "Twin Sharing";
            case 3:
                return "Triple Sharing";
            default:
                return "Four Sharing";
        }
    }
}