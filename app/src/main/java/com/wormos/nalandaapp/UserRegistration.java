package com.wormos.nalandaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class UserRegistration extends AppCompatActivity {

    //declaration
    TextView registrationAlreadyUserTv;
    AppCompatButton continueRegBtn,registrationBackBtn;
    Spinner registrationMaleFemaleSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        //initialization
        registrationAlreadyUserTv = findViewById(R.id.registration_already_user_tv);
        continueRegBtn = findViewById(R.id.registration_continue_btn);
        registrationBackBtn = findViewById(R.id.registration_back_btn);
        registrationMaleFemaleSpinner = findViewById(R.id.registration_male_female_spinner);

        //Methodology
        //Array adapter for male female spinner
        ArrayAdapter<CharSequence> maleFemaleAdapter = ArrayAdapter.createFromResource(this,
                R.array.male_female_str_array, R.layout.spinner_item);

        maleFemaleAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        registrationMaleFemaleSpinner.setAdapter(maleFemaleAdapter);

        registrationBackBtn.setOnClickListener(view -> finish());

        continueRegBtn.setOnClickListener(view -> startActivity(new Intent(this,SelectHostel.class)));

        registrationAlreadyUserTv.setOnClickListener(view -> {
            startActivity(new Intent(UserRegistration.this, UserLogin.class));
            finish();
        });
    }
}