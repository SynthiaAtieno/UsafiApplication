package com.example.usafiapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
/*import com.example.blood_donantion_app.adapter.UserAdapter;
import com.example.blood_donantion_app.model.Users;*/
import com.example.usafiapp.adapter.UserAdapter;
import com.example.usafiapp.model.Users;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView nav_fullname, nav_email, nav_type, nav_bloodgroup;
    private CircleImageView profile;
    private DatabaseReference userRef;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<Users> usersList;
    private UserAdapter userAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drwerLayout);
        toolbar = findViewById(R.id.toolbca);
        navigationView = findViewById(R.id.nav_view);
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressbar);

        usersList =new ArrayList<>();
        userAdapter = new UserAdapter(MainActivity.this,usersList);
        recyclerView.setAdapter(userAdapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type = snapshot.child("type").getValue().toString();

                if (type.equals("Employer"))
                {
                    readRecipient();
                }
                else
                {
                    readDonors();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        nav_fullname = navigationView.getHeaderView(0).findViewById(R.id.nav_userFullname);
        nav_email = navigationView.getHeaderView(0).findViewById(R.id.nav_userEmail);
        nav_type = navigationView.getHeaderView(0).findViewById(R.id.nav_userType);
        profile = navigationView.getHeaderView(0).findViewById(R.id.nav_user_image);
        //nav_bloodgroup = navigationView.getHeaderView(0).findViewById(R.id.nav_userBloodgroup);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String name = snapshot.child("name").getValue().toString();
                    nav_fullname.setText(name);

                    /*String bloodgroup = snapshot.child("bloodgroup").getValue().toString();
                    nav_bloodgroup.setText(bloodgroup);
*/
                    String email = snapshot.child("email").getValue().toString();
                    nav_email.setText(email);

                    String type = snapshot.child("type").getValue().toString();
                    nav_type.setText(type);
                    if (snapshot.hasChild("profilepictureuri"))
                    {
                        String imageurl = snapshot.child("profilepictureuri").getValue().toString();
                        Glide.with(getApplicationContext()).load(imageurl).into(profile);
                    }
                    else {
                        profile.setImageResource(R.drawable.profile);;
                    }
                    Menu menu = navigationView.getMenu();
                    if (type.equals("Worker"))
                    {
                        menu.findItem(R.id.sent_email).setTitle("Received Emails");
                        menu.findItem(R.id.notifications).setVisible(true);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Usafi App");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,toolbar
                ,R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }

    private void readDonors() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = reference.orderByChild("type").equalTo("Employer");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Users user = dataSnapshot.getValue(Users.class);
                    usersList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (usersList.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "No Employers found", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readRecipient() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = reference.orderByChild("type").equalTo("Worker");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Users user = dataSnapshot.getValue(Users.class);
                    usersList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (usersList.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "No Employers found", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
         /*   case R.id.aplus:
                Intent intent = new Intent(MainActivity.this, CategorySelected.class);
                intent.putExtra("group", "A+");
                startActivity(intent);
                break;

            case R.id.aminus:
                Intent intent1 = new Intent(MainActivity.this, CategorySelected.class);
                intent1.putExtra("group", "A-");
                startActivity(intent1);
                break;

            case R.id.bplus:
                Intent intent2 = new Intent(MainActivity.this, CategorySelected.class);
                intent2.putExtra("group", "B+");
                startActivity(intent2);
                break;

            case R.id.bminus:
                Intent intent3 = new Intent(MainActivity.this, CategorySelected.class);
                intent3.putExtra("group", "B-");
                startActivity(intent3);
                break;

            case R.id.abplus:
                Intent intent4 = new Intent(MainActivity.this, CategorySelected.class);
                intent4.putExtra("group", "AB+");
                startActivity(intent4);
                break;

            case R.id.abminus:
                Intent intent5 = new Intent(MainActivity.this, CategorySelected.class);
                intent5.putExtra("group", "AB-");
                startActivity(intent5);
                break;

            case R.id.oplus:
                Intent intent6 = new Intent(MainActivity.this, CategorySelected.class);
                intent6.putExtra("group", "O+");
                startActivity(intent6);
                break;

            case R.id.ominus:
                Intent intent7 = new Intent(MainActivity.this, CategorySelected.class);
                intent7.putExtra("group", "O-");
                startActivity(intent7);
                break;*/

            case R.id.compatible:
                Intent intent8 = new Intent(MainActivity.this, CategorySelected.class);
                intent8.putExtra("group", "me");
                startActivity(intent8);
                break;

            case R.id.pofile:
                startActivity(new Intent(MainActivity.this, Profile.class));
                finish();
                break;

            case R.id.sent_email:
                startActivity(new Intent(MainActivity.this, SentEmail.class));
                finish();
                break;

            case R.id.notifications:
                startActivity(new Intent(MainActivity.this, Notifications.class));
                finish();
                break;
            case R.id.about:
                startActivity(new Intent(MainActivity.this, About.class));
                finish();
                break;


            case R.id.help:
                startActivity(new Intent(MainActivity.this, Help.class));
                finish();
                break;
            case R.id.share:
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Your body here";
                String sharesub = "Your Subject here";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, sharesub);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Share Via"));
                break;

            case R.id.logout:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}