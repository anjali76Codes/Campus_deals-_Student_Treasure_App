package com.example.campus;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class Chat extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText messageEditText;
    private Button sendButton;
    private ChatAdapter chatAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize RecyclerView, EditText, and Button
        recyclerView = findViewById(R.id.recyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(); // You need to create ChatAdapter class
        recyclerView.setAdapter(chatAdapter);

        // Initialize DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("chats");

        // Set OnClickListener for the Send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the message from EditText
                String message = messageEditText.getText().toString().trim();

                // If the message is not empty, add it to the RecyclerView and Firebase database
                if (!message.isEmpty()) {
                    // Get sender ID (You might want to replace "dummySenderId" with the actual sender's ID)
                    String senderId = "dummySenderId";

                    // Create a new ChatModel object
                    ChatModel chatModel = new ChatModel(message, senderId, System.currentTimeMillis());

                    // Push the chatModel to Firebase database
                    databaseReference.push().setValue(chatModel);

                    // Add the message to RecyclerView
                    chatAdapter.addMessage(chatModel);

                    // Clear EditText after sending message
                    messageEditText.setText("");

                    // Scroll to the bottom of the RecyclerView
                    recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                }
            }
        });
    }
}
