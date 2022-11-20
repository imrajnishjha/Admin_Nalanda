package com.wormos.nalandaadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ReferAdapter extends FirebaseRecyclerAdapter<ReferModel,ReferAdapter.referViewModel> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    public ReferAdapter(@NonNull FirebaseRecyclerOptions<ReferModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull referViewModel holder, int position, @NonNull ReferModel model) {
        holder.referralText.setText(String.format("%s Just Referred to %s", model.getReferralName(), model.getName()));
    }

    @NonNull
    @Override
    public referViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.refer_item,parent,false);
        return new referViewModel(view);
    }

    static class referViewModel extends RecyclerView.ViewHolder{
        TextView referralText;
        View referView;

        public referViewModel(@NonNull View itemView) {
            super(itemView);
            referralText= itemView.findViewById(R.id.refer_item_text);
            referView = itemView;
        }

    }
}
