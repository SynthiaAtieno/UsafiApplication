package com.example.usafiapp.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
/*import com.example.blood_donantion_app.Email.JavaMailAPi;
import com.example.blood_donantion_app.R;
import com.example.blood_donantion_app.model.Users;*/
import com.example.usafiapp.Email.JavaMailAPi;
import com.example.usafiapp.R;
import com.example.usafiapp.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<Users> usersList;

    public UserAdapter(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Users users = usersList.get(position);
        holder.usertypes.setText(users.getType());
        if (users.getType().equals("Worker"))
        {
            holder.emailBtn.setVisibility(View.VISIBLE);
        }
        holder.useremails.setText(users.getEmail());
        holder.usernames.setText(users.getName());
        holder.userlocations.setText(users.getIdNumber());
        holder.userphones.setText(users.getPhoneNo());
        holder.userinterest.setText(users.getInterest());
        if (users.getProfilepictureuri() != null){
            Glide.with(context).load(users.getProfilepictureuri()).into(holder.profileimages);
        }
        else {
            holder.profileimages.setImageResource(R.drawable.profile);
        }
        final String nameOfTheReciever = users.getName();
        final String idOfTheReciever = users.getId();

        //sending the email
        holder.emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context).setTitle("SEND EMAIL")
                        .setMessage("Send email to "+users.getName()+"?")
                        .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String nameOfSender = snapshot.child("name").getValue().toString();
                                        String email = snapshot.child("email").getValue().toString();
                                        String phone = snapshot.child("phoneNo").getValue().toString();
                                        //String blood = snapshot.child("bloodgroup").getValue().toString();

                                        String mEmail = users.getEmail();
                                        String mSubject = "CLEANING SERVICE";
                                        String mMessage = "Hello "+ nameOfTheReciever+", "+nameOfSender+" would like you to work for him/her. Here is his/her details:\n"
                                                +"Name: "+nameOfSender+"\n"+
                                                "Phone Number: "+phone+"\n"+
                                                "Email: "+email+"\n"+
                                                /*"Blood Group: "+blood+"\n"+*/
                                                "Kindly Reach out to him/her. Thank you!"
                                                /*+"BLOOD DONATION APP, DONATE BLOOD, SAVE LIFE!"*/;
                                        JavaMailAPi javaMailAPi = new JavaMailAPi(context, mEmail, mSubject ,mMessage);
                                        javaMailAPi.execute();

                                        DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference("emails")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        senderRef.child(idOfTheReciever).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful())
                                                {
                                                    DatabaseReference recieverRef = FirebaseDatabase.getInstance().getReference("emails")
                                                            .child(idOfTheReciever);
                                                    recieverRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);

                                                    addNotifications(idOfTheReciever, FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                }
                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView profileimages;
        public TextView usernames, userphones, userlocations, usertypes, useremails, userinterest;
        AppCompatButton emailBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileimages = itemView.findViewById(R.id.userprofileimage);
            useremails = itemView.findViewById(R.id.useremail);
            userphones = itemView.findViewById(R.id.userphonenumber);
            usernames = itemView.findViewById(R.id.username);
            userlocations = itemView.findViewById(R.id.userlocation);
            usertypes = itemView.findViewById(R.id.usertype);
            userinterest = itemView.findViewById(R.id.userinterest);
            emailBtn = itemView.findViewById(R.id.emailNow);
        }
    }
    private void addNotifications(String receiverId, String senderId)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("notifications").child(receiverId);
        String date = DateFormat.getDateInstance().format(new Date());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("receiverId", receiverId);
        hashMap.put("senderId", senderId);
        hashMap.put("text", "Sent you an email, kindly check");
        hashMap.put("date", date);

        reference.push().setValue(hashMap);


    }
}