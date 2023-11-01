package com.example.infotech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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

    ImageButton backbtn;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    List<Note> notesList;

    private FirebaseAuth mAuth; // Firebase Authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_view_all);
        recyclerView = findViewById(R.id.recyclerview);
        backbtn = findViewById(R.id.backButton);

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Authentication

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        notesList = new ArrayList<>();
        noteAdapter = new NoteAdapter(notesList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(noteAdapter);

        // Fetch notes from Firebase for the current user
        fetchUserNotes();
    }

    private void fetchUserNotes() {
        FirebaseUser currentUser = mAuth.getCurrentUser(); // Get the current authenticated user
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            // Reference to the current user's notes
            DatabaseReference userNotesRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(currentUserId)
                    .child("notes");

            userNotesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    notesList.clear(); // Clear the existing notes

                    for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                        Note note = noteSnapshot.getValue(Note.class);
                        notesList.add(note);
                    }

                    noteAdapter.setNotes(notesList); // Update the adapter with the fetched notes
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(NotesView_All.this, "Failed to fetch notes from Firebase", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
