package com.wormos.nalandaadmin;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class HighLightAdapter extends FirebaseRecyclerAdapter<HighLightModel,HighLightAdapter.highLightViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    public HighLightAdapter(@NonNull FirebaseRecyclerOptions<HighLightModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull highLightViewHolder holder, int position, @NonNull HighLightModel model) {
        holder.title.setText(model.getTitle());
        holder.tagline.setText(model.getTagline());
        holder.linkName.setText(model.getLinkName());
        Glide.with(holder.highLightPurl.getContext())
                .load(model.getPurl())
                .placeholder(R.drawable.nalanda_logo)
                .into(holder.highLightPurl);
        holder.linkName.setOnClickListener(linkView->{
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://"+model.getLink()));
            linkView.getContext().startActivity(i);
        });

        holder.removeIcon.setOnClickListener(view -> {
            holder.removeText.setText(R.string.removing);
            DatabaseReference highlightRef = FirebaseDatabase.getInstance().getReference("Highlights");
            highlightRef.child(Objects.requireNonNull(getRef(position).getKey())).removeValue();
        });

    }

    @NonNull
    @Override
    public highLightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.highlight_item,parent,false);
        return new highLightViewHolder(view);
    }

    static class highLightViewHolder extends RecyclerView.ViewHolder{

        TextView linkName,title,tagline,removeText;
        ImageView highLightPurl;
        CardView removeIcon;
        public highLightViewHolder(@NonNull View itemView) {
            super(itemView);
            linkName = itemView.findViewById(R.id.highlight_linkName_popup);
            title = itemView.findViewById(R.id.highlight_title_popup);
            tagline = itemView.findViewById(R.id.highlight_tagline_popup);
            highLightPurl = itemView.findViewById(R.id.highlight_purl_popup);
            removeIcon = itemView.findViewById(R.id.highlight_item_remove_cv);
            removeText = itemView.findViewById(R.id.highlight_item_remove_edit_btn_txt);
        }
    }
}
