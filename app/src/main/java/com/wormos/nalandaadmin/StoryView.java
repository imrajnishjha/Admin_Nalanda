package com.wormos.nalandaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class StoryView extends AppCompatActivity {

    String key;
    VideoView storyView;
    ImageView thumbnailView;
    AppCompatButton storyBackBtn;
    DatabaseReference storyReference = FirebaseDatabase.getInstance().getReference("Stories");
    RelativeLayout storyLoadProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_view);

        storyView = findViewById(R.id.story_view_video);
        storyBackBtn = findViewById(R.id.story_view_back_btn);
        thumbnailView = findViewById(R.id.story_view_thumbnail);
        key=getIntent().getStringExtra("storyKey");
        storyLoadProgress = findViewById(R.id.story_view_progressBarRL);

        storyBackBtn.setOnClickListener(view -> finish());

        storyReference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String videoPurlStr = Objects.requireNonNull(snapshot.child("videoPurl").getValue()).toString();
                Glide.with(getApplicationContext())
                        .load(videoPurlStr)
                        .error(R.drawable.nalanda_bed_logo)
                        .into(thumbnailView);
                playStory(videoPurlStr,storyView,thumbnailView,storyLoadProgress);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

//to play a story

    void playStory(String path,VideoView videoView,ImageView thumbnailView,RelativeLayout progressBar){
        videoView.setVideoPath(path);
        videoView.setOnPreparedListener(mediaPlayer -> {
            mediaPlayer.start();
            new Handler().postDelayed(() -> {
                progressBar.setVisibility(View.GONE);
                thumbnailView.setVisibility(View.INVISIBLE);
                videoView.setVisibility(View.VISIBLE);
            },500);

        });
        videoView.setOnCompletionListener(mediaPlayer -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}