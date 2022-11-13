package com.wormos.nalandaadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class FoodMenu extends AppCompatActivity {

    TextView day1,day2,day3,day4,day5,day6,day7;
    RecyclerView foodMenuRV;
    FirebaseRecyclerOptions<FoodMenuModel> options;
    FoodMenuAdapter foodMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        //initialization
        day1 = findViewById(R.id.food_day1);
        day2 = findViewById(R.id.food_day2);
        day3 = findViewById(R.id.food_day3);
        day4 = findViewById(R.id.food_day4);
        day5 = findViewById(R.id.food_day5);
        day6 = findViewById(R.id.food_day6);
        day7 = findViewById(R.id.food_day7);
        foodMenuRV = findViewById(R.id.food_menuRV);
        foodMenuRV.setLayoutManager(new LinearLayoutManager(this));

        //Methodology
        dateSetter(day1,day2,day3,day4,day5,day6,day7);

        day1.setBackgroundColor(Color.parseColor("#2D6BC8"));
        day1.setOnClickListener(v-> menuChange(day1,day2,day3,day4,day5,day6,day7));
        day2.setOnClickListener(v-> menuChange(day2,day1,day3,day4,day5,day6,day7));
        day3.setOnClickListener(v-> menuChange(day3,day2,day1,day4,day5,day6,day7));
        day4.setOnClickListener(v-> menuChange(day4,day2,day3,day1,day5,day6,day7));
        day5.setOnClickListener(v-> menuChange(day5,day2,day3,day4,day1,day6,day7));
        day6.setOnClickListener(v-> menuChange(day6,day2,day3,day4,day5,day1,day7));
        day7.setOnClickListener(v-> menuChange(day7,day2,day3,day4,day5,day6,day1));


    }

    //Different day menuChange
    public void menuChange(TextView day1,TextView day2,TextView day3,TextView day4,TextView day5,TextView day6,TextView day7){
        day1.setBackgroundColor(Color.parseColor("#2D6BC8"));
        day2.setBackgroundColor(Color.parseColor("#7CB8E7"));
        day3.setBackgroundColor(Color.parseColor("#7CB8E7"));
        day4.setBackgroundColor(Color.parseColor("#7CB8E7"));
        day5.setBackgroundColor(Color.parseColor("#7CB8E7"));
        day6.setBackgroundColor(Color.parseColor("#7CB8E7"));
        day7.setBackgroundColor(Color.parseColor("#7CB8E7"));


        FirebaseRecyclerOptions<FoodMenuModel> options2 ;
        String weekDay;
        if(day1.getText().toString().equals("Mon")){
            options2 = new FirebaseRecyclerOptions.Builder<FoodMenuModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("Food Menu").child("Monday"), FoodMenuModel.class)
                    .build();
            weekDay="Monday";
        } else if(day1.getText().toString().equals("Tue")) {
            options2 = new FirebaseRecyclerOptions.Builder<FoodMenuModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("Food Menu").child("Tuesday"), FoodMenuModel.class)
                    .build();
            weekDay="Tuesday";
        } else if(day1.getText().toString().equals("Wed")) {
            options2 = new FirebaseRecyclerOptions.Builder<FoodMenuModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("Food Menu").child("Wednesday"), FoodMenuModel.class)
                    .build();
            weekDay = "Wednesday";
        } else if(day1.getText().toString().equals("Thu")) {
            options2 = new FirebaseRecyclerOptions.Builder<FoodMenuModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("Food Menu").child("Thursday"), FoodMenuModel.class)
                    .build();
            weekDay = "Thursday";
        } else if(day1.getText().toString().equals("Fri")) {
            options2 = new FirebaseRecyclerOptions.Builder<FoodMenuModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("Food Menu").child("Friday"), FoodMenuModel.class)
                    .build();
            weekDay = "Friday";
        } else if(day1.getText().toString().equals("Sun")) {
            options2 = new FirebaseRecyclerOptions.Builder<FoodMenuModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("Food Menu").child("Sunday"), FoodMenuModel.class)
                    .build();
            weekDay = "Sunday";
        } else  {
            options2 = new FirebaseRecyclerOptions.Builder<FoodMenuModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("Food Menu").child("Saturday"), FoodMenuModel.class)
                    .build();
            weekDay = "Saturday";
        }

        FoodMenuAdapter shiftAdapter = new FoodMenuAdapter(options2,weekDay);
        foodMenuRV.setAdapter(shiftAdapter);
        shiftAdapter.startListening();
    }

    //Setting the other week days according to today's date
    public void dateSetter( TextView day1, TextView day2, TextView day3, TextView day4, TextView day5, TextView day6, TextView day7){
        Calendar calendar = Calendar.getInstance();
        String todaysDate = UserAttendance.todaysDateFormatter("dd MMM yyyy");
        final int weekNo = calendar.get(Calendar.DAY_OF_WEEK);
        String weekday = weekCalculator(weekNo);
        StringBuilder s = new StringBuilder(weekday);
        s.append(", ").append(todaysDate);
        day1.setText(weekday.substring(0,3));
        day2.setText(weekCalculator(weekNo+1).substring(0,3));
        day3.setText(weekCalculator(weekNo+2).substring(0,3));
        day4.setText(weekCalculator(weekNo+3).substring(0,3));
        day5.setText(weekCalculator(weekNo+4).substring(0,3));
        day6.setText(weekCalculator(weekNo+5).substring(0,3));
        day7.setText(weekCalculator(weekNo+6).substring(0,3));

        //Food RecycleView Implementation
        options = new FirebaseRecyclerOptions.Builder<FoodMenuModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Food Menu").child(weekday), FoodMenuModel.class)
                .build();
        foodMenuAdapter = new FoodMenuAdapter(options,weekday);
        foodMenuRV.setAdapter(foodMenuAdapter);
    }


    //weekDayCalculator
    public String weekCalculator(int i) {
        int k = i % 7;
        switch (k) {
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 0:
                return "Saturday";
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            default:
                return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        foodMenuAdapter.startListening();
    }
}