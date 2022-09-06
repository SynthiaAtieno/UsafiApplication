package com.example.usafiapp.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
/*import com.example.blood_donantion_app.R;
import com.example.blood_donantion_app.model.Notification;
import com.example.blood_donantion_app.model.Users;*/
import com.example.usafiapp.R;
import com.example.usafiapp.model.Notification;
import com.example.usafiapp.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context context;
    private List<Notification> notificationList;

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new NotificationAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Notification notification = notificationList.get(position);

        holder.notText.setText(notification.getText());
        holder.notDate.setText(notification.getDate());

        getUserInfo(holder.profileImage, holder.notName,notification.getSenderId());
    }

    private void getUserInfo(final CircleImageView circleImageView, final TextView nameTextView, final String senderid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //senderid = snapshot.child("senderId").getValue().toString();
                Notification notification = snapshot.getValue(Notification.class);
                Users users = snapshot.getValue(Users.class);
                nameTextView.setText(users.getName());
                Glide.with(context).load(users.getProfilepictureuri()).into(circleImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView profileImage;
        public TextView notName, notText, notDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            notName = itemView.findViewById(R.id.notificationName);
            notText = itemView.findViewById(R.id.notificationText);
            notDate = itemView.findViewById(R.id.notificationDate);


        }
    }
}