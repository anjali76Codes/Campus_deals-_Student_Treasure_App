//package com.example.chatting;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;
//
//import com.example.chatting.R;
//import com.example.chatting.databinding.ActivityNavigationBinding;
//import com.example.chatting.login;
//import com.google.android.material.navigation.NavigationView;
//import com.google.android.material.snackbar.Snackbar;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class navigation extends AppCompatActivity {
//
//    private AppBarConfiguration mAppBarConfiguration;
//    private Uri imageUri; // Add this variable to store the image URI
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        ActivityNavigationBinding binding = ActivityNavigationBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // Retrieve the image URI passed from the registration activity
//        if (getIntent().hasExtra("userImageUri")) {
//            String imageUriString = getIntent().getStringExtra("userImageUri");
//            imageUri = Uri.parse(imageUriString);
//        }
//
//        setSupportActionBar(binding.appBarNavigation.toolbar);
//
//        DrawerLayout drawer = binding.drawerLayout;
//        NavigationView navigationView = binding.navView;
//
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_about, R.id.nav_buy, R.id.nav_sell, R.id.nav_cart, R.id.nav_faq)
//                .setOpenableLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
//
//        // Set the image URI to the profile image view
//        if (imageUri != null) {
//            View headerView = navigationView.getHeaderView(0);
//            CircleImageView profileImageView = headerView.findViewById(R.id.profile);
//            profileImageView.setImageURI(imageUri);
//        }
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.navigation, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.action_logout) {
//            // Perform logout
//            logout();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void logout() {
//        // Add your logout logic here, such as clearing session data, etc.
//        // For example, you can start the login activity and finish the current activity.
//        Intent intent = new Intent(this, login.class);
//        startActivity(intent);
//        finish(); // Close the current activity
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
//}


package com.example.campus;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.campus.databinding.ActivityNavigationBinding;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class navigation extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Uri imageUri; // Add this variable to store the image URI
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityNavigationBinding binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String imageUriString = intent.getStringExtra("userImageUri");
        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString);
        }

        setSupportActionBar(binding.appBarNavigation.toolbar);
//        FloatingActionButton fab = binding.appBarNavigation.fab;
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_about, R.id.nav_buy, R.id.nav_sell,  R.id.nav_faq)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextView = headerView.findViewById(R.id.profileTextView);
        CircleImageView profileImageView = headerView.findViewById(R.id.profile);
        if (username != null) {
            usernameTextView.setText(username);
        }

        if (imageUri != null) {
            // Use Glide to load the profile image efficiently
            Glide.with(this).load(imageUri).into(profileImageView);
        }

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the popup_profile layout
                View popupView = getLayoutInflater().inflate(R.layout.popup_profile, null);

                // Create a PopupWindow object
                popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                // Set a background drawable with rounded corners
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                // Set an elevation value for the PopupWindow
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    popupWindow.setElevation(20);
                }

                // Set a transition animation for the PopupWindow
                popupWindow.setAnimationStyle(R.style.PopupAnimation);

                // Set a focusable property to true to receive touch events
                popupWindow.setFocusable(true);

                // Show the PopupWindow at a specified location on the screen
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            // Perform logout
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        // Add your logout logic here, such as clearing session data, etc.
        // For example, you can start the login activity and finish the current activity.
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}