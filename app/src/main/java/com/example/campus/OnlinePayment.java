package com.example.campus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class OnlinePayment extends AppCompatActivity {

    private EditText editTextAmount;
    private EditText editTextCardNumber;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_payment);

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextCardNumber = findViewById(R.id.editTextCardNumber);
        Button payButton = findViewById(R.id.buttonPay);

        // Retrieve product ID from intent
        productId = getIntent().getStringExtra("productId");

        // Pay button click listener
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateDetails()) {
                    // Simulate payment success
                    Toast.makeText(OnlinePayment.this, "Payment successful", Toast.LENGTH_SHORT).show();
                    // Pass back the product ID to indicate successful payment
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("productId", productId);
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Close the OnlinePayment activity
                } else {
                    Toast.makeText(OnlinePayment.this, "Please fill all details correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Validate payment details
    private boolean validateDetails() {
        String amount = editTextAmount.getText().toString().trim();
        String cardNumber = editTextCardNumber.getText().toString().trim();

        if (amount.isEmpty() || cardNumber.isEmpty()) {
            return false;
        }

        if (cardNumber.length() != 15) {
            editTextCardNumber.setError("Card number must be 15 digits");
            return false;
        }

        return true;
    }
}
