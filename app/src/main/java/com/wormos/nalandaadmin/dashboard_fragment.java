package com.wormos.nalandaadmin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class dashboard_fragment extends Fragment {

    RecyclerView highlightRV,storyRV;
    View view;
    FirebaseRecyclerOptions<StoryModel> storyOption;
    FirebaseRecyclerOptions<HighLightModel> highLightOption;
    StoryAdapter storyAdapter;
    HighLightAdapter highLightAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard_fragment, container, false);

        //highlightSetup
        highlightRV = view.findViewById(R.id.dashboard_highlightRV);
        LinearLayoutManager highLightLinerLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true);
        highLightLinerLayoutManager.setStackFromEnd(true);
        highlightRV.setLayoutManager(highLightLinerLayoutManager);
        highLightOption  = new FirebaseRecyclerOptions.Builder<HighLightModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Highlights"), HighLightModel.class)
                .build();
        highLightAdapter = new HighLightAdapter(highLightOption);
        highlightRV.setAdapter(highLightAdapter);

        //Story Setup
        storyRV = view.findViewById(R.id.dashboard_storyRV);
        LinearLayoutManager storyLinearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true);
        storyLinearLayoutManager.setStackFromEnd(true);
        storyRV.setLayoutManager(storyLinearLayoutManager);
        storyOption  = new FirebaseRecyclerOptions.Builder<StoryModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Stories"), StoryModel.class)
                .build();
        storyAdapter = new StoryAdapter(storyOption);
        storyRV.setAdapter(storyAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        storyAdapter.startListening();
        highLightAdapter.startListening();
    }
}