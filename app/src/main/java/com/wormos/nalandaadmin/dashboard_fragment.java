package com.wormos.nalandaadmin;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;


public class dashboard_fragment extends Fragment {

    RecyclerView highlightRV,storyRV;
    View view;
    FirebaseRecyclerOptions<StoryModel> storyOption;
    FirebaseRecyclerOptions<HighLightModel> highLightOption;
    StoryAdapter storyAdapter;
    HighLightAdapter highLightAdapter;
    CardView addStoryCV,attendanceCard,registeredUserCard,userVerificationCard;
    Uri videoUri;
    ImageView selectStory;




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

        //AddStory
        addStoryCV = view.findViewById(R.id.dashboard_addStoryCV);

        addStoryCV.setOnClickListener(v->{
            Dialog addStoryDialog = new Dialog(getContext());
            LayoutInflater layoutInflater = getLayoutInflater();
            View addStoryView = layoutInflater.inflate(R.layout.select_story_video_popup,null,false);
            addStoryDialog.setContentView(addStoryView);
            addStoryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            addStoryDialog.show();
            selectStory = addStoryView.findViewById(R.id.select_video_story);
            TextView uploadStory = addStoryView.findViewById(R.id.uploadStoryTV);
            selectStory.setOnClickListener(storyView ->{
                chooseVideo();
            });

            uploadStory.setOnClickListener(uploadView ->{
                UploadStorytoFirebase(addStoryDialog);
            });

        });

        //Dashboard navigation's
        attendanceCard = view.findViewById(R.id.dashboard_attendance);
        registeredUserCard = view.findViewById(R.id.dashboard_users);
        userVerificationCard = view.findViewById(R.id.dashboard_verification);
        attendanceCard.setOnClickListener(v->startActivity(new Intent(getContext(),UserAttendance.class)));
        registeredUserCard.setOnClickListener(v->startActivity(new Intent(getContext(),UserRegistered.class)));
        userVerificationCard.setOnClickListener(v->startActivity(new Intent(getContext(),UserVerification.class)));

        return view;
    }

    private void UploadStorytoFirebase(Dialog dialog) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("/Stories");
        storageRef.putFile(videoUri).addOnSuccessListener(success-> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            DatabaseReference storyRef = FirebaseDatabase.getInstance().getReference("Stories");
            String key = storyRef.push().getKey();
            HashMap<String,Object> storyMap = new HashMap<>();
            storyMap.put("videoPurl",uri.toString());
            storyMap.put("thumbnail",uri.toString());
            assert key != null;
            storyRef.child(key).updateChildren(storyMap).addOnSuccessListener(successRef-> dialog.dismiss());
        }));
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
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        storyAdapter.startListening();
        highLightAdapter.startListening();
    }
}