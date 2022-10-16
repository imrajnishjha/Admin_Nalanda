package com.wormos.nalandaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class StudentProfile extends AppCompatActivity {

    //declaring views and variables
    ImageView studentProfilePictureIv;
    TextView studentProfileNameTv, studentProfileRoomNoTv, studentProfileRoomTypeTv;
    FirebaseDatabase database;
    DatabaseReference studentDatabaseReference;
    FirebaseAuth mAuth;
    String studentEmailConverted;
    ProgressDialog loadingProfileProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        studentProfileNameTv = findViewById(R.id.student_profile_name_tv);
        studentProfileRoomNoTv = findViewById(R.id.student_profile_room_no_tv);
        studentProfileRoomTypeTv = findViewById(R.id.student_profile_room_type_tv);

        //get Firebase Database and Authentication reference
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Log.d("studentEmailConverted", mAuth.getCurrentUser().getEmail().replaceAll("\\.", "%7"));
        studentEmailConverted = Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()).replaceAll("\\.", "%7");
        studentDatabaseReference = database.getReference("Students/" + studentEmailConverted);

        updateProfileWithFirebaseData();
    }

    private void updateProfileWithFirebaseData() {
        loadingProfileProgressDialog = new ProgressDialog(StudentProfile.this);
        loadingProfileProgressDialog.setMessage("Loading your profile...");
        loadingProfileProgressDialog.show();
        
        studentDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //getting data in strings
                String studentName = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                String studentRoomNo = Objects.requireNonNull(snapshot.child("room_no").getValue()).toString();
                String studentRoomType = Objects.requireNonNull(snapshot.child("room_type").getValue()).toString();

                //setting string data in text views
                studentProfileNameTv.setText(studentName);
                studentProfileRoomNoTv.setText(studentRoomNo);
                studentProfileRoomTypeTv.setText(studentRoomType);
                loadingProfileProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentProfile.this, "Failed to load your profile :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}