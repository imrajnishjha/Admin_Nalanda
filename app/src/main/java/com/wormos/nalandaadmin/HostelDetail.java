package com.wormos.nalandaadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class HostelDetail extends AppCompatActivity {

    AppCompatButton hostelDetailBackBtn,hostelDetailAddHostelBtn;
    RecyclerView hostelRv;
    HostelDetailAdapter hostelDetailAdapter;
    FirebaseRecyclerOptions<HostelDetailModel> options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_detail);

        hostelDetailBackBtn = findViewById(R.id.hostel_detail_back_btn);
        hostelDetailAddHostelBtn = findViewById(R.id.hostel_detail_add_btn);
        hostelRv = findViewById(R.id.hostelDetailRV);

        hostelRv.setLayoutManager(new LinearLayoutManager(this));
        options = new  FirebaseRecyclerOptions.Builder<HostelDetailModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Hostel Data"), HostelDetailModel.class)
                .build();
        hostelDetailAdapter = new HostelDetailAdapter(options);
        hostelRv.setAdapter(hostelDetailAdapter);



    //Navigation's
        hostelDetailBackBtn.setOnClickListener(view -> finish());
        hostelDetailAddHostelBtn.setOnClickListener(view -> startActivity(new Intent(this,HostelAdd.class)
                .putExtra("edit","0")));
    }

    @Override
    protected void onStart() {
        super.onStart();
        hostelDetailAdapter.startListening();
    }
}