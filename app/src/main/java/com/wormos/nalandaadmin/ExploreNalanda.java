package com.wormos.nalandaadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class ExploreNalanda extends AppCompatActivity {
    AppCompatButton loginBtn;
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


        //Methodology

        loginBtn.setOnClickListener(view -> startActivity(new Intent(this, UserLogin.class)));
    }
}