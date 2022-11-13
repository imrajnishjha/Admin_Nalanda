package com.wormos.nalandaadmin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;



public class AttendanceAdapter extends FirebaseRecyclerAdapter<StudentHostelModel,AttendanceAdapter.attendanceViewHolder>{
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    Context mContext;

    public AttendanceAdapter(@NonNull FirebaseRecyclerOptions<StudentHostelModel> options, Context mContext) {
        super(options);
        this.mContext = mContext;
    }

    DatabaseReference studentData = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onBindViewHolder(@NonNull attendanceViewHolder holder, int position, @NonNull StudentHostelModel model) {
        SharedPreferences adminHostel = mContext.getSharedPreferences("adminHostel", Context.MODE_PRIVATE);
        String hostelName = adminHostel.getString("hostelName"," ");
        final String[] purl = new String[1];
        final String[] name = new String[1];
        holder.userId.setText(model.getId());
        studentData.child("Students").child(Objects.requireNonNull(getRef(position).getKey())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                     purl[0] = Objects.requireNonNull(snapshot.child("purl").getValue()).toString();
                     name[0] = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                    holder.userName.setText(name[0]);
                    Glide.with(holder.userProfile.getContext())
                            .load(purl[0])
                            .error(R.drawable.nalanda_logo)
                            .into(holder.userProfile);
                }
            }
            

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.attendanceGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton selectedOptionAttendance = radioGroup.findViewById(i);
            holder.attendanceMotionLayout.setProgress(0);
            HashMap<String,Object> attendanceMap = new HashMap<>();
            attendanceMap.put(Objects.requireNonNull(getRef(holder.getAbsoluteAdapterPosition()).getKey()),selectedOptionAttendance.getText());
            studentData.child("Attendance").child(hostelName).child(UserAttendance.todaysDateFormatter("YYYY-MM-dd")).updateChildren(attendanceMap)
                    .addOnSuccessListener(success-> new Handler().postDelayed(()-> holder.attendanceMotionLayout.transitionToEnd(),1000));
        });

    }

    @NonNull
    @Override
    public attendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_item,parent,false);
        return new attendanceViewHolder(view);
    }


    static class attendanceViewHolder extends RecyclerView.ViewHolder{

        CircleImageView userProfile;
        TextView userName,userId;
        RadioGroup attendanceGroup;
        MotionLayout attendanceMotionLayout;

        public attendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfile = itemView.findViewById(R.id.attendance_user_profile);
            userName = itemView.findViewById(R.id.attendance_user_name);
            userId = itemView.findViewById(R.id.attendance_user_id);
            attendanceGroup = itemView.findViewById(R.id.attendance_radio_group);
            attendanceMotionLayout = itemView.findViewById(R.id.attendanceMotionLayout);

        }
    }
}
