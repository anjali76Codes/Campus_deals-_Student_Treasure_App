package com.example.campus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class registration extends AppCompatActivity {
    TextView loginbut;
    EditText rg_username,rg_email,rg_password1,rg_password2;
    Button rg_signUp;
    CircleImageView rg_profileImg;
    FirebaseAuth auth;
    Uri imageURI;
    String imageUri;
    String emailPattern =  "^[a-zA-Z0-9._%+-]+@vcet\\.edu\\.in$";
    //"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Establishing your account.");
        progressDialog.setCancelable(false);

        database=FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();
        loginbut=findViewById(R.id.loginbut);
        rg_username=findViewById(R.id.rgUsername);
        rg_email=findViewById(R.id.rgEmail);
        rg_password1=findViewById(R.id.rgPassword1);
        rg_password2=findViewById(R.id.rgPassword2);
        rg_profileImg=findViewById(R.id.profilerg0);
        rg_signUp=findViewById(R.id.signUpbutton);


        loginbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(registration.this, login.class);
                startActivity(intent);
                finish();
            }
        });


        rg_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namee=rg_username.getText().toString();
                String emaill=rg_email.getText().toString();
                String password1=rg_password1.getText().toString();
                String password2=rg_password2.getText().toString();
                String status="Hey I'm using this application.";

                if (TextUtils.isEmpty(namee) || TextUtils.isEmpty(emaill) || TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)){
                    progressDialog.dismiss();
                    Toast.makeText(registration.this,"Please Enter Valid Information.",Toast.LENGTH_SHORT).show();
                } else if (!emaill.matches(emailPattern)) {
                    progressDialog.dismiss();
                    rg_email.setError("Type a valid email.");
                } else if (password1.length()<6) {
                    progressDialog.dismiss();
                    rg_password1.setError("Password must be 6 characters or more.");
                } else if (!password1.equals(password2)) {
                    progressDialog.dismiss();
                    rg_password1.setError("Password doesn't match.");
                } else {
                    auth.createUserWithEmailAndPassword(emaill,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String id=task.getResult().getUser().getUid();
                                DatabaseReference reference=database.getReference().child("user").child(id);
                                StorageReference storageReference=storage.getReference().child("Upload").child(id);

                                if (imageURI!=null){
                                    storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageUri=uri.toString();
                                                        Users users=new Users(id,namee,emaill,password1,password2,imageUri);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    progressDialog.show();
                                                                    Intent intent=new Intent(registration.this, navigation.class); // Change to login class
                                                                    startActivity(intent);
                                                                    finish(); // Finish registration activity to prevent going back using back button
                                                                }else {
                                                                    Toast.makeText(registration.this,"Error in creating the user.",Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else {
                                    String status="Hey I'm using this application.";
                                    imageUri="https://firebasestorage.googleapis.com/v0/b/chatting-3970b.appspot.com/o/prof.jpg?alt=media&token=0acb2de0-5cff-487b-a295-d2f42533f0dc";
                                    Users users=new Users(id,namee,emaill,password1,imageUri,status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(registration.this, navigation.class); // Change to login class
                                                startActivity(intent);
                                                finish(); // Finish registration activity to prevent going back using back button
                                            } else {
                                                Toast.makeText(registration.this, "Error in creating the user.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


        rg_profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10){
            if (data!=null){
                imageURI=data.getData();
                rg_profileImg.setImageURI(imageURI);
            }
        }
    }
}
