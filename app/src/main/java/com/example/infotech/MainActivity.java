package com.example.infotech;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {


    private TextView headerUsernameTextView;
    private TextView headerNameTextView;

    FloatingActionButton fab;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and show the custom dialog
                Dialog customDialog = new Dialog(MainActivity.this);
                customDialog.setContentView(R.layout.fab_dialoglayout);

                // Find the button inside the dialog layout
                Button addNoteBtn = customDialog.findViewById(R.id.addNotebtn);


                addNoteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Open the 'NotesView_All' activity from the dialog
                        Intent intent = new Intent(MainActivity.this, Notes_Edit.class);
                        startActivity(intent);
                        customDialog.dismiss(); // Close the dialog
                    }
                });



                customDialog.setTitle("Custom Dialog Title");
                customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                customDialog.show();
            }
        });


        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        findViewById(R.id.menuButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        // Set up navigation drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(navigationView, navController);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Get references to the TextViews in the header layout
        View headerView = navigationView.getHeaderView(0);
        headerUsernameTextView = headerView.findViewById(R.id.header_username);
        headerNameTextView = headerView.findViewById(R.id.header_name);

        // Retrieve user information and update the header
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Update the username with the email
            headerUsernameTextView.setText(user.getEmail());

            // Update the name (first name + middle initial + last name) in the header
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(user.getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String middleName = dataSnapshot.child("middleName").getValue(String.class);
                        String lastName = dataSnapshot.child("lastName").getValue(String.class);

                        String name = firstName;

                        if (middleName != null && !middleName.isEmpty()) {
                            name += " " + middleName.charAt(0) + ".";
                        }

                        if (lastName != null && !lastName.isEmpty()) {
                            name += " " + lastName;
                        }

                        headerNameTextView.setText(name);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle any errors here
                }
            });

        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.nav_About) {
                    navController.navigate(R.id.action_dashboard_to_about); // Handle navigation to the "About" fragment
                } else if (itemId == R.id.nav_home) {
                    navController.navigate(R.id.action_about_to_dashboard); // Handle navigation to the "Home" fragment
                } else if (itemId == R.id.nav_logout) {
                    showLogoutConfirmationDialog(); // Show logout confirmation dialog
                }

                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
                return true;
            }
        });
    }

    // Function to show the confirmation dialog
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle logout
                        FirebaseAuth.getInstance().signOut();
                        redirectToSignInPage();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // Function to redirect to the Sign-In page after logout
    private void redirectToSignInPage() {
        Intent intent = new Intent(this, Signin_Page.class); // Replace with your SignInActivity
        startActivity(intent);
        finish(); // Close the current activity
    }
}