package com.wormos.nalandaadmin;

import static com.wormos.nalandaadmin.Dashboard.storageRef;
import static com.wormos.nalandaadmin.Dashboard.userEmailConverted;
import static com.wormos.nalandaadmin.Dashboard.userRef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AdminProfile extends AppCompatActivity {

    //declaring views and variables
    ImageView adminProfilePictureIv;
    TextView adminProfileNameTv, adminProfileHostelTv, adminProfileAdminTypeTv;
    FirebaseDatabase database;
    DatabaseReference studentDatabaseReference;
    FirebaseAuth mAuth;
    String adminEmailConverted;
    AppCompatButton logoutBtn;
    RelativeLayout loadingProfileProgressDialog;
    Uri profileUri;
    ProgressBar profilePhotoUpdateProgress;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        adminProfileNameTv = findViewById(R.id.student_profile_name_tv);
        adminProfileHostelTv = findViewById(R.id.student_profile_room_no_tv);
        adminProfileAdminTypeTv = findViewById(R.id.student_profile_room_type_tv);
        loadingProfileProgressDialog = findViewById(R.id.user_profile_progressBarRL);
        adminProfilePictureIv = findViewById(R.id.student_profile_picture_iv);
        profilePhotoUpdateProgress = findViewById(R.id.user_profile_photo_progressBar);
        logoutBtn = findViewById(R.id.profile_logout_btn);

        //get Firebase Database and Authentication reference
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        adminEmailConverted = Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()).replaceAll("\\.", "%7");
        studentDatabaseReference = database.getReference("Admin/" + adminEmailConverted);

        updateProfileWithFirebaseData(studentDatabaseReference);

        //logging out user
        logoutBtn.setOnClickListener(view -> {
            mAuth.signOut();
            Toast.makeText(getApplicationContext(), "You are logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), ExploreNalanda.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        });

        //Changing Profile Picture
        adminProfilePictureIv.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            LayoutInflater pickImglayoutInflater = getLayoutInflater();
            View pickImgview = pickImglayoutInflater.inflate(R.layout.image_picker_item, null);
            builder.setCancelable(true);
            builder.setView(pickImgview);
            AlertDialog alertDialogImg = builder.create();
            Window window = alertDialogImg.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            alertDialogImg.show();
            window.setAttributes(wlp);

            CardView cameraCardView = pickImgview.findViewById(R.id.chooseCamera);
            CardView galleryCardView = pickImgview.findViewById(R.id.chooseGallery);

            galleryCardView.setOnClickListener(view1 -> {
                ImagePicker.with(this)
                        .galleryOnly()
                        .crop(1f, 1f)
                        .maxResultSize(720, 1080)
                        .start(0);
                alertDialogImg.dismiss();
            });
            cameraCardView.setOnClickListener(view1 -> {
                ImagePicker.with(this)
                        .cameraOnly()
                        .crop(1f, 1f)
                        .maxResultSize(720, 1080)
                        .start(1);
                alertDialogImg.dismiss();
            });
        });
    }

    //set data of user from firebase
    private void updateProfileWithFirebaseData(DatabaseReference studentDatabaseReference) {
        loadingProfileProgressDialog.setVisibility(View.VISIBLE);

        studentDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //getting data in strings
                String studentName = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                String studentHostel = Objects.requireNonNull(snapshot.child("hostel").getValue()).toString();
                String studentAdminType = Objects.requireNonNull(snapshot.child("adminType").getValue()).toString();
                String purl = Objects.requireNonNull(snapshot.child("purl").getValue()).toString();

                //setting string data in text views
                adminProfileNameTv.setText(studentName);
                adminProfileHostelTv.setText(studentHostel);
                adminProfileAdminTypeTv.setText(studentAdminType);
                Glide.with(getApplicationContext())
                        .load(purl)
                        .error(R.drawable.defaultprofile2)
                        .into(adminProfilePictureIv);
                loadingProfileProgressDialog.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to load your profile :(", Toast.LENGTH_SHORT).show();
                loadingProfileProgressDialog.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            profileUri = data.getData();
            Dashboard.uploadImageToFirebase(profileUri, storageRef, userRef.child(userEmailConverted), adminProfilePictureIv, profilePhotoUpdateProgress);
        } else if (resultCode == RESULT_OK && requestCode == 1) {
            profileUri = data.getData();
            Dashboard.uploadImageToFirebase(profileUri, storageRef, userRef.child(userEmailConverted), adminProfilePictureIv, profilePhotoUpdateProgress);
        }
    }
}