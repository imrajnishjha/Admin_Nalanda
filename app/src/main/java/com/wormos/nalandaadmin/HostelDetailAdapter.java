package com.wormos.nalandaadmin;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class HostelDetailAdapter extends FirebaseRecyclerAdapter<HostelDetailModel,HostelDetailAdapter.hostelViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    public HostelDetailAdapter(@NonNull FirebaseRecyclerOptions<HostelDetailModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull hostelViewHolder holder, int position, @NonNull HostelDetailModel model) {
        holder.hostelName.setText(model.getName());
        holder.hostelLocation.setText(model.getAddress());

        holder.hostelView.setOnClickListener(view -> view.getContext().startActivity(new Intent(view.getContext(),HostelAdd.class)
                .putExtra("hostelKey",getRef(holder.getAbsoluteAdapterPosition()).getKey()).putExtra("edit","1")));


    }

    @NonNull
    @Override
    public hostelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hostel_detail_item,parent,false);
        return new hostelViewHolder(view);
    }

    static class hostelViewHolder extends RecyclerView.ViewHolder{

        ImageView hostelIv;
        TextView hostelName,hostelLocation;
        View hostelView;

        public hostelViewHolder(@NonNull View itemView) {
            super(itemView);
            hostelIv = itemView.findViewById(R.id.hostel_item_Iv);
            hostelLocation = itemView.findViewById(R.id.hostel_item_location);
            hostelName = itemView.findViewById(R.id.hostel_item_name);
            hostelView = itemView;
        }
    }
}
