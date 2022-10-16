package com.wormos.nalandaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import de.hdodenhof.circleimageview.CircleImageView;
import io.ak1.BubbleTabBar;

public class Dashboard extends AppCompatActivity {

    BubbleTabBar bottomNavBar;
    CardView dashboardTransportCv, dashboardLaundryCv, dashboardHouseKeepingCv, dashboardRepairCv;
    CircleImageView dashboardProfilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //initialization
        bottomNavBar = findViewById(R.id.bottom_nav_Bar);
        dashboardTransportCv = findViewById(R.id.dashboard_transport_cv);
        dashboardLaundryCv = findViewById(R.id.dashboard_laundry_cv);
        dashboardHouseKeepingCv = findViewById(R.id.dashboard_house_keeping_cv);
        dashboardRepairCv = findViewById(R.id.dashboard_repair_cv);
        dashboardProfilePhoto = findViewById(R.id.dashboard_profile_photo);

        //methodology
        FragmentTransaction dashboardTrans = getSupportFragmentManager().beginTransaction();
        dashboardTrans.replace(R.id.dashboard_fragment_holder, new dashboard_fragment());
        dashboardTrans.commit();

        dashboardTransportCv.setOnClickListener(view -> {
            selectSubMenu(dashboardTransportCv);
        });

        dashboardLaundryCv.setOnClickListener(view -> {
            selectSubMenu(dashboardLaundryCv);
        });

        dashboardHouseKeepingCv.setOnClickListener(view -> {
            selectSubMenu(dashboardHouseKeepingCv);
        });

        dashboardRepairCv.setOnClickListener(view -> {
            selectSubMenu(dashboardRepairCv);
        });

        bottomNavBar.addBubbleListener(i -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Log.d("uhj", "onBubbleClick: " + i);
            switch (i) {
                case 2131296485:
                    transaction.replace(R.id.dashboard_fragment_holder, new food_fragment());
                    break;
                case 2131296672:
                    transaction.replace(R.id.dashboard_fragment_holder, new my_room_fragment());
                    break;
                case 2131296407:
                    transaction.replace(R.id.dashboard_fragment_holder, new dashboard_fragment());
                    break;
                case 2131296463:
                    transaction.replace(R.id.dashboard_fragment_holder, new explore_fragment());
                    break;
                case 2131296650:
                    transaction.replace(R.id.dashboard_fragment_holder, new refer_fragment());
                    break;
            }
            transaction.commit();
        });

        dashboardProfilePhoto.setOnClickListener(view -> startActivity(new Intent(Dashboard.this, StudentProfile.class)));

    }

    private void selectSubMenu(CardView selectedSubMenuCv) {
        greyOutAllSubMenuCv();
        selectedSubMenuCv.setCardBackgroundColor(getResources().getColor(R.color.nalanda_blue));
    }


    private void greyOutAllSubMenuCv() {
        dashboardTransportCv.setCardBackgroundColor(getResources().getColor(R.color.ligh_grey));
        dashboardLaundryCv.setCardBackgroundColor(getResources().getColor(R.color.ligh_grey));
        dashboardHouseKeepingCv.setCardBackgroundColor(getResources().getColor(R.color.ligh_grey));
        dashboardRepairCv.setCardBackgroundColor(getResources().getColor(R.color.ligh_grey));
    }
}
