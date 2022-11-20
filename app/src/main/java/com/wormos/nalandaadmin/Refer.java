package com.wormos.nalandaadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Refer extends AppCompatActivity {

    AppCompatButton referBackBtn;
    RecyclerView referRv;
    FirebaseRecyclerOptions<ReferModel> options;
    ReferAdapter referAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);

        referBackBtn = findViewById(R.id.refer_user_back_btn);
        referBackBtn.setOnClickListener(view -> finish());

        referRv = findViewById(R.id.referUserRV);
        referRv.setLayoutManager(new LinearLayoutManager(this));
        options = new FirebaseRecyclerOptions.Builder<ReferModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Refer"), ReferModel.class)
                .build();
        referAdapter = new ReferAdapter(options);
        referRv.setAdapter(referAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        referAdapter.startListening();
    }
}