package com.wormos.nalandaadmin;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UserLogin extends AppCompatActivity {


    AppCompatButton loginBackBtn, loginBtn;
    EditText loginUsernameEdtTxt, loginPasswordEdtTxt;
    FirebaseAuth mAuth;
    ProgressDialog loginProgress;
    DatabaseReference adminRefs = FirebaseDatabase.getInstance().getReference("Admin");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);


        loginBackBtn = findViewById(R.id.login_back_btn);
        loginBtn = findViewById(R.id.login_btn);
        loginUsernameEdtTxt = findViewById(R.id.login_username_edtTxt);
        loginPasswordEdtTxt = findViewById(R.id.login_password_edtTxt);

        //Methodology
        loginBackBtn.setOnClickListener(view -> finish());

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

                String userEmailConverted= Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()).replaceAll("\\.","%7");
                adminRefs.child(userEmailConverted).addListenerForSingleValueEvent(new ValueEventListener() {
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
                            startActivity(new Intent(UserLogin.this, Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                            Toast.makeText(getApplicationContext(), "You are logged in!", Toast.LENGTH_SHORT).show();
                            loginProgress.dismiss();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }).addOnFailureListener(e -> {
                loginProgress.dismiss();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            });

        }
    }
}