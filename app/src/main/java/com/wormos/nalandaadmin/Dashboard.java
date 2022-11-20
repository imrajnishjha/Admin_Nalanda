package com.wormos.nalandaadmin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import de.hdodenhof.circleimageview.CircleImageView;
import io.ak1.BubbleTabBar;

public class Dashboard extends AppCompatActivity {

    BubbleTabBar bottomNavBar;
    CircleImageView dashboardProfilePhoto;

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

        dashboardProfilePhoto.setOnClickListener(view -> startActivity(new Intent(Dashboard.this, StudentProfile.class)));

    }


}
