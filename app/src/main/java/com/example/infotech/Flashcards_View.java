package com.example.infotech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Flashcards_View extends AppCompatActivity {

    private static final String TAG = "Flashcards_View"; // Add this line


    ImageButton backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards_view);







        backbtn = findViewById(R.id.backButton);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity (Notes_Edit) to go back to the previous activity (Dashboard_Fragment)
                finish();
            }
        });

        // Retrieve the flashcardSet data from the Intent
        Intent intent = getIntent();
        if (intent != null) {
            FlashcardSet flashcardSet = intent.getParcelableExtra("flashcardSet");

            if (flashcardSet != null) {
                // Now you can access the data in the flashcardSet object
                String title = flashcardSet.getTitle();

                // Find the TextView in your layout by its ID
                TextView titleTextView = findViewById(R.id.title); // Replace 'titleTextView' with your TextView ID

                // Set the text of the TextView to the title
                titleTextView.setText(title);


            }
        }


        // Initialize RecyclerView with a horizontal LinearLayoutManager
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Retrieve flashcards data from Firebase and populate the list
        List<Flashcard> flashcards = new ArrayList<>();

        // Replace the following code with your Firebase data retrieval logic
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userID = currentUser.getUid();
            DatabaseReference  flashcardsReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("flashcards");

            flashcardsReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange called");

                    for (DataSnapshot titleSnapshot : dataSnapshot.getChildren()) {
                        String title = titleSnapshot.getKey(); // Get the title of the flashcard set

                        for (DataSnapshot flashcardSnapshot : titleSnapshot.getChildren()) {
                            String flashcardID = flashcardSnapshot.getKey(); // Get the flashcard ID


                            String question = flashcardSnapshot.child("Questions").getValue(String.class);
                            String answer = flashcardSnapshot.child("Answers").getValue(String.class);

                            Flashcard flashcard = new Flashcard(question, answer);
                            flashcards.add(flashcard);

                            // Add logging to check the question and answer for each flashcard
                            Log.d(TAG, "Flashcard ID: " + flashcardID);
                            Log.d(TAG, "Title: " + title);
                            Log.d(TAG, "Question: " + question);
                            Log.d(TAG, "Answer: " + answer);
                        }
                    }
                    // Create an adapter and set it to the RecyclerView
                    FlashcardSetAdapterSecond adapter = new FlashcardSetAdapterSecond(flashcards);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error, if any
                    Log.e(TAG, "Firebase Database Error: " + databaseError.getMessage());
                }
            });

        }


    }

}