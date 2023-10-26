package com.example.infotech;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//import io.realm.Realm;


public class Dashboard_Fragment extends Fragment {
    ImageButton newNoteButton;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter; // Custom RecyclerView adapter for notes

    ProgressBar PB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard_, container, false);

        recyclerView = v.findViewById(R.id.recyclerview);
        newNoteButton = v.findViewById(R.id.addnewnotebtn);

        // Set up the RecyclerView and adapter
        noteAdapter = new NoteAdapter(new ArrayList<>()); // Initialize with an empty list
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(noteAdapter);

        // In Dashboard_Fragment
        newNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to Notes_Edit and pass an extra parameter
                Intent intent = new Intent(getActivity(), Notes_Edit.class);
                intent.putExtra("isNewNote", true);
                startActivity(intent);

            }
        });

        // Retrieve and display notes
        retrieveAndDisplayNotes();

        return v;
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
                noteAdapter.setNotes(noteList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors or exceptions
            }
        });

    }

}



