package com.wormos.nalandaadmin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserVerificationAdapter extends FirebaseRecyclerAdapter<UserVerificationModel,UserVerificationAdapter.userVerificationViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    Context mContext;

    public UserVerificationAdapter(@NonNull FirebaseRecyclerOptions<UserVerificationModel> options, Context mContext) {
        super(options);
        this.mContext = mContext;
    }

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onBindViewHolder(@NonNull userVerificationViewHolder holder, int position, @NonNull UserVerificationModel model) {
        SharedPreferences adminHostel = mContext.getSharedPreferences("adminHostel", Context.MODE_PRIVATE);
        String hostelName = adminHostel.getString("hostelName"," ");
        holder.userId.setText(model.getId());

        holder.userName.setText(model.getName());
        Glide.with(holder.userProfile.getContext())
                .load(model.getPurl())
                .error(R.drawable.defaultprofile2)
                .into(holder.userProfile);
        holder.rejectBtn.setOnClickListener(view -> {
            Dialog confirmationDialog = new Dialog(view.getContext());
            @SuppressLint("InflateParams") View v = LayoutInflater.from(view.getContext()).inflate(R.layout.are_you_sure_popup,null,false);
            TextView yesBtn = v.findViewById(R.id.yesbtn);
            TextView noBtn = v.findViewById(R.id.nobtn);
            confirmationDialog.setContentView(v);
            confirmationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            confirmationDialog.show();

            noBtn.setOnClickListener(noView-> confirmationDialog.dismiss());

            yesBtn.setOnClickListener(yesView-> databaseReference.child("New Registration").child(hostelName)
                    .child(Objects.requireNonNull(getRef(holder.getAbsoluteAdapterPosition()).getKey())).removeValue()
                    .addOnSuccessListener(success->confirmationDialog.dismiss()));
        });

        holder.approveBtn.setOnClickListener(view -> {

            Dialog submissionDialog = new Dialog(view.getContext());
            @SuppressLint("InflateParams") View submissionView = LayoutInflater.from(view.getContext()).inflate(R.layout.room_no_and_enter_password_popup,
                    null,
                    false);
            TextView infoTv = submissionView.findViewById(R.id.infoTV);
            EditText infoEdt = submissionView.findViewById(R.id.infoEdt);
            TextView infoSubmitBtn = submissionView.findViewById(R.id.infoSubmitBtn);
            infoTv.setVisibility(View.GONE);
            infoEdt.setHint("Enter Room No");
            submissionDialog.setContentView(submissionView);
            submissionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            submissionDialog.show();

            infoSubmitBtn.setOnClickListener(v->{
                if(infoEdt.getText().toString().isEmpty()){
                    infoEdt.setError("Enter RoomNo");
                    infoEdt.requestFocus();
                } else {
                    String roomNo = infoEdt.getText().toString();
                    submissionDialog.dismiss();
                    Dialog passwordDialog = new Dialog(view.getContext());
                    @SuppressLint("InflateParams") View passwordView = LayoutInflater.from(view.getContext()).inflate(R.layout.room_no_and_enter_password_popup,
                            null,false);
                    TextView infoPasswordTv = passwordView.findViewById(R.id.infoTV);
                    EditText infoPasswordEdt = passwordView.findViewById(R.id.infoEdt);
                    TextView infoPasswordBtn = passwordView.findViewById(R.id.infoSubmitBtn);
                    RelativeLayout infoProgressBar = passwordView.findViewById(R.id.reg_upload_progressBarRL);
                    String adminMail= Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
                    infoPasswordTv.setText(adminMail);
                    infoPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT);
                    passwordDialog.setContentView(passwordView);
                    passwordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    passwordDialog.show();

                    infoPasswordBtn.setOnClickListener(passView->{
                        infoProgressBar.setVisibility(View.VISIBLE);
                        if(infoPasswordEdt.getText().toString().isEmpty()){
                            infoPasswordEdt.setError("Enter Password");
                            infoPasswordEdt.requestFocus();
                            infoProgressBar.setVisibility(View.GONE);
                        } else {
                            String password = infoPasswordEdt.getText().toString();
                            assert adminMail != null;
                            mAuth.signInWithEmailAndPassword(adminMail,password).addOnSuccessListener(loginSuccess->{
                                passwordDialog.setCancelable(false);

                                HashMap<String,Object> studentDataMap = new HashMap<>();
                                studentDataMap.put("id",model.getId());
                                studentDataMap.put("name",model.getName());
                                studentDataMap.put("email",model.getEmail());
                                studentDataMap.put("purl",model.getPurl());
                                studentDataMap.put("address",model.getAddress());
                                studentDataMap.put("city",model.getCity());
                                studentDataMap.put("gender",model.getGender());
                                studentDataMap.put("hostel",model.getHostel());
                                studentDataMap.put("phoneNo",model.getPhoneNo());
                                studentDataMap.put("state",model.getState());
                                studentDataMap.put("university",model.getUniversity());
                                studentDataMap.put("room_type",UserVerification.sharingType(model.getSeater()));
                                studentDataMap.put("room_no",Integer.parseInt(roomNo));
                                databaseReference.child("Students").child(model.getEmail().replaceAll("\\.","%7").toLowerCase())
                                        .updateChildren(studentDataMap).addOnSuccessListener(success->
                                                mAuth.createUserWithEmailAndPassword(model.getEmail().toLowerCase(),"nalanda@123")
                                                .addOnSuccessListener(createSuccess->{
                                                    mAuth.signInWithEmailAndPassword(adminMail,password);
                                                    databaseReference.child("New Registration").child(model.getHostel())
                                                            .child(Objects.requireNonNull(getRef(position).getKey())).removeValue()
                                                            .addOnSuccessListener(reLoginSuccess-> {
                                                                HashMap<String,Object> hostelUserMap = new HashMap<>();
                                                                hostelUserMap.put("id",model.getId());
                                                                databaseReference.child("Hostel").child(model.getHostel())
                                                                        .child(model.getEmail().replaceAll("\\.","%7"))
                                                                        .updateChildren(hostelUserMap).addOnSuccessListener(hostelMapSuccess->passwordDialog.dismiss());
                                                            });
                                                }).addOnFailureListener(failure->{
                                                            Toast.makeText(view.getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                                                            passwordDialog.dismiss();
                                                        })).addOnFailureListener(failure->{
                                            Toast.makeText(view.getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                                            passwordDialog.dismiss();
                                        });
                            }).addOnFailureListener(failure->{
                                infoPasswordEdt.setError("Enter correct Password");
                                infoPasswordEdt.requestFocus();
                                infoProgressBar.setVisibility(View.GONE);
                            });

                        }
                    });
                }
            });

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
