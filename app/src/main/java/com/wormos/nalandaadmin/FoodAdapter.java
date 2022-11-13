package com.wormos.nalandaadmin;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoodAdapter extends FirebaseRecyclerAdapter<StudentHostelModel,FoodAdapter.lunchViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    public FoodAdapter(@NonNull FirebaseRecyclerOptions<StudentHostelModel> options) {
        super(options);
    }
    DatabaseReference studentData = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onBindViewHolder(@NonNull lunchViewHolder holder, int position, @NonNull StudentHostelModel model) {
        holder.userId.setText(model.getId());
        final String[] collegeName = new String[1];

        studentData.child("Students").child(Objects.requireNonNull(getRef(position).getKey())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String purl = Objects.requireNonNull(snapshot.child("purl").getValue()).toString();
                    String name = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                    collegeName[0] = Objects.requireNonNull(snapshot.child("university").getValue()).toString();
                    holder.userName.setText(name);
                    Glide.with(holder.userProfile.getContext())
                            .load(purl)
                            .error(R.drawable.nalanda_logo)
                            .into(holder.userProfile);
                    holder.userCollege.setText(collegeName[0]);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @NonNull
    @Override
    public lunchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_lunch_item,parent,false);
        return new lunchViewHolder(view);
    }

    static class lunchViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userProfile;
        TextView userName,userId,userCollege;


        public lunchViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfile = itemView.findViewById(R.id.user_lunch_user_profile);
            userName = itemView.findViewById(R.id.user_lunch_user_name);
            userId = itemView.findViewById(R.id.user_lunch_user_id);
            userCollege = itemView.findViewById(R.id.user_lunch_collegeName);
        }
    }
}
