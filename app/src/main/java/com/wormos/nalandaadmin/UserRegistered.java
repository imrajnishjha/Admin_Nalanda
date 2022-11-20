package com.wormos.nalandaadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegistered extends AppCompatActivity {

    AppCompatButton registeredUserBackBtn;
    RecyclerView registeredUserRV;
    UserRegisteredAdapter userRegisteredAdapter;
    FirebaseRecyclerOptions<UserRegisteredModel> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registered);

        SharedPreferences adminHostel = getApplicationContext().getSharedPreferences("adminHostel", Context.MODE_PRIVATE);
        String hostelName = adminHostel.getString("hostelName"," ");
        String adminName = adminHostel.getString("adminType"," ");

        registeredUserBackBtn = findViewById(R.id.registered_user_back_btn);
        registeredUserBackBtn.setOnClickListener(view -> finish());

        //User Setup
        registeredUserRV = findViewById(R.id.registeredUserRV);
        registeredUserRV.setLayoutManager(new LinearLayoutManager(this));

        if(adminName.equals("superadmin")){
            options = new FirebaseRecyclerOptions.Builder<UserRegisteredModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("Students"), UserRegisteredModel.class)
                    .build();
            userRegisteredAdapter = new UserRegisteredAdapter(options,"yes");
        } else {
            options = new FirebaseRecyclerOptions.Builder<UserRegisteredModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("Hostel").child(hostelName), UserRegisteredModel.class)
                    .build();
            userRegisteredAdapter = new UserRegisteredAdapter(options,"no");
        }
        registeredUserRV.setAdapter(userRegisteredAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        userRegisteredAdapter.startListening();
    }
}