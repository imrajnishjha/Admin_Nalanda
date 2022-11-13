package com.wormos.nalandaadmin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class food_fragment extends Fragment {

    View view;
    TextView lunchCount;
    RecyclerView lunchRv;
    MotionLayout addLunchToggleBtn;
    FirebaseRecyclerOptions<StudentHostelModel> options;
    FoodAdapter foodAdapter;
    DatabaseReference lunchDataRef = FirebaseDatabase.getInstance().getReference("Lunch");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_food_fragment, container, false);

        lunchCount = view.findViewById(R.id.food_lunch_count);
        addLunchToggleBtn = view.findViewById(R.id.food_open_lunch_motion_animation);


    //Checking the initial condition of toggle Button
        lunchDataRef.child("Chanakaya").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String date = Objects.requireNonNull(snapshot.child("date").getValue()).toString();
                    String lunchAvailability = Objects.requireNonNull(snapshot.child("Lunch Available").getValue()).toString();
                    if(date.equals(UserAttendance.todaysDateFormatter("YYYY-MM-dd"))){
                        if(lunchAvailability.equals("true")){
                            addLunchToggleBtn.setProgress(1);
                            Log.d("jiz", "onDataChange: ");
                        }
                    } else {
                        addLunchToggleBtn.setProgress(1);
                        Log.d("jiz", "onDataChange: ");
                    }
                } else {
                    HashMap<String,Object> lunchAvailableMap = new HashMap<>();
                    lunchAvailableMap.put("Lunch Available","true");
                    lunchAvailableMap.put("date",UserAttendance.todaysDateFormatter("YYYY-MM-dd"));
                    lunchDataRef.child("Chanakaya").updateChildren(lunchAvailableMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    //Lunch RecyclerView Setup
        lunchRv = view.findViewById(R.id.foodUserLunchBoxRV);
        lunchRv.setLayoutManager(new LinearLayoutManager(requireContext()));
        options = new FirebaseRecyclerOptions.Builder<StudentHostelModel>()
                .setQuery(lunchDataRef.child("Lunch Data")
                        .child(UserAttendance.todaysDateFormatter("YYYY-MM-dd"))
                        .child("Chanakaya"), StudentHostelModel.class)
                .build();
        foodAdapter = new FoodAdapter(options);
        lunchRv.setAdapter(foodAdapter);



    //Lunch Toggle Btn implementation using MotionLayout
        addLunchToggleBtn.addTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {
                Log.d("ukrf", "onTransitionChange: "+startId+" "+endId);
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                if(currentId==2131362016){
                    HashMap<String,Object> lunchAvailabilityMap =new HashMap<>();
                    lunchAvailabilityMap.put("Lunch Available","true");
                    lunchAvailabilityMap.put("date",UserAttendance.todaysDateFormatter("YYYY-MM-dd"));
                    lunchDataRef.child("Chanakaya").updateChildren(lunchAvailabilityMap);
                } else if(currentId==2131362299){
                    HashMap<String,Object> lunchAvailabilityMap =new HashMap<>();
                    lunchAvailabilityMap.put("Lunch Available","false");
                    lunchAvailabilityMap.put("date",UserAttendance.todaysDateFormatter("YYYY-MM-dd"));
                    lunchDataRef.child("Chanakaya").updateChildren(lunchAvailabilityMap);
                }
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        foodAdapter.startListening();
    }
}