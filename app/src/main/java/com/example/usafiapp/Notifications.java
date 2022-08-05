package com.example.usafiapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
/*
import com.example.blood_donantion_app.adapter.NotificationAdapter;
import com.example.blood_donantion_app.model.Notification;*/
import com.example.usafiapp.adapter.NotificationAdapter;
import com.example.usafiapp.model.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class Notifications extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    NotificationAdapter notificationAdapter;

    List<String> idList;
    List<Notification> notificationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        toolbar = findViewById(R.id.toolbca);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sent Emails");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        notificationsList = new ArrayList<>();

        notificationAdapter = new NotificationAdapter(Notifications.this, notificationsList);
        recyclerView.setAdapter(notificationAdapter);
        readNotifications();
    }

    private void readNotifications() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("notifications").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    notificationsList.add(notification);
                }
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                startActivity(new Intent(Notifications.this, MainActivity.class));
                finish();
                return  true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}