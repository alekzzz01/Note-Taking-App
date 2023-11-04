package com.example.infotech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Flashcardsview_All extends AppCompatActivity {





    ImageButton backbtn;

    List<FlashcardSet> flashcardSets = new ArrayList<>();

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcardsview_all);



        backbtn = findViewById(R.id.backButton);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity (Notes_Edit) to go back to the previous activity (Dashboard_Fragment)
                finish();
            }
        });







        // Initialize your RecyclerView and its adapter
        RecyclerView recyclerView = findViewById(R.id.flashcardrecyclerview);
        FlashcardSetAdapter adapter = new FlashcardSetAdapter(flashcardSets);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

// Query the Firebase database to fetch the flashcard sets and populate the `flashcardSets` list
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userID = firebaseUser.getUid();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("flashcards");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                flashcardSets.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String title = snapshot.getKey();
                    long flashcardCount = snapshot.getChildrenCount();
                    FlashcardSet flashcardSet = new FlashcardSet(title, flashcardCount);
                    flashcardSets.add(flashcardSet);
                }

                adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error, if any
            }
        });







    }
}