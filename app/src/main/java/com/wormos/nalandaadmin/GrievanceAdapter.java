package com.wormos.nalandaadmin;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class GrievanceAdapter extends FirebaseRecyclerAdapter<GrievanceModel,GrievanceAdapter.grievanceViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    public GrievanceAdapter(@NonNull FirebaseRecyclerOptions<GrievanceModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull grievanceViewHolder holder, int position, @NonNull GrievanceModel model) {
        holder.grievanceSubject.setText(model.getSubject());
        holder.grievanceStatus.setText(model.getStatus());
        holder.grievanceView.setOnClickListener(view -> view.getContext()
                .startActivity(new Intent(view.getContext(),GrievanceDetail.class)
                        .putExtra("GrievanceKey",getRef(position).getKey())));
    }

    @NonNull
    @Override
    public grievanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grievance_item,parent,false);
        return new grievanceViewHolder(view);
    }

    static class grievanceViewHolder extends RecyclerView.ViewHolder{
        TextView grievanceSubject,grievanceStatus;
        View grievanceView;

        public grievanceViewHolder(@NonNull View itemView) {
            super(itemView);
            grievanceStatus = itemView.findViewById(R.id.grievanceItemStatus);
            grievanceSubject = itemView.findViewById(R.id.grievanceItemTV);
            grievanceView = itemView;
        }
    }
}
