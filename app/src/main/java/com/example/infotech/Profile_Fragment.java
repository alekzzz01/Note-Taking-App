package com.example.infotech;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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


    ImageView profileImageView;

    private ImageButton profileButton;


    private ImageButton developerButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);
        fullNameTextView = view.findViewById(R.id.etFullName);
        profileButton = view.findViewById(R.id.profileSettings);
        developerButton = view.findViewById(R.id.developerPage);
        profileImageView = view.findViewById(R.id.profileImageView);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Profile_Settings.class);
                startActivity(intent);
            }
        });


        developerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Developer_Page.class);
                startActivity(intent);
            }
        });



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

                        // Create the full name by combining first name, middle name initial, and last name
                        String fullName = "";
                        if (firstName != null) {
                            fullName += firstName;
                        }
                        if (middleName != null && !middleName.isEmpty()) {
                            fullName += " " + middleName.charAt(0) + ".";
                        }
                        if (lastName != null) {
                            fullName += " " + lastName;
                        }

                        // Fetch the image URL from the database
                        String imageUrl = dataSnapshot.child("profileImage").getValue(String.class);

                        if (imageUrl != null) {
                            // Load the profile image using Glide
                            Glide.with(Profile_Fragment.this).load(imageUrl).into(profileImageView);
                        }

                        fullNameTextView.setText(fullName);
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
