package com.wormos.nalandaadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class HighLightAdapter extends FirebaseRecyclerAdapter<HighLightModel,HighLightAdapter.highLightViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public HighLightAdapter(@NonNull FirebaseRecyclerOptions<HighLightModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull highLightViewHolder holder, int position, @NonNull HighLightModel model) {

    }

    @NonNull
    @Override
    public highLightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.highlight_item,parent,false);
        return new highLightViewHolder(view);
    }

    static class highLightViewHolder extends RecyclerView.ViewHolder{

        TextView linkName,title,tagline;
        ImageView highLightPurl;
        public highLightViewHolder(@NonNull View itemView) {
            super(itemView);
            linkName = itemView.findViewById(R.id.highlight_linkName);
            title = itemView.findViewById(R.id.highlight_title);
            tagline = itemView.findViewById(R.id.highlight_tagline);
            highLightPurl = itemView.findViewById(R.id.highlight_purl);
        }
    }
}
