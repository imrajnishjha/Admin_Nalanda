package com.wormos.nalandaadmin;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class EventManagementAdapter extends FirebaseRecyclerAdapter<EventManagementModel,EventManagementAdapter.EventListViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    public EventManagementAdapter(@NonNull FirebaseRecyclerOptions<EventManagementModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EventListViewHolder holder, int position, @NonNull EventManagementModel model) {
        holder.name.setText(model.getTitle());
        holder.location.setText(model.getLocation());
        holder.time.setText(model.getTime());
        String year = model.getDate();
        if (year.length() == 10) {
            String day = year.substring(0, 1);
            String month = year.substring(2, 5);
            holder.day.setText(day);
            holder.month.setText(month);

        } else {
            String day = year.substring(0, 2);
            String month = year.substring(3, 6);
            holder.day.setText(day);
            holder.month.setText(month);
        }

        Glide.with(holder.eventImg.getContext())
                .load(model.getImgUrl())
                .error(R.drawable.nalanda_logo)
                .into(holder.eventImg);

        holder.EventListView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), EventDetail.class);
            intent.putExtra("EventItemKey", getRef(position).getKey());
            intent.putExtra("edit","1");
            view.getContext().startActivity(intent);
        });
    }

    @NonNull
    @Override
    public EventListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View eventView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item,parent,false);
        return new EventListViewHolder(eventView);
    }

    static class EventListViewHolder extends RecyclerView.ViewHolder {

        ImageView eventImg;
        TextView day,month,name,location,time;
        View EventListView;


        public EventListViewHolder(@NonNull View itemView) {
            super(itemView);

            eventImg =  itemView.findViewById(R.id.EventListImage);
            day =  itemView.findViewById(R.id.EventListDay);
            month =   itemView.findViewById(R.id.EventListMonth);
            name =  itemView.findViewById(R.id.EventListName);
            location =  itemView.findViewById(R.id.EventListLocation);
            time =  itemView.findViewById(R.id.EventListTime);
            EventListView= itemView;
        }
    }
}
