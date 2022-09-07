package com.example.usafiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    ImageView logo;
    TextView title, slogan;
    Animation top_anim, bottom_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logo);
        title = findViewById(R.id.title);
       // slogan = findViewById(R.id.slogan);

        top_anim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom_anim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        logo.setAnimation(top_anim);
        title.setAnimation(bottom_anim);
        //slogan.setAnimation(bottom_anim);

        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();


        int Splash_screen = 4800;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*Intent intent = new Intent(SplashActivity.this, Login.class);
                startActivity(intent);
                finish();*/

                if (currentUser != null){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(SplashActivity.this, Login.class));
                    finish();
                }
            }
        }, Splash_screen);

    }
}