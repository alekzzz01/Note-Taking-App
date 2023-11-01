package com.example.infotech;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Profile_Fragment extends Fragment {
    private TextView fullNameTextView;
    private DatabaseReference userDatabaseRef;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);
        fullNameTextView = view.findViewById(R.id.etFullName);

        // Get the current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // Initialize a reference to the user's data in the Firebase Realtime Database
            userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());

            // Add a ValueEventListener to fetch and display the full name
            userDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String lastName = dataSnapshot.child("lastName").getValue(String.class);
                        String middleName = dataSnapshot.child("middleName").getValue(String.class);

                        // Create the full name by combining first, middle, and last names
                        String fullName = "";
                        if (firstName != null) {
                            fullName += firstName;
                        }
                        if (middleName != null) {
                            fullName += " " + middleName;
                        }
                        if (lastName != null) {
                            fullName += " " + lastName;
                        }

                        fullNameTextView.setText("Full Name: " + fullName);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle any errors here
                }
            });
        }

        return view;
    }
}