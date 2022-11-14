package com.wormos.nalandaadmin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class grievance_fragment extends Fragment {

    View view;
    RecyclerView grievanceRV;
    FirebaseRecyclerOptions<GrievanceModel> options;
    GrievanceAdapter grievanceAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        SharedPreferences adminHostel = requireContext().getSharedPreferences("adminHostel", Context.MODE_PRIVATE);
        String hostelName = adminHostel.getString("hostelName"," ");

        view= inflater.inflate(R.layout.fragment_grievance_fragment, container, false);
        grievanceRV = view.findViewById(R.id.grievanceRv);
        grievanceRV.setLayoutManager(new LinearLayoutManager(requireContext()));

        options = new FirebaseRecyclerOptions.Builder<GrievanceModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Grievance").child(hostelName),GrievanceModel.class).build();
        grievanceAdapter = new GrievanceAdapter(options);
        grievanceRV.setAdapter(grievanceAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        grievanceAdapter.startListening();
    }
}