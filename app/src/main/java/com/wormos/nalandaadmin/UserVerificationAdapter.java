package com.wormos.nalandaadmin;

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

public class UserVerificationAdapter extends FirebaseRecyclerAdapter<UserVerificationModel,UserVerificationAdapter.userVerificationViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    public UserVerificationAdapter(@NonNull FirebaseRecyclerOptions<UserVerificationModel> options) {
        super(options);
    }
    DatabaseReference studentData = FirebaseDatabase.getInstance().getReference("Students");

    @Override
    protected void onBindViewHolder(@NonNull userVerificationViewHolder holder, int position, @NonNull UserVerificationModel model) {
        holder.userId.setText(model.getId());

        studentData.child(Objects.requireNonNull(getRef(position).getKey())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String purl = Objects.requireNonNull(snapshot.child("purl").getValue()).toString();
                    String name = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                    holder.userName.setText(name);
                    Glide.with(holder.userProfile.getContext())
                            .load(purl)
                            .error(R.drawable.nalanda_logo)
                            .into(holder.userProfile);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @NonNull
    @Override
    public userVerificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_verification_item,parent,false);
        return new userVerificationViewHolder(view);
    }

    static class userVerificationViewHolder extends RecyclerView.ViewHolder{

        CircleImageView userProfile;
        TextView userName,userId;
        AppCompatButton rejectBtn,approveBtn;

        public userVerificationViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfile = itemView.findViewById(R.id.verification_user_profile);
            userName = itemView.findViewById(R.id.verification_user_name);
            userId = itemView.findViewById(R.id.verification_user_id);
            rejectBtn = itemView.findViewById(R.id.verification_remove_btn);
            approveBtn = itemView.findViewById(R.id.verification_approve_btn);
        }
    }
}
