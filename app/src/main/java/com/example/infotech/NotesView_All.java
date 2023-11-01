package com.example.infotech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotesView_All extends AppCompatActivity {

    RecyclerView recyclerViewSecond; // Define the second RecyclerView
    NoteAdapterSecond noteAdapterSecond; // Create the second adapter
    List<Note> notesListForSecond; // Create a list for the second adapter

    private FirebaseAuth mAuth;
    private DatabaseReference notesReference; // Reference to the user's notes in Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_view_all);
        recyclerViewSecond = findViewById(R.id.recyclerview);
        mAuth = FirebaseAuth.getInstance();



        // Set up the second RecyclerView and adapter
        notesListForSecond = new ArrayList<>();
        noteAdapterSecond = new NoteAdapterSecond(notesListForSecond);
        recyclerViewSecond.setAdapter(noteAdapterSecond);
        recyclerViewSecond.setLayoutManager(new LinearLayoutManager(this));

        // Call the method to retrieve and display user's notes
        retrieveAndDisplayNotes();

        FirebaseApp.initializeApp(this); // Initialize Firebase

    }


    private void retrieveAndDisplayNotes() {
        // Get a reference to the Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get the current user's UID from Firebase Authentication
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get a reference to the "notes" node under the user's UID
        DatabaseReference userNotesRef = databaseReference.child("users").child(userId).child("notes");

        // Retrieve the notes from Firebase
        userNotesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Note> noteList = new ArrayList<>();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    Note note = noteSnapshot.getValue(Note.class);
                    if (note != null) {
                        noteList.add(note);
                    }
                }

                // Update the RecyclerView adapter with the list of notes
                noteAdapterSecond.setNotes(noteList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors or exceptions
            }
        });
    }
}