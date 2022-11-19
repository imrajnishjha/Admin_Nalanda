package com.wormos.nalandaadmin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;

public class EventDetail extends AppCompatActivity {

    ImageView eventDetailImg;
    DatabaseReference EventReference = FirebaseDatabase.getInstance().getReference("Events");
    ActivityResultLauncher<String> mGetEventImage;
    Uri EventImageUri=null;
    CardView eventDetailEditCV;
    StorageReference storageProfilePicReference;
    EditText EventName,EventLocation,EventDescription;
    String date="",time="",description="",lower_casetitle="",Eventyear="",key,editType,editPurlStr,editNameStr,editLocationStr,editDescriptionStr,editDateStr,editWeekDayStr,editTimeStr;
    TextView Eventdate,Eventmonth,EventTime,EventWeekDay,nullYearTV;
    DatePickerDialog.OnDateSetListener Datelistner;
    TimePickerDialog.OnTimeSetListener Timelistner;
    AppCompatButton EventPostBtn;
    ProgressDialog EventCreatProgress;
    Vector<String> res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        storageProfilePicReference = FirebaseStorage.getInstance().getReference();

        eventDetailImg = findViewById(R.id.EventDetailImage);
        Eventdate = findViewById(R.id.EventDetailDate);
        Eventmonth = findViewById(R.id.EventDetailMonth);
        EventTime = findViewById(R.id.EventDetailTime);
        eventDetailEditCV = findViewById(R.id.EventDetailDateCV);
        EventWeekDay = findViewById(R.id.EventDetailWeekday);
        EventPostBtn = findViewById(R.id.EventDetailPostEvent);
        EventName = findViewById(R.id.EventDetailName);
        EventLocation = findViewById(R.id.EventDetailLocation);
        nullYearTV = findViewById(R.id.nullyearTV);
        EventDescription = findViewById(R.id.EventDetailDescription);
        key= getIntent().getStringExtra("EventItemKey");
        editType = getIntent().getStringExtra("edit");



        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        if(editType.equals("1")){
            EventReference.child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        editPurlStr = Objects.requireNonNull(snapshot.child("imgUrl").getValue()).toString();
                        editNameStr = Objects.requireNonNull(snapshot.child("title").getValue()).toString();
                        editLocationStr = Objects.requireNonNull(snapshot.child("location").getValue()).toString();
                        editDescriptionStr = Objects.requireNonNull(snapshot.child("description").getValue()).toString();
                        editDateStr = Objects.requireNonNull(snapshot.child("date").getValue()).toString();
                        editWeekDayStr = Objects.requireNonNull(snapshot.child("weekday").getValue()).toString();
                        editTimeStr = Objects.requireNonNull(snapshot.child("time").getValue()).toString();
                        Glide.with(getApplicationContext())
                                .load(editPurlStr)
                                .error(R.drawable.nalanda_logo)
                                .into(eventDetailImg);
                        EventName.setText(editNameStr);
                        EventDescription.setText(editDescriptionStr);
                        EventLocation.setText(editLocationStr);
                        EventTime.setText(editTimeStr);
                        EventWeekDay.setText(editWeekDayStr);
                        nullYearTV.setText(editDateStr);
                        res = splitStrings(editDateStr);
                        Eventdate.setText(res.get(0));
                        Eventmonth.setText(res.get(1));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



        eventDetailEditCV.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(EventDetail.this,android.R.style.Theme_Material_Dialog_Alert,Timelistner,
                    hour,minute,false);

            timePickerDialog.show();
        });

        Datelistner = (datePicker, i, i1, i2) -> {
            i1 = i1+1;
            Eventmonth.setText(Monthcalculator(i1));
            Eventdate.setText(String.valueOf(i2));
            Calendar week = Calendar.getInstance();
            week.set(i,i1,i2);
            int dayOfWeek = week.get(Calendar.DAY_OF_WEEK);
            Log.e("ghg", "onDateSet: "+dayOfWeek );
            EventWeekDay.setText(Weekcalculator(dayOfWeek));
            Eventyear = i2 + "-" + Monthcalculator(i1) + "-" + i;
            nullYearTV.setText(Eventyear);


        };
        Timelistner = (timePicker, i, i1) -> {
            String time = timeFormatter(i,i1);
            EventTime.setText(time);
            DatePickerDialog datePickerDialog = new DatePickerDialog(EventDetail.this,
                    android.R.style.Theme_Material_Dialog_Alert, Datelistner,year,month,day);

            datePickerDialog.show();
        };


        eventDetailImg.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            LayoutInflater pickImglayoutInflater = getLayoutInflater();
            View pickImgview = pickImglayoutInflater.inflate(R.layout.image_picker_item, null);
            builder.setCancelable(true);
            builder.setView(pickImgview);
            AlertDialog alertDialogImg = builder.create();
            Window window = alertDialogImg.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            alertDialogImg.show();
            window.setAttributes(wlp);

            CardView cameraCardView = pickImgview.findViewById(R.id.chooseCamera);
            CardView galleryCardView = pickImgview.findViewById(R.id.chooseGallery);

            galleryCardView.setOnClickListener(view1 -> {
                ImagePicker.with(this)
                        .galleryOnly()
                        .crop(4f,3f)//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(620, 620)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(0);
                alertDialogImg.dismiss();
            });
            cameraCardView.setOnClickListener(view1 -> {
                ImagePicker.with(this)
                        .cameraOnly()
                        .crop(4f,3f)//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(620, 620)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(1);
                alertDialogImg.dismiss();
            });
        });

        EventPostBtn.setOnClickListener(view -> {
            if(EventName.getText().toString().isEmpty()){
                EventName.setError("Enter the event name");
                EventName.requestFocus();
            } else if(EventLocation.getText().toString().isEmpty()) {
                EventLocation.setError("Enter the Location");
                EventLocation.requestFocus();
            } else if(EventDescription.getText().toString().isEmpty()){
                EventDescription.setError("Enter the Description");
                EventDescription.requestFocus();
            } else if(Eventdate.getText().toString().isEmpty()) {
                Toast.makeText(EventDetail.this, "Please select a Date",Toast.LENGTH_SHORT).show();
            } else if(EventTime.getText().toString().isEmpty()) {
                Toast.makeText(EventDetail.this, "Please select a Time", Toast.LENGTH_SHORT).show();
            } else{
                EventCreatProgress = new ProgressDialog(EventDetail.this);
                if(editType.equals("0")){
                    if(EventImageUri==null){
                        Toast.makeText(EventDetail.this, "Please select an Image",Toast.LENGTH_SHORT).show();
                    } else {
                        uploadEventData(EventImageUri,EventCreatProgress);
                    }
                } else {
                    if(EventImageUri==null){
                        uploadEditedEventStr(EventCreatProgress);
                    } else {
                        uploadEditedEvent(EventImageUri,EventCreatProgress);
                    }
                }

            }

        });
    }




    private void uploadEventData(Uri EventImageUri, ProgressDialog EventCreatProgress) {
        EventCreatProgress.setMessage("Event Creating...");
        EventCreatProgress.show();
        StorageReference productFileRef = storageProfilePicReference.child("Events Images/" + EventName.getText().toString());
        productFileRef.putFile(EventImageUri).addOnSuccessListener(taskSnapshot -> productFileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            DatabaseReference EventReference = FirebaseDatabase.getInstance().getReference().child("Events");
            String newEventKey = EventReference.push().getKey();
            HashMap<String,Object> newEventMap = new HashMap<>();
            newEventMap.put("date",Eventyear);
            newEventMap.put("title",EventName.getText().toString());
            newEventMap.put("location",EventLocation.getText().toString());
            newEventMap.put("description",EventDescription.getText().toString());
            newEventMap.put("weekday",EventWeekDay.getText().toString());
            newEventMap.put("time",EventTime.getText().toString());
            newEventMap.put("imgUrl",uri.toString());

            EventReference.child(Objects.requireNonNull(newEventKey)).updateChildren(newEventMap).addOnSuccessListener(unused -> {
                Toast.makeText(EventDetail.this, "Event Created", Toast.LENGTH_SHORT).show();
                EventCreatProgress.dismiss();
                startActivity(new Intent(EventDetail.this,EventDetail.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra("edit","0"));


            }).addOnFailureListener(e -> {
                Toast.makeText(EventDetail.this, "Event Creation Failed!", Toast.LENGTH_SHORT).show();
                EventCreatProgress.dismiss();
            });


        }).addOnFailureListener(e -> {
            Toast.makeText(EventDetail.this, "Event Creation Failed!", Toast.LENGTH_SHORT).show();
            EventCreatProgress.dismiss();
        })).addOnFailureListener(e -> {
            Toast.makeText(EventDetail.this, "Event Creation Failed!", Toast.LENGTH_SHORT).show();
            EventCreatProgress.dismiss();
        });
    }

    public void uploadEditedEvent(Uri eventImageUri,ProgressDialog dialog){
        dialog.setMessage("Event updating...");
        dialog.show();
        StorageReference productFileRef = storageProfilePicReference.child("Events Images/" + EventName.getText().toString());
        productFileRef.putFile(eventImageUri).addOnSuccessListener(s-> productFileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            HashMap<String,Object> editedMap = new HashMap<>();
            editedMap.put("date",nullYearTV.getText().toString());
            editedMap.put("title",EventName.getText().toString());
            editedMap.put("location",EventLocation.getText().toString());
            editedMap.put("description",EventDescription.getText().toString());
            editedMap.put("weekday",EventWeekDay.getText().toString());
            editedMap.put("time",EventTime.getText().toString());
            editedMap.put("imgUrl",uri.toString());
            EventReference.child(key).updateChildren(editedMap).addOnSuccessListener(s1 ->{
                Toast.makeText(EventDetail.this, "Event Updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                if(!EventName.getText().toString().equals(editNameStr)){
                    StorageReference oldProductRef = storageProfilePicReference.child("Events Images/" + editNameStr);
                    oldProductRef.delete();
                }

            }).addOnFailureListener(f->{
                Toast.makeText(EventDetail.this, "Event Updating Failed!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });

        }).addOnFailureListener(f->{
            Toast.makeText(EventDetail.this, "Event Updating Failed!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        })).addOnFailureListener(f->{
            Toast.makeText(EventDetail.this, "Event Updating Failed!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }

    public void uploadEditedEventStr(ProgressDialog dialog){
        dialog.setMessage("Event updating...");
        dialog.show();
        HashMap<String,Object> editedMap = new HashMap<>();
        editedMap.put("date",nullYearTV.getText().toString());
        editedMap.put("title",EventName.getText().toString());
        editedMap.put("location",EventLocation.getText().toString());
        editedMap.put("description",EventDescription.getText().toString());
        editedMap.put("weekday",EventWeekDay.getText().toString());
        editedMap.put("time",EventTime.getText().toString());
        EventReference.child(key).updateChildren(editedMap).addOnSuccessListener(s->{
            Toast.makeText(EventDetail.this, "Event Updated", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }).addOnFailureListener(f->{
            Toast.makeText(EventDetail.this, "Event Updating Failed!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            if (resultCode == RESULT_OK && requestCode == 0) {
                EventImageUri = data.getData();
                eventDetailImg.setImageURI(EventImageUri);
            } else if (resultCode == RESULT_OK && requestCode == 1) {
                EventImageUri = data.getData();
                eventDetailImg.setImageURI(EventImageUri);
            }
        }

    }


    public String Monthcalculator(int i){

        switch(i){
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";

        }
        return "null";
    }

    public String Weekcalculator(int i) {

        switch (i) {
            case 7:
                return "Thursday";
            case 1:
                return "Friday";
            case 2:
                return "Saturday";
            case 3:
                return "Sunday";
            case 4:
                return "Monday";
            case 5:
                return "Tuesday";
            case 6:
                return "Wednesday";
            default:
                return null;
        }

    }

    public String timeFormatter(int hour,int min){
        StringBuilder Time = new StringBuilder();
        if (hour<12) {
            if(hour<1){
                if(min<10){
                    Time.append("12:0").append(min).append("AM");
                } else {
                    Time.append("12:").append(min).append("AM");
                }
            } else if(hour<10){
                if(min<10){
                    Time.append("0").append(hour).append(":0").append(min).append("AM");
                } else {
                    Time.append("0").append(hour).append(":").append(min).append("AM");
                }
            } else {
                if(min<10){
                    Time.append(hour).append(":0").append(min).append("AM");
                } else {
                    Time.append(hour).append(":").append(min).append("AM");
                }
            }
        } else {
            if(hour==12){
                if(min<10){
                    Time.append("12:0").append(min).append("PM");
                } else {
                    Time.append("12:").append(min).append("PM");
                }
            } else {
                hour=hour-12;
                if(hour<10){
                    if(min<10){
                        Time.append("0").append(hour).append(":0").append(min).append("PM");
                    } else {
                        Time.append("0").append(hour).append(":").append(min).append("PM");
                    }
                } else {
                    if(min<10){
                        Time.append(hour).append(":0").append(min).append("PM");
                    } else {
                        Time.append(hour).append(":").append(min).append("PM");
                    }
                }

            }
        }
        return Time.toString();
    }

    static Vector<String> splitStrings(String str)
    {
        StringBuilder word = new StringBuilder();


        str = str + '-';

        int l = str.length();

        Vector<String> substr_list = new Vector<>();
        for (int i = 0; i < l; i++)
        {
            if (str.charAt(i) != '-')
            {
                word.append(str.charAt(i));
            }
            else
            {
                if (word.length() != 0)
                {
                    substr_list.add(word.toString());
                }
                word = new StringBuilder();
            }
        }
        return substr_list;
    }
}