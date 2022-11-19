package com.wormos.nalandaadmin;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;


public class dashboard_fragment extends Fragment {

    RecyclerView highlightRV,storyRV;
    View view;
    FirebaseRecyclerOptions<StoryModel> storyOption;
    FirebaseRecyclerOptions<HighLightModel> highLightOption;
    StoryAdapter storyAdapter;
    HighLightAdapter highLightAdapter;
    CardView addStoryCV,attendanceCard,registeredUserCard,userVerificationCard, userReferralsCard;
    Uri videoUri,imageUri;
    ImageView selectStory,highlightImage;
    TextView userAttendanceTv,userRegisteredTv,userVerificationTv,addMoreTv;
    ImageView userAttendanceIv,userRegisteredIv,userVerificationIv;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard_fragment, container, false);
        userReferralsCard = view.findViewById(R.id.dashboard_referals);
        attendanceCard = view.findViewById(R.id.dashboard_attendance);
        registeredUserCard = view.findViewById(R.id.dashboard_users);
        userVerificationCard = view.findViewById(R.id.dashboard_verification);
        userAttendanceTv = view.findViewById(R.id.userAttendanceText);
        userRegisteredTv = view.findViewById(R.id.userRegisteredText);
        userVerificationTv = view.findViewById(R.id.userVerificationText);
        userAttendanceIv = view.findViewById(R.id.userAttendanceImg);
        userRegisteredIv = view.findViewById(R.id.userRegisteredImg);
        userVerificationIv = view.findViewById(R.id.userVerificationImg);
        addMoreTv = view.findViewById(R.id.dashboard_add_more_text);

    //Super Admin setup
        SharedPreferences adminHostel = requireContext().getSharedPreferences("adminHostel", Context.MODE_PRIVATE);
        String adminName = adminHostel.getString("adminType"," ");
        if(adminName.equals("superadmin")){
            userReferralsCard.setVisibility(View.VISIBLE);
            userAttendanceTv.setText("Hostels");
            userAttendanceIv.setImageResource(R.drawable.hostel_img);
            userRegisteredTv.setText("Students");
            userRegisteredIv.setImageResource(R.drawable.userverification);
            userVerificationTv.setText("Event\nManagement");
            userVerificationIv.setImageResource(R.drawable.event_management);
            addMoreTv.setVisibility(View.VISIBLE);
        }


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


    //add Highlight
        addMoreTv.setOnClickListener(v->{
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
                            .crop(2f, 1f)
                            .maxResultSize(720, 1080)
                            .start(0);
                    alertDialogImg.dismiss();
                });
                cameraCardView.setOnClickListener(view1 -> {
                    ImagePicker.with(this)
                            .cameraOnly()
                            .crop(2f, 1f)
                            .maxResultSize(720, 1080)
                            .start(1);
                    alertDialogImg.dismiss();
                });
            });

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


    //AddStory
        addStoryCV = view.findViewById(R.id.dashboard_addStoryCV);

        addStoryCV.setOnClickListener(v->{
            Dialog addStoryDialog = new Dialog(getContext());
             View addStoryView = View.inflate(requireContext(),R.layout.select_story_video_popup,null);
            addStoryDialog.setContentView(addStoryView);
            addStoryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            addStoryDialog.show();
            selectStory = addStoryView.findViewById(R.id.select_video_story);
            TextView uploadStory = addStoryView.findViewById(R.id.uploadStoryTV);
            RelativeLayout storyUploadProgressBar = addStoryView.findViewById(R.id.story_upload_progressBarRL);
            selectStory.setOnClickListener(storyView -> chooseVideo());

            uploadStory.setOnClickListener(uploadView -> UploadStoryToFirebase(addStoryDialog,storyUploadProgressBar));

        });

        //Dashboard navigation's
        attendanceCard.setOnClickListener(v->startActivity(new Intent(getContext(),UserAttendance.class)));
        registeredUserCard.setOnClickListener(v->startActivity(new Intent(getContext(),UserRegistered.class)));
        userVerificationCard.setOnClickListener(v->{
            if(adminName.equals("superadmin")){
                startActivity(new Intent(getContext(),EventManagement.class));
            } else {
                startActivity(new Intent(getContext(),UserVerification.class));
            }
        });

        return view;
    }

    private void UploadStoryToFirebase(Dialog dialog, RelativeLayout progressBar) {
        if(videoUri!=null){
            DatabaseReference storyRef = FirebaseDatabase.getInstance().getReference("Stories");
            String key = storyRef.push().getKey();
            assert key != null;
            progressBar.setVisibility(View.VISIBLE);
            dialog.setCancelable(false);
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("Stories").child(key);
            storageRef.putFile(videoUri).addOnSuccessListener(success-> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                HashMap<String,Object> storyMap = new HashMap<>();
                storyMap.put("videoPurl",uri.toString());
                storyRef.child(key).updateChildren(storyMap).addOnSuccessListener(successRef-> dialog.dismiss()).addOnFailureListener(failure->dialog.dismiss());
            })).addOnFailureListener(failure->{
                dialog.dismiss();
                Toast.makeText(view.getContext(), "Please try again", Toast.LENGTH_SHORT).show();
            });
        }

    }

    private void UploadHighlightToFirebase(Dialog dialog, RelativeLayout progressBar,String title,String tagline, String link, String linkName) {
        if(imageUri!=null){
            DatabaseReference highlightRef = FirebaseDatabase.getInstance().getReference("Highlights");
            String key = highlightRef.push().getKey();
            assert key != null;
            progressBar.setVisibility(View.VISIBLE);
            dialog.setCancelable(false);
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("Highlights").child(key);
            storageRef.putFile(imageUri).addOnSuccessListener(success-> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                HashMap<String,Object> highlightMap = new HashMap<>();
                highlightMap.put("purl",uri.toString());
                highlightMap.put("Title",title);
                highlightMap.put("link",link);
                highlightMap.put("linkName",linkName);
                highlightMap.put("tagline",tagline);
                highlightRef.child(key).updateChildren(highlightMap).addOnSuccessListener(successRef-> dialog.dismiss()).addOnFailureListener(failure->dialog.dismiss());
            })).addOnFailureListener(failure->{
                dialog.dismiss();
                Toast.makeText(view.getContext(), "Please try again", Toast.LENGTH_SHORT).show();
            });
        }

    }

    public void chooseVideo(){
        Intent videoIntent = new Intent();
        videoIntent.setType("video/*");
        videoIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(videoIntent,90);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==90 || requestCode==RESULT_OK){
            assert data != null;
            videoUri = data.getData();
            Glide.with(requireContext())
                    .load(videoUri)
                    .placeholder(R.drawable.nalanda_logo)
                    .into(selectStory);
        } else if (resultCode == RESULT_OK && requestCode == 0) {
            assert data != null;
            imageUri = data.getData();
            loadHighlight(imageUri);
        } else if (resultCode == RESULT_OK && requestCode == 1) {
            assert data != null;
            imageUri = data.getData();
            loadHighlight(imageUri);
        }
    }

    //Function which load highlight field
    public void loadHighlight(Uri imageUri){
        Dialog addHighlightDialog = new Dialog(getContext());
        View addHighlightView = View.inflate(requireContext(),R.layout.add_highlight_popup,null);
        EditText highlightLink = addHighlightView.findViewById(R.id.highlight_link);
        EditText highlightLinkName = addHighlightView.findViewById(R.id.highlight_linkName);
        EditText highlightTitle = addHighlightView.findViewById(R.id.highlight_title);
        EditText highlightTagLine = addHighlightView.findViewById(R.id.highlight_tagline);
        RelativeLayout highlightProgressBar = addHighlightView.findViewById(R.id.highlight_upload_progressBarRL);
        highlightImage = addHighlightView.findViewById(R.id.highlight_purl);
        TextView uploadHighlightTv = addHighlightView.findViewById(R.id.uploadHighlightTV);
        addHighlightDialog.setContentView(addHighlightView);
        addHighlightDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addHighlightDialog.show();

        Glide.with(requireContext())
                .load(imageUri)
                .placeholder(R.drawable.nalanda_logo)
                .into(highlightImage);
        uploadHighlightTv.setOnClickListener(uploadView-> UploadHighlightToFirebase(addHighlightDialog,highlightProgressBar,highlightTitle.getText().toString(),
                highlightTagLine.getText().toString(),highlightLink.getText().toString(),highlightLinkName.getText().toString()));

    }

    @Override
    public void onStart() {
        super.onStart();
        storyAdapter.startListening();
        highLightAdapter.startListening();
    }
}