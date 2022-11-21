package com.wormos.nalandaadmin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.ak1.BubbleTabBar;

public class Dashboard extends AppCompatActivity {

    BubbleTabBar bottomNavBar;
    CircleImageView dashboardProfilePhoto;
    static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Admin");
    static StorageReference storageRef = FirebaseStorage.getInstance().getReference("Profile Picture/" + Objects.requireNonNull(mAuth.getCurrentUser()).getEmail() + "ProfilePicture");


    //don't use this converted mail in this activity it may cause some bug, you can use it in some other activity it'll work fine.
    static String userEmailConverted= Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()).replaceAll("\\.","%7");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        SharedPreferences adminHostel = getApplicationContext().getSharedPreferences("adminHostel", Context.MODE_PRIVATE);
        String adminName = adminHostel.getString("adminType"," ");

        //initialization
        bottomNavBar = findViewById(R.id.bottom_nav_Bar);
        dashboardProfilePhoto = findViewById(R.id.dashboard_profile_photo);

        //methodology
        FragmentTransaction dashboardTrans = getSupportFragmentManager().beginTransaction();
        dashboardTrans.replace(R.id.dashboard_fragment_holder, new dashboard_fragment());
        dashboardTrans.commit();

        //getting Profile Picture
        String email = Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()).replaceAll("\\.","%7");
        userRef.child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ProfilePurl = Objects.requireNonNull(snapshot.child("purl").getValue()).toString();
                Glide.with(getApplicationContext())
                        .load(ProfilePurl)
                        .error(R.drawable.defaultprofile2)
                        .into(dashboardProfilePhoto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        bottomNavBar.addBubbleListener(i -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Log.d("uhj", "onBubbleClick: " + i);
            switch (i) {
                case 2131362093:
                    if(adminName.equals("superadmin")){
                        transaction.replace(R.id.dashboard_fragment_holder, new FoodMenuFragment());
                    } else {
                        transaction.replace(R.id.dashboard_fragment_holder, new food_fragment());
                    }
                    break;
                case 2131362329:
                    transaction.replace(R.id.dashboard_fragment_holder, new transport_fragment());
                    break;
                case 2131361994:
                    transaction.replace(R.id.dashboard_fragment_holder, new dashboard_fragment());
                    break;
                case 2131362056:
                    transaction.replace(R.id.dashboard_fragment_holder, new grievance_fragment());
                    break;
                case 2131362314:
                    transaction.replace(R.id.dashboard_fragment_holder, new notification_fragment());
                    break;
            }
            transaction.commit();
        });

        dashboardProfilePhoto.setOnClickListener(view -> startActivity(new Intent(Dashboard.this, AdminProfile.class)));

    }

    //Uploading Image to FirebaseStorage and Update the corresponding RealtimeDB

    public static void uploadImageToFirebase(Uri ImageUri, StorageReference storageRef, DatabaseReference rdbRef, ImageView imageView, ProgressBar progressBar){
        progressBar.setVisibility(View.VISIBLE);
        storageRef.putFile(ImageUri).addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            HashMap<String,Object> userMap = new HashMap<>();
            userMap.put("purl",uri.toString());
            rdbRef.updateChildren(userMap).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    imageView.setImageURI(ImageUri);
                } else {
                    Toast.makeText(imageView.getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            });
        }));


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
