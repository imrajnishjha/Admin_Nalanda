package com.wormos.nalandaadmin;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class transport_fragment extends Fragment {

    View fragmentView;
    AppCompatButton transportFragmentBusBtn, transportFragmentCabBtn;
    RecyclerView transportFragmentRv;
    FirebaseRecyclerOptions<TransportScheduleModel> options;
    TransportScheduleAdapter transportScheduleAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_transport_fragment, container, false);
        transportFragmentBusBtn = fragmentView.findViewById(R.id.transport_fragment_bus_btn);
        transportFragmentCabBtn = fragmentView.findViewById(R.id.transport_fragment_cab_btn);
        transportFragmentRv = fragmentView.findViewById(R.id.transport_fragment_rv);

        //set cab btn as grey and bus btn as blue during onCreate
        transportFragmentCabBtn.setBackground(ContextCompat.getDrawable(fragmentView.getContext(),R.drawable.greyed_rounded_btn));

        //Layout Manager for Recycler View
        transportFragmentRv.setLayoutManager(new LinearLayoutManager(getContext()));

        //Firebase Recycler option for bus schedule
        options = new FirebaseRecyclerOptions.Builder<TransportScheduleModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Transport Schedule").child("Bus Schedule"), TransportScheduleModel.class)
                .build();

        //setting up adapter for recycler view
        transportScheduleAdapter = new TransportScheduleAdapter(options,"Bus Schedule");
        transportFragmentRv.setAdapter(transportScheduleAdapter);
        transportScheduleAdapter.startListening();

        transportFragmentBusBtn.setOnClickListener(view -> setBusOption());
        transportFragmentCabBtn.setOnClickListener(view -> setCabOption());

        return fragmentView;
    }

    private void setCabOption() {
        transportFragmentBusBtn.setBackgroundDrawable(ContextCompat.getDrawable(fragmentView.getContext(),R.drawable.greyed_rounded_btn));
        transportFragmentCabBtn.setBackgroundDrawable(ContextCompat.getDrawable(fragmentView.getContext(),R.drawable.light_blue_rounded_btn));

        //Firebase Recycler option for bus schedule
        options = new FirebaseRecyclerOptions.Builder<TransportScheduleModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Transport Schedule").child("Cab Schedule"), TransportScheduleModel.class)
                .build();

        //setting up adapter for recycler view
        transportScheduleAdapter = new TransportScheduleAdapter(options,"Cab Schedule");
        transportFragmentRv.setAdapter(transportScheduleAdapter);
        transportScheduleAdapter.startListening();
    }

    private void setBusOption() {
        transportFragmentCabBtn.setBackgroundDrawable(ContextCompat.getDrawable(fragmentView.getContext(),R.drawable.greyed_rounded_btn));
        transportFragmentBusBtn.setBackgroundDrawable(ContextCompat.getDrawable(fragmentView.getContext(),R.drawable.light_blue_rounded_btn));

        //Firebase Recycler option for bus schedule
        options = new FirebaseRecyclerOptions.Builder<TransportScheduleModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Transport Schedule").child("Bus Schedule"), TransportScheduleModel.class)
                .build();

        //setting up adapter for recycler view
        transportScheduleAdapter = new TransportScheduleAdapter(options,"Bus Schedule");
        transportFragmentRv.setAdapter(transportScheduleAdapter);
        transportScheduleAdapter.startListening();
    }
}