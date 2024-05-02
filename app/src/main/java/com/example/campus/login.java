package com.example.campus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    TextView signUp;
    Button button;
    EditText email, password;
    FirebaseAuth auth;
    String emailPattern = "^[a-zA-Z0-9._%+-]+@vcet\\.edu\\.in$";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.Logbutton);
        email = findViewById(R.id.editTextLogEmail);
        password = findViewById(R.id.editTextLogPassword);
        signUp = findViewById(R.id.signUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, registration.class);
                startActivity(intent);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String pass = password.getText().toString();

                if (TextUtils.isEmpty(Email)) {
                    progressDialog.dismiss();
                    Toast.makeText(login.this, "Enter the Email.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pass)) {
                    progressDialog.dismiss();
                    Toast.makeText(login.this, "Enter the Password", Toast.LENGTH_SHORT).show();
                } else if (!Email.matches(emailPattern)) {
                    progressDialog.dismiss();
                    email.setError("Give Proper Email Address");
                } else if (pass.length() < 6) {
                    progressDialog.dismiss();
                    password.setError("Password must be more than 6 characters.");
                    Toast.makeText(login.this, "Password must be more than 6 characters.", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();

                    auth.signInWithEmailAndPassword(Email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // Inside the onComplete listener for signInWithEmailAndPassword
                            // Inside the onComplete listener for signInWithEmailAndPassword
                            if (task.isSuccessful()) {
                                String userId = auth.getCurrentUser().getUid();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user").child(userId);
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        // If user data exists, fetch username and image URI
                                        if (dataSnapshot.exists()) {
                                            progressDialog.dismiss();
                                            String username = dataSnapshot.child("username").getValue(String.class);
                                            String imageUri = dataSnapshot.child("imageUri").getValue(String.class);

                                            // Pass username and image URI to the navigation activity
                                            Intent intent = new Intent(login.this, navigation.class);
                                            intent.putExtra("username", username);
                                            intent.putExtra("userImageUri", imageUri);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            // If user data doesn't exist, display an error message
                                            progressDialog.dismiss();
                                            Toast.makeText(login.this, "You need to sign up first.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle error
                                        progressDialog.dismiss();
                                        Toast.makeText(login.this, "Error retrieving user data.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                // If sign-in fails, display an error message
                                progressDialog.dismiss();
                                Toast.makeText(login.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        }); // Add this closing parenthesis
                }
            }
        });
    }
}
