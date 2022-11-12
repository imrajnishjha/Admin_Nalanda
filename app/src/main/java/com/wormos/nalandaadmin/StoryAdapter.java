package com.wormos.nalandaadmin;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class StoryAdapter extends FirebaseRecyclerAdapter<StoryModel,StoryAdapter.storyViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    public StoryAdapter(@NonNull FirebaseRecyclerOptions<StoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull storyViewHolder holder, int position, @NonNull StoryModel model) {
        Glide.with(holder.storyThumbnail.getContext())
                .load(model.getVideoPurl())
                .error(R.drawable.nalanda_bed_logo)
                .into(holder.storyThumbnail);

        holder.view.setOnClickListener(v->{
            v.getContext().startActivity(new Intent(v.getContext(),StoryView.class).putExtra("storyKey",getRef(position).getKey()));
        });


    }

    @NonNull
    @Override
    public storyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item,parent,false);
        return new storyViewHolder(view);
    }

    static class storyViewHolder extends RecyclerView.ViewHolder{

        ImageView storyThumbnail;
        ImageView playbtn;
        View view;

        public storyViewHolder(@NonNull View itemView) {
            super(itemView);
            storyThumbnail = itemView.findViewById(R.id.story_thumbnail);
            playbtn = itemView.findViewById(R.id.storyPlayBtn);
            view = itemView;
        }
    }
}
