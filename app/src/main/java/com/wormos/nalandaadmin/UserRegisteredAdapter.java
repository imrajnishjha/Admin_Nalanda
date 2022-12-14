package com.wormos.nalandaadmin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class UserRegisteredAdapter extends FirebaseRecyclerAdapter<UserRegisteredModel,UserRegisteredAdapter.UserRegisteredViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    public UserRegisteredAdapter(@NonNull FirebaseRecyclerOptions<UserRegisteredModel> options, String superAdmin) {
        super(options);
        this.superAdmin = superAdmin;
    }
    String superAdmin;
    DatabaseReference studentData = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onBindViewHolder(@NonNull UserRegisteredViewHolder holder, int position, @NonNull UserRegisteredModel model) {
        holder.userId.setText(model.getId());
        String studentKey = getRef(holder.getAbsoluteAdapterPosition()).getKey();
        final String[] hostelName = new String[1];

        if(superAdmin.equals("yes")){
            holder.userName.setText(model.getName());
            Glide.with(holder.userProfile.getContext())
                    .load(model.getPurl())
                    .error(R.drawable.nalanda_logo)
                    .into(holder.userProfile);
            hostelName[0] = model.getHostel();
        } else {
            studentData.child("Students").child(Objects.requireNonNull(getRef(position).getKey())).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String purl = Objects.requireNonNull(snapshot.child("purl").getValue()).toString();
                        String name = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                        hostelName[0] = Objects.requireNonNull(snapshot.child("hostel").getValue()).toString();
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

        holder.removeBtn.setOnClickListener(view -> {
            Dialog confirmationDialog = new Dialog(view.getContext());
            @SuppressLint("InflateParams") View v = LayoutInflater.from(view.getContext()).inflate(R.layout.are_you_sure_popup,null,false);
            TextView yesBtn = v.findViewById(R.id.yesbtn);
            TextView noBtn = v.findViewById(R.id.nobtn);
            confirmationDialog.setContentView(v);
            confirmationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            confirmationDialog.show();

            noBtn.setOnClickListener(noView->
                confirmationDialog.dismiss()
            );

            yesBtn.setOnClickListener(yesView->{
                confirmationDialog.setCancelable(false);
                studentData.child("Students").child(Objects.requireNonNull(studentKey)).removeValue()
                        .addOnSuccessListener(success-> studentData.child("Hostel").child(hostelName[0])
                                .child(Objects.requireNonNull(studentKey))
                                .removeValue()
                                .addOnSuccessListener(removalSuccess->confirmationDialog.dismiss())
                                .addOnFailureListener(failure->{
                                    Toast.makeText(view.getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                                    confirmationDialog.dismiss();
                                })).addOnFailureListener(failure->{
                            Toast.makeText(view.getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                            confirmationDialog.dismiss();
                        });
            });

        });
    }

    @NonNull
    @Override
    public UserRegisteredViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.registered_user_item,parent,false);
        return new UserRegisteredViewHolder(view);
    }

    static class UserRegisteredViewHolder extends RecyclerView.ViewHolder{

        CircleImageView userProfile;
        TextView userName,userId;
        AppCompatButton removeBtn;

        public UserRegisteredViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfile = itemView.findViewById(R.id.registration_user_profile);
            userName = itemView.findViewById(R.id.registration_user_name);
            userId = itemView.findViewById(R.id.registration_user_id);
            removeBtn = itemView.findViewById(R.id.registration_reject_btn);
        }
    }
}
