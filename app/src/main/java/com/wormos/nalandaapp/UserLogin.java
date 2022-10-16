package com.wormos.nalandaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;

public class UserLogin extends AppCompatActivity {

    TextView loginNewUserTv;
    AppCompatButton loginBackBtn, loginBtn;
    EditText loginUsernameEdtTxt, loginPasswordEdtTxt;
    FirebaseAuth mAuth;
    ProgressDialog loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        loginNewUserTv = findViewById(R.id.login_new_user_tv);
        loginBackBtn = findViewById(R.id.login_back_btn);
        loginBtn = findViewById(R.id.login_btn);
        loginUsernameEdtTxt = findViewById(R.id.login_username_edtTxt);
        loginPasswordEdtTxt = findViewById(R.id.login_password_edtTxt);

        //Methodology
        loginBackBtn.setOnClickListener(view -> finish());

        loginNewUserTv.setOnClickListener(view -> {
            startActivity(new Intent(UserLogin.this, UserRegistration.class));
            finish();
        });

        loginBtn.setOnClickListener(view -> loginUser());
    }

    //login user with email and password
    private void loginUser() {

        String userEmail = loginUsernameEdtTxt.getText().toString();
        String password = loginPasswordEdtTxt.getText().toString();

        if (userEmail.isEmpty()) {
            loginUsernameEdtTxt.setError("Enter email");
            loginUsernameEdtTxt.requestFocus();
        } else if (password.isEmpty()) {
            loginPasswordEdtTxt.setError("Enter password");
            loginPasswordEdtTxt.requestFocus();
        } else {
            loginProgress = new ProgressDialog(UserLogin.this);
            loginProgress.setMessage("Logging you in...");
            loginProgress.show();
            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(userEmail, password).addOnSuccessListener(authResult -> {
                Toast.makeText(getApplicationContext(), "You are logged in!", Toast.LENGTH_SHORT).show();
                loginProgress.dismiss();
                startActivity(new Intent(UserLogin.this, Dashboard.class));
            }).addOnFailureListener(e -> {
                loginProgress.dismiss();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            });

        }
    }
}