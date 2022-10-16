package com.wormos.nalandaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.wormos.nalandaapp.UserLogin;
import com.wormos.nalandaapp.UserRegistration;

public class ExploreNalanda extends AppCompatActivity {
    AppCompatButton loginBtn,exploreBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_nalanda);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(ExploreNalanda.this, Dashboard.class));
        }

        loginBtn= findViewById(R.id.explore_login_btn);
        exploreBtn = findViewById(R.id.explore_registration_btn);

        //Methodology
        exploreBtn.setOnClickListener(view -> startActivity(new Intent(this, UserRegistration.class)));
        loginBtn.setOnClickListener(view -> startActivity(new Intent(this, UserLogin.class)));
    }
}