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
    NoteAdapter noteAdapter;

    private FriendAdapter friendAdapter;

    RelativeLayout Viewlesson;






    ProgressBar PB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard_, container, false);

        recyclerView = v.findViewById(R.id.recyclerview);
        RecyclerView friendsRecyclerView = v.findViewById(R.id.friendRecyclerview);
        newNoteButton = v.findViewById(R.id.addnewnotebtn);

        TextView etFirstName = v.findViewById(R.id.etFirstName);

        Viewlesson = v.findViewById(R.id.lessonView);


        Viewlesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Lesson_View.class);
                startActivity(intent);
            }
        });



        // Set up the RecyclerView and adapter for friends
        friendAdapter = new FriendAdapter(new ArrayList<>());
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        friendsRecyclerView.setAdapter(friendAdapter);




        // Set up the RecyclerView and adapter
        noteAdapter = new NoteAdapter(new ArrayList<>()); // Initialize with an empty list
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(noteAdapter);




        newNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to Notes_Edit and pass an extra parameter
                Intent intent = new Intent(getActivity(), Notes_Edit.class);
                intent.putExtra("isNewNote", true);
                startActivity(intent);

            }
        });


        displayUserFirstName();


// Retrieve and display friends
        retrieveAndDisplayFriends();

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

    private void displayUserFirstName() {
        // Get the current user's UID from Firebase Authentication
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get a reference to the "firstName" field under the user's UID in the database
        DatabaseReference userFirstNameRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userId)
                .child("firstName");

        userFirstNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String firstName = dataSnapshot.getValue(String.class);
                if (firstName != null) {
                    // Set the user's first name in the TextView
                    TextView etFirstName = getView().findViewById(R.id.etFirstName); // Retrieve the TextView
                    etFirstName.setText("Hi, " + firstName + "!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors or exceptions
            }
        });
    }

    private void retrieveAndDisplayFriends() {
        // Get a reference to the Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get the current user's UID from Firebase Authentication
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get a reference to the "friends" node under the user's UID
        DatabaseReference userFriendsRef = databaseReference.child("users").child(userId).child("friends");

        // Retrieve the friend data from Firebase
        userFriendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Friend> friendsList = new ArrayList<>();
                for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                    String friendUserId = friendSnapshot.getKey();
                    Friend friend = new Friend(friendUserId, null, null); // Initialize with null for name and profileImageURL

                    // Retrieve the friend's data from the database (replace with the actual database structure)
                    DatabaseReference friendDataRef = databaseReference.child("users").child(friendUserId);
                    friendDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot friendDataSnapshot) {
                            if (friendDataSnapshot.exists()) {
                                // Retrieve the friend's first name, last name, and profile image URL
                                String friendFirstName = friendDataSnapshot.child("firstName").getValue(String.class);
                                String friendLastName = friendDataSnapshot.child("lastName").getValue(String.class);
                                String profileImageURL = friendDataSnapshot.child("profileImage").getValue(String.class);

                                // Combine the first letter of the last name with the first name
                                String friendName = friendFirstName + " " + friendLastName.charAt(0) + ".";

                                // Update the friend's name and profile image URL
                                friend.setName(friendName);
                                friend.setProfileImageURL(profileImageURL);

                                // Add the friend to the list
                                friendsList.add(friend);

                                // Update the RecyclerView adapter with the list of friends
                                friendAdapter.setData(friendsList);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle any errors or exceptions
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors or exceptions
            }
        });
    }



}




