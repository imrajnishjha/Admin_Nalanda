package com.wormos.nalandaadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class UserVerification extends AppCompatActivity {

    AppCompatButton verificationBackBtn;
    RecyclerView verificationRV;
    UserVerificationAdapter userVerificationAdapter;
    FirebaseRecyclerOptions<UserVerificationModel> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verification);

        verificationBackBtn = findViewById(R.id.verification_user_back_btn);
        verificationBackBtn.setOnClickListener(view -> finish());

        //Verification Setup
        verificationRV = findViewById(R.id.verificationUserRV);
        verificationRV.setLayoutManager(new LinearLayoutManager(this));
        options = new FirebaseRecyclerOptions.Builder<UserVerificationModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Hostel").child("Chanakaya"), UserVerificationModel.class)
                .build();
        userVerificationAdapter = new UserVerificationAdapter(options);
        verificationRV.setAdapter(userVerificationAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        userVerificationAdapter.startListening();
    }
}