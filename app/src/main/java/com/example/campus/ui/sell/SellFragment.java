package com.example.campus.ui.sell;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.campus.databinding.FragmentSellBinding;
import com.example.campus.model;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class SellFragment extends Fragment {

    private FragmentSellBinding binding;
    private SellViewModel sellViewModel;

    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int PERMISSION_REQUEST_CODE = 100;

    private ImageView imageView;
    private Uri selectedImageUri; // Store the selected image URI

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sellViewModel = new ViewModelProvider(this).get(SellViewModel.class);
        binding = FragmentSellBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Button selectImageButton = binding.buttonSelectImage;
        imageView = binding.imageProductPreview;

        selectImageButton.setOnClickListener(v -> selectImage());

        Button submitButton = binding.buttonSubmit;
        submitButton.setOnClickListener(v -> {
            uploadProductToFirebase();
        });

        return root;
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose your product image");
        builder.setItems(options, (dialog, item) -> {
            if (item == 0) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
                } else {
                    openCamera();
                }
            } else if (item == 1) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
            } else if (item == 2) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE && data != null && data.getExtras() != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);
            }
        }
    }

    private void uploadProductToFirebase() {
        String productName = binding.editTextProductName.getText().toString().trim();
        String productPrice = binding.editTextProductPrice.getText().toString().trim();
        String productDetails = binding.editTextProductDetails.getText().toString().trim();

        if (productName.isEmpty() || productPrice.isEmpty() || productDetails.isEmpty() || selectedImageUri == null) {
            Toast.makeText(requireContext(), "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload the image to Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("product_images");
        String imageFileName = "product_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child(imageFileName);

        imageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully, get the download URL
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Create a model object with product details and image URL
                        model product = new model(productName, Double.parseDouble(productPrice), productDetails, uri.toString());


                        // Upload the product details to Firebase Realtime Database
                        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");
                        String productId = productsRef.push().getKey();

                        productsRef.child(Objects.requireNonNull(productId)).setValue(product)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(requireContext(), "Product uploaded successfully", Toast.LENGTH_SHORT).show();
                                    // Clear input fields after successful upload
                                    binding.editTextProductName.setText("");
                                    binding.editTextProductPrice.setText("");
                                    binding.editTextProductDetails.setText("");
                                    imageView.setImageDrawable(null); // Clear image preview
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), "Failed to upload product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("SellFragment", "Failed to upload product", e);
                                });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Failed to get image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("SellFragment", "Failed to get image URL", e);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("SellFragment", "Failed to upload image", e);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
