package com.example.campus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class splash extends AppCompatActivity {
    ImageView logo;
    TextView text;
    Animation topAnim, bottomAnim;
    FirebaseAuth auth;
    SharedPreferences sharedPreferences;
    boolean isFirstTime;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        text=findViewById(R.id.logoNameImg);
        auth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        text.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if it's the user's first time
                if (isFirstTime) {
                    // User is launching the app for the first time, navigate to registration
                    Intent intent = new Intent(splash.this, registration.class);
                    startActivity(intent);
                } else {
                    // User is not launching the app for the first time
                    if (auth.getCurrentUser() != null) {
                        // User is logged in, navigate to MainActivity
                        Intent intent = new Intent(splash.this, registration.class);
                        startActivity(intent);
                    } else {
                        // User is not logged in, navigate to login screen
                        Intent intent = new Intent(splash.this, login.class);
                        startActivity(intent);
                    }
                }
                finish(); // Finish the splash activity
            }
        }, 4000);

        // Mark isFirstTime as false after the first launch
        if (isFirstTime) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();
        }
    }
}

