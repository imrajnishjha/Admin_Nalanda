package com.wormos.nalandaadmin;

import static com.wormos.nalandaadmin.UserAttendance.todaysDateFormatter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ExploreNalanda extends AppCompatActivity {
    AppCompatButton loginBtn;
    DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference("Admin");
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    String userEmailConverted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_nalanda);



        if(mAuth.getCurrentUser() != null) {
            userEmailConverted= Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()).replaceAll("\\.","%7");;
            adminRef.child(userEmailConverted).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String hostelName= Objects.requireNonNull(snapshot.child("hostel").getValue()).toString();
                        String adminType = Objects.requireNonNull(snapshot.child("adminType").getValue()).toString();
                        SharedPreferences adminHostel = getApplicationContext().getSharedPreferences("adminHostel", Context.MODE_PRIVATE);
                        SharedPreferences.Editor adminHostelEditor = adminHostel.edit();
                        adminHostelEditor.putString("hostelName",hostelName);
                        adminHostelEditor.putString("adminType",adminType);
                        adminHostelEditor.apply();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            startActivity(new Intent(ExploreNalanda.this, Dashboard.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }

        loginBtn= findViewById(R.id.explore_login_btn);


        //Methodology

        loginBtn.setOnClickListener(view -> startActivity(new Intent(this, UserLogin.class)));
    }
}