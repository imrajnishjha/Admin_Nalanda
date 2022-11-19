package com.wormos.nalandaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class HostelAdd extends AppCompatActivity {

    EditText hostelAddName,hostelAddAddress,hostelAddAddressLink,hostelAddDescription;
    AppCompatButton hostelAddBackBtn,hostelAddUploadBtn;
    ImageSlider hostelImageSlider;
    List<SlideModel> imageSlideModels = new ArrayList<>();
    List<Uri> imageUris;
    RelativeLayout progressbarRl;
    RadioGroup categoryRadioGrp;
    RadioButton categoryRadioBtn;
    String editable;
    String hostelKey;
    DatabaseReference hostelRef = FirebaseDatabase.getInstance().getReference("Hostel Data");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_add);

        hostelAddAddress= findViewById(R.id.hostel_add_Address);
        hostelAddDescription = findViewById(R.id.add_hostel_Description);
        hostelAddName = findViewById(R.id.hostel_add_name);
        hostelAddAddressLink = findViewById(R.id.hostel_add_address_link);
        hostelAddBackBtn = findViewById(R.id.hostel_add_back_btn);
        hostelAddUploadBtn = findViewById(R.id.hostel_add_upload_btn);
        hostelImageSlider = findViewById(R.id.hostel_add_image_slider);
        progressbarRl= findViewById(R.id.hostel_add_upload_progressBarRL);
        categoryRadioGrp = findViewById(R.id.hostel_Add_radio_group);
        editable = getIntent().getStringExtra("edit");
        hostelKey = getIntent().getStringExtra("hostelKey");


        hostelAddBackBtn.setOnClickListener(view -> finish());

//Loading the hostel data if editing option is selected
        if(editable.equals("1")){
            hostelRef.child(hostelKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot imageUri:snapshot.child("images").getChildren()){
                            imageSlideModels.add(new SlideModel(Objects.requireNonNull(imageUri.getValue()).toString(), ScaleTypes.CENTER_CROP));
                        }
                        String hostelNameStr = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                        String hostelAddressStr = Objects.requireNonNull(snapshot.child("address").getValue()).toString();
                        String hostelAddressLinkStr = Objects.requireNonNull(snapshot.child("addressLink").getValue()).toString();
                        String hostelDescriptionStr = Objects.requireNonNull(snapshot.child("description").getValue()).toString();
                        String hostelCategoryStr = Objects.requireNonNull(snapshot.child("category").getValue()).toString();
                        hostelAddName.setText(hostelNameStr);
                        hostelAddAddress.setText(hostelAddressStr);
                        hostelAddAddressLink.setText(hostelAddressLinkStr);
                        hostelAddDescription.setText(hostelDescriptionStr);
                        if(hostelCategoryStr.equals("Boys")){
                            categoryRadioGrp.check(R.id.hostel_add_boys);
                        } else {
                            categoryRadioGrp.check(R.id.hostel_add_girls);
                        }

                        hostelImageSlider.setImageList(imageSlideModels);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    //uploading data to firebase
        hostelAddUploadBtn.setOnClickListener(view -> {
            int selectedRadioBtn = categoryRadioGrp.getCheckedRadioButtonId();
            categoryRadioBtn = findViewById(selectedRadioBtn);
            if(hostelAddName.getText().toString().isEmpty()){
                hostelAddName.setError("Please Enter Name");
                hostelAddName.requestFocus();
            }  else if(hostelAddAddress.getText().toString().isEmpty()){
                hostelAddAddress.setError("Please Enter Name");
                hostelAddAddress.requestFocus();
            }else if(hostelAddAddressLink.getText().toString().isEmpty()){
                hostelAddAddressLink.setError("Please Enter Name");
                hostelAddAddressLink.requestFocus();
            }else if(hostelAddDescription.getText().toString().isEmpty()){
                hostelAddDescription.setError("Please Enter Name");
                hostelAddDescription.requestFocus();
            }else if(selectedRadioBtn==-1){
                Toast.makeText(getApplicationContext(),"Please select Category",Toast.LENGTH_SHORT).show();
            } else {

                if(editable.equals("0")){
                    if(imageUris==null){
                        Toast.makeText(getApplicationContext(),"Please select Image",Toast.LENGTH_SHORT).show();
                    } else  {
                        UploadImageToFirebase(imageUris);
                    }
                } else {
                    if(imageUris==null){
                        UploadEditedDataToFireBase();
                    } else {
                        UploadImageToFirebase(imageUris);
                    }
                }
            }
        });


    //select image from gallery

        hostelImageSlider.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Pictures: "), 1);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1) {
                if(resultCode == RESULT_OK ){
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        imageUris = new ArrayList<>();
                        for (int i = 0; i < count; i++) {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            imageSlideModels.add(new SlideModel(imageUri.toString(), ScaleTypes.CENTER_CROP));
                            imageUris.add(imageUri);
                        }
                        hostelImageSlider.setImageList(imageSlideModels);
                    }  else if(data.getData() != null) {
                        imageUris = new ArrayList<>();
                        Uri imageUri = data.getData();
                        imageUris.add(imageUri);
                        imageSlideModels.add(new SlideModel(imageUri.toString(), ScaleTypes.CENTER_CROP));
                        hostelImageSlider.setImageList(imageSlideModels);
                    }

                }
            }
        }

    //function to upload hostel image on firebase storage
    public void UploadImageToFirebase(List<Uri> uris){
        progressbarRl.setVisibility(View.VISIBLE);

        StorageReference storageHostelReference = FirebaseStorage.getInstance().getReference("Hostel Images/" + hostelAddName.getText().toString());

        DatabaseReference HostelReference = FirebaseDatabase.getInstance().getReference().child("Hostel Data").child(hostelAddName.getText().toString());
        for(Uri imgUri:uris){
            String uriKey = autoStringGenerator();
            storageHostelReference.child(uriKey).putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageHostelReference.child(uriKey).getDownloadUrl().addOnSuccessListener(uri -> {
                        HashMap<String,Object> newHostelImageMap = new HashMap<>();
                        newHostelImageMap.put(uriKey,uri.toString());
                        HostelReference.child("images").updateChildren(newHostelImageMap)
                                .addOnFailureListener(fail->progressbarRl.setVisibility(View.GONE));
                    });
                }
            }).addOnFailureListener(fail->progressbarRl.setVisibility(View.GONE));
        }
        HashMap<String,Object> newHostelDataMap = new HashMap<>();
        newHostelDataMap.put("name",hostelAddName.getText().toString());
        newHostelDataMap.put("address",hostelAddAddress.getText().toString());
        newHostelDataMap.put("addressLink",hostelAddAddressLink.getText().toString());
        newHostelDataMap.put("description",hostelAddDescription.getText().toString());
        newHostelDataMap.put("category",categoryRadioBtn.getText().toString());
        HostelReference.updateChildren(newHostelDataMap)
                .addOnSuccessListener(success->progressbarRl.setVisibility(View.GONE))
                .addOnFailureListener(fail->progressbarRl.setVisibility(View.GONE));
    }

//upload edited data to firebase
    public void UploadEditedDataToFireBase(){
        progressbarRl.setVisibility(View.VISIBLE);
        DatabaseReference HostelReference = FirebaseDatabase.getInstance().getReference().child("Hostel Data").child(hostelAddName.getText().toString());
        HashMap<String,Object> EditedHostelDataMap = new HashMap<>();
        EditedHostelDataMap.put("name",hostelAddName.getText().toString());
        EditedHostelDataMap.put("address",hostelAddAddress.getText().toString());
        EditedHostelDataMap.put("addressLink",hostelAddAddressLink.getText().toString());
        EditedHostelDataMap.put("description",hostelAddDescription.getText().toString());
        EditedHostelDataMap.put("category",categoryRadioBtn.getText().toString());
        HostelReference.updateChildren(EditedHostelDataMap)
                .addOnSuccessListener(success->progressbarRl.setVisibility(View.GONE))
                .addOnFailureListener(fail->progressbarRl.setVisibility(View.GONE));
    }

    public String autoStringGenerator() {
        String Alphabet = "abcdefg123456789hijaklmnop2347787492dsjahfj234t62831234567890qwertyuiopasdfghjklzxcvbnm,1234567890qawsedrtyuisdfghjxqrstuvwxyz";
        StringBuilder randomPass = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(Alphabet.length());
            char randomChar1 = Alphabet.charAt(index);
            randomPass.append(randomChar1);
        }
        return randomPass.toString();
    }
}