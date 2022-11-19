package com.wormos.nalandaadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventManagement extends AppCompatActivity {

    RecyclerView eventRv;
    CircleImageView addEventBtn;
    EventManagementAdapter eventManagementAdapter;
    FirebaseRecyclerOptions<EventManagementModel> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_management);

        addEventBtn= findViewById(R.id.newEventIcon);
        addEventBtn.setOnClickListener(view -> startActivity(new Intent(this,EventDetail.class).putExtra("edit","0")));

    //Event RecyclerView Setup
        eventRv=findViewById(R.id.scheduleEventRecycleView);
        LinearLayoutManager eventLinerLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,true);
        eventLinerLayoutManager.setStackFromEnd(true);
        eventRv.setLayoutManager(eventLinerLayoutManager);

        options = new  FirebaseRecyclerOptions.Builder<EventManagementModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Events"), EventManagementModel.class)
                .build();
        eventManagementAdapter = new EventManagementAdapter(options);
        eventRv.setAdapter(eventManagementAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        eventManagementAdapter.startListening();
    }
}