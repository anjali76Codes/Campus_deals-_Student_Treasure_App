package com.example.campus.ui.faq;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.campus.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

public class FaqFragment extends Fragment {

    private EditText questionEditText;
    private LinearLayout containerQuestions;
    private String questionText;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_faq, container, false);

        questionEditText = root.findViewById(R.id.FaqTextE);
        Button submitButton = root.findViewById(R.id.button_submit);
        containerQuestions = root.findViewById(R.id.container);

        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPrefs.getString("username", "Default Username");


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(question)) {
                    addQuestionToContainer(question,username,true); // Assuming the current user's questions are displayed on the right
                    questionEditText.getText().clear();
                } else {
                    Toast.makeText(getActivity(), "Please enter a question", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Add listener to Firebase database reference
        FirebaseDatabase.getInstance().getReference().child("faq").addChildEventListener(childEventListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        // Remove listener when the fragment is stopped
        FirebaseDatabase.getInstance().getReference().child("faq").removeEventListener(childEventListener);
    }

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
            // Retrieve the new question from the database
            String questionText = dataSnapshot.child("question").getValue(String.class);
            if (!TextUtils.isEmpty(questionText)) {
                // Add the new question to the UI
                String username = null;
                addQuestionToContainer(questionText,username, true); // Pass false as isCurrentUser
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }

        // Other overridden methods of ChildEventListener
        // onChildChanged(), onChildRemoved(), onChildMoved(), onCancelled()
    };

    private void saveQuestionToFirebase(String questionText) {
        saveQuestionToFirebase(null);
    }



    private void addQuestionToContainer(String questionText, String username, boolean isCurrentUser) {
        // Create a card view to hold the question
        CardView cardView = new CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.card_margin_bottom));
        cardView.setLayoutParams(cardParams);

        // Set card view elevation and corner radius
        cardView.setCardElevation(getResources().getDimensionPixelSize(R.dimen.card_elevation));
        cardView.setRadius(getResources().getDimensionPixelSize(R.dimen.card_corner_radius));

        // Set card view background color based on user
        int backgroundColor = isCurrentUser ? R.color.user_current : R.color.user_other;
        cardView.setCardBackgroundColor(getResources().getColor(backgroundColor));

        // Create a text view for the question
        TextView questionTextView = new TextView(requireContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(
                getResources().getDimensionPixelSize(R.dimen.text_margin_horizontal),
                getResources().getDimensionPixelSize(R.dimen.text_margin_vertical),
                getResources().getDimensionPixelSize(R.dimen.text_margin_horizontal),
                getResources().getDimensionPixelSize(R.dimen.text_margin_vertical));
        questionTextView.setLayoutParams(params);
        questionTextView.setText(questionText);

        // Add padding between username and question text
        questionTextView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.text_padding_top), 0, 0);

        // Add the text view to the card view
        cardView.addView(questionTextView);

        // Add username above the question text
        TextView usernameTextView = new TextView(requireContext());
        LinearLayout.LayoutParams usernameParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        usernameParams.setMargins(
                getResources().getDimensionPixelSize(R.dimen.text_margin_horizontal),
                0,
                getResources().getDimensionPixelSize(R.dimen.text_margin_horizontal),
                0);
        usernameTextView.setLayoutParams(usernameParams);
        usernameTextView.setText(username);
        usernameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14); // Adjust font size as needed

        // Add padding between username and question text
        usernameTextView.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.text_padding_bottom));

        cardView.addView(usernameTextView, 0); // Add usernameTextView at index 0 to display above questionTextView

        // Add the card view to the container
        containerQuestions.addView(cardView);

        // Save the question to Firebase
        saveQuestionToFirebase(questionText);
    }
    }
