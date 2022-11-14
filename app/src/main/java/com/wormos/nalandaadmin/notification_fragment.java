package com.wormos.nalandaadmin;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class notification_fragment extends Fragment {

    View view;
    TextView notificationSubject,notificationDetail;
    AppCompatButton sendNotificationBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification_fragment, container, false);

        notificationSubject = view.findViewById(R.id.notificationSubject);
        notificationDetail = view.findViewById(R.id.notificationDescription);
        sendNotificationBtn = view.findViewById(R.id.notification_send_btn);

        sendNotificationBtn.setOnClickListener(v->{
            if(notificationSubject.getText().toString().isEmpty()){
                notificationSubject.setError("Provide the Subject");
                notificationSubject.requestFocus();
            } else if(notificationDetail.getText().toString().isEmpty()){
                notificationDetail.setError("Provide the Description");
                notificationDetail.requestFocus();
            } else {
                DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference("Student Token");
                tokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            notifyUser(Objects.requireNonNull(dataSnapshot.getValue()).toString(),notificationSubject.getText().toString()
                                    ,notificationDetail.getText().toString());
                            notificationSubject.setText("");
                            notificationDetail.setText("");
                            Toast.makeText(requireContext(), "Notified", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });

        return view;
    }

    public void notifyUser(String key,String Subject,String Description){
        MyFirebaseNotificationSender notificationSender = new MyFirebaseNotificationSender(key,Subject,Description,requireContext(),getActivity());
        notificationSender.SendNotifications();
    }
}