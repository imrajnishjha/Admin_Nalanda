package com.wormos.nalandaadmin;


import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class TransportScheduleAdapter extends FirebaseRecyclerAdapter<TransportScheduleModel, TransportScheduleAdapter.transportScheduleViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    String type;

    public TransportScheduleAdapter(@NonNull FirebaseRecyclerOptions<TransportScheduleModel> options, String type) {
        super(options);
        this.type = type;
    }

    DatabaseReference transportRef = FirebaseDatabase.getInstance().getReference("Transport Schedule");
    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull transportScheduleViewHolder holder, int position, @NonNull TransportScheduleModel model) {
        holder.driverNameEdt.setText(model.getDriverName());
        holder.driverNameTv.setText(model.getDriverName());
        holder.timeTv.setText(model.getTime());
        holder.driverContactNumberEdt.setText(model.getDriverContactNumber());
        holder.driverContactNumberTv.setText(model.getDriverContactNumber());

        holder.editCV.setOnClickListener(view -> {
            if(holder.editTv.getText().equals("Save")){
                holder.editTv.setText("Changing");
                HashMap<String,Object> transportMap = new HashMap<>();
                transportMap.put("driverContactNumber",holder.driverContactNumberEdt.getText().toString());
                transportMap.put("driverName",holder.driverNameEdt.getText().toString());
                transportRef.child(type).child(Objects.requireNonNull(getRef(position).getKey())).updateChildren(transportMap)
                        .addOnSuccessListener(success->{
                            holder.driverContactNumberTv.setVisibility(View.VISIBLE);
                            holder.driverContactNumberEdt.setVisibility(View.GONE);
                            holder.driverNameTv.setVisibility(View.VISIBLE);
                            holder.driverNameEdt.setVisibility(View.GONE);
                            holder.editTv.setText("Edit");
                        });

            } else {
                holder.editTv.setText("Save");
                holder.driverContactNumberTv.setVisibility(View.GONE);
                holder.driverContactNumberEdt.setVisibility(View.VISIBLE);
                holder.driverNameTv.setVisibility(View.GONE);
                holder.driverNameEdt.setVisibility(View.VISIBLE);
                Log.d("ukle", "onBindViewHolder: ");
            }
        });
    }

    @NonNull
    @Override
    public transportScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transport_schedule_item,parent,false);
        return new transportScheduleViewHolder(view);
    }

    static class transportScheduleViewHolder extends RecyclerView.ViewHolder{

        TextView  timeTv,editTv ,driverNameTv,driverContactNumberTv;
        CardView editCV;
        EditText driverNameEdt,driverContactNumberEdt;

        public transportScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            driverNameEdt = itemView.findViewById(R.id.transport_schedule_item_driver_name_Edt);
            driverNameTv = itemView.findViewById(R.id.transport_schedule_item_driver_name_tv);
            timeTv = itemView.findViewById(R.id.transport_schedule_item_timing_tv);
            driverContactNumberEdt = itemView.findViewById(R.id.transport_schedule_item_driver_phone_number_Edt);
            driverContactNumberTv = itemView.findViewById(R.id.transport_schedule_item_driver_phone_number_tv);
            editCV = itemView.findViewById(R.id.transport_schedule_item_edit_cv);
            editTv = itemView.findViewById(R.id.transport_edit_btn);
        }
    }
}