package com.wormos.nalandaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class UserRegistration extends AppCompatActivity {

    //declaration
    TextView registrationAlreadyUserTv;
    AppCompatButton continueRegBtn, registrationBackBtn;
    Spinner registrationMaleFemaleSpinner, registrationStatesSpinner, registrationCitiesSpinner;
    String selectedState;
    ArrayAdapter<CharSequence> citiesSpinnerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        //initialization
        registrationAlreadyUserTv = findViewById(R.id.registration_already_user_tv);
        continueRegBtn = findViewById(R.id.registration_continue_btn);
        registrationBackBtn = findViewById(R.id.registration_back_btn);
        registrationMaleFemaleSpinner = findViewById(R.id.registration_male_female_spinner);
        registrationStatesSpinner = findViewById(R.id.registration_states_spinner);
        registrationCitiesSpinner = findViewById(R.id.registration_cities_spinner);

        //Methodology
        //Array adapters for spinners
        ArrayAdapter<CharSequence> maleFemaleAdapter = ArrayAdapter.createFromResource(this,
                R.array.male_female_str_array, R.layout.spinner_item);

        ArrayAdapter<CharSequence> statesSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_indian_states, R.layout.spinner_item);

        citiesSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.cities_default_array, R.layout.spinner_item);

        //setting up the dropdown resources for adapter
        maleFemaleAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        statesSpinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        citiesSpinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        //setting adapter to the spinners
        registrationMaleFemaleSpinner.setAdapter(maleFemaleAdapter);
        registrationStatesSpinner.setAdapter(statesSpinnerAdapter);
        registrationCitiesSpinner.setAdapter(citiesSpinnerAdapter);

        //setting listener for selecting cities based on selected state
        registrationStatesSpinner.setOnItemSelectedListener(new registrationSpinnerListener());

        registrationBackBtn.setOnClickListener(view -> finish());

        continueRegBtn.setOnClickListener(view -> startActivity(new Intent(this, SelectHostel.class)));

        registrationAlreadyUserTv.setOnClickListener(view -> {
            startActivity(new Intent(UserRegistration.this, UserLogin.class));
            finish();
        });
    }

    //listener for selecting cities based on selected state
    private class registrationSpinnerListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //getting the selected state
            selectedState = registrationStatesSpinner.getSelectedItem().toString();
            switch (selectedState){
                case"Select Your State": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.cities_default_array, R.layout.spinner_item); break;
                case"Andaman and Nicobar Islands": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_andaman_and_nicobar_cities, R.layout.spinner_item); break;
                case"Andhra Pradesh": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_andra_pradesh_cities, R.layout.spinner_item); break;
                case"Arunachal Pradesh": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_arunachal_pradesh_cities, R.layout.spinner_item); break;
                case"Assam": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_assam_cities, R.layout.spinner_item); break;
                case"Bihar": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_bihar_cities, R.layout.spinner_item); break;
                case"Chandigarh": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_chandigarh_cities, R.layout.spinner_item); break;
                case"Chhattisgarh": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_chhattisgarh_cities, R.layout.spinner_item); break;
                case"Dadra and Nagar Haveli": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_dadra_and_nagar_haveli_cities, R.layout.spinner_item); break;
                case"Delhi": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_delhi_cities, R.layout.spinner_item); break;
                case"Goa": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_goa_cities, R.layout.spinner_item); break;
                case"Gujarat": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_gujarat_cities, R.layout.spinner_item); break;
                case"Haryana": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_haryana_cities, R.layout.spinner_item); break;
                case"Himachal Pradesh": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_himachal_pradesh_cities, R.layout.spinner_item); break;
                case"Jammu and Kashmir": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_jammu_and_kashmir_cities, R.layout.spinner_item); break;
                case"Jharkhand": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_jharkhand_cities, R.layout.spinner_item); break;
                case"Karnataka": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_karnataka_cities, R.layout.spinner_item); break;
                case"Kerala": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_kerala_cities, R.layout.spinner_item); break;
                case"Madhya Pradesh": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_madhya_pradesh_cities, R.layout.spinner_item); break;
                case"Maharashtra": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_maharashtra_cities, R.layout.spinner_item); break;
                case"Manipur": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_manipur_cities, R.layout.spinner_item); break;
                case"Meghalaya": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_meghalaya_cities, R.layout.spinner_item); break;
                case"Mizoram": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_mizoram_cities, R.layout.spinner_item); break;
                case"Nagaland": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_nagaland_cities, R.layout.spinner_item); break;
                case"Orissa": citiesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_orissa_cities, R.layout.spinner_item); break;
            }
            citiesSpinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            registrationCitiesSpinner.setAdapter(citiesSpinnerAdapter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}