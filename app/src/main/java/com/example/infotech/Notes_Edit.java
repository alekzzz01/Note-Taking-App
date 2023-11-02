package com.example.infotech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;


import android.app.ProgressDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Notes_Edit extends AppCompatActivity {
    private boolean isButtonClicked = false;

    TextView dateTimeTextView;

    Button saveButton;
    EditText titleInput, descriptionInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_edit);

        ImageButton settingsButton = findViewById(R.id.settingsButton);
        ImageButton backbutton = findViewById(R.id.backButton);

        dateTimeTextView = findViewById(R.id.date_time);

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        String formattedDate = dateFormat.format(date);
        dateTimeTextView.setText(formattedDate);

        saveButton = findViewById(R.id.savebtn);
        titleInput = findViewById(R.id.titleinput);
        descriptionInput = findViewById(R.id.descriptioninput);


        // Retrieve the selected note from the intent's extras
        Note selectedNote = getIntent().getParcelableExtra("selectedNote");

        if (selectedNote != null) {
            // Populate EditText fields with selected note's data
            titleInput.setText(selectedNote.getTitle());
            descriptionInput.setText(selectedNote.getDescription());
        }


        // Set an onClickListener for the ImageButton
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a PopupMenu
                PopupMenu popupMenu = new PopupMenu(Notes_Edit.this, settingsButton);
                popupMenu.getMenuInflater().inflate(R.menu.settings_menu, popupMenu.getMenu());

                // Set item click listeners
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Handle item click actions here
                        if (item.getItemId() == R.id.move) {
                            // Do something for Item 1


                        } else if (item.getItemId() == R.id.ChangeColor) {
                            // Do something for Item 2


                        } else if (item.getItemId() == R.id.Delete) {
                            // Do something for Item 3


                        }
                        return true;
                    }
                });

                // Show the popup menu
                popupMenu.show();
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity (Notes_Edit) to go back to the previous activity (Dashboard_Fragment)
                finish();
            }
        });


        // Get a reference to the Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

// Get the current user's UID from Firebase Authentication
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

// Get a reference to the "notes" node under the user's UID
        DatabaseReference userNotesRef = databaseReference.child("users").child(userId).child("notes");

// Use push() to automatically generate a unique key for the new note
        DatabaseReference newNoteRef = userNotesRef.push();

// Get the title, time, and description from the EditText fields
        String title = titleInput.getText().toString();
        String description = descriptionInput.getText().toString();
        long timestamp = System.currentTimeMillis();

// Create a Note object
        Note note = new Note(title, timestamp, description);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("Notes_Edit.java", "User is authenticated");
            userId = currentUser.getUid();
            Log.d("Notes_Edit.java", "User ID: " + userId);

            // Set an onClickListener for the "Save" button
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Notes_Edit.java", "saveButton onClick");
                    saveButton.setEnabled(false); // Disable the button to prevent multiple clicks
                    ProgressDialog progressDialog = new ProgressDialog(Notes_Edit.this);
                    progressDialog.setMessage("Saving...");
                    progressDialog.show();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    if (userId == null || userId.isEmpty()) {
                        Log.e("Notes_Edit.java", "User ID is null or empty");
                        saveButton.setEnabled(true); // Re-enable the button
                        progressDialog.dismiss(); // Dismiss the dialog
                        return;
                    }

                    Log.d("Notes_Edit.java", "selectedNote: " + selectedNote);

                    String newTitle = titleInput.getText().toString();
                    String newDescription = descriptionInput.getText().toString();
                    long newTimestamp = System.currentTimeMillis();

                    if (selectedNote == null) {
                        // Create a new Note object if selectedNote is null
                        Note note = new Note(newTitle, newTimestamp, newDescription);

                        DatabaseReference newNoteRef = databaseReference.child("users").child(userId).child("notes").push();
                        newNoteRef.setValue(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Notes_Edit.this, "Note saved to Firebase", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Log.e("Notes_Edit.java", "Database operation failed: " + task.getException().getMessage());
                                    Toast.makeText(Notes_Edit.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                saveButton.setEnabled(true); // Re-enable the button
                                progressDialog.dismiss(); // Dismiss the dialog
                                Log.d("Notes_Edit.java", "Button enabled and dialog dismissed");
                            }
                        });
                    } else {
                        // Update the selected note's title, description, and timestamp
                        if (selectedNote.getKey() != null) {
                            selectedNote.setTitle(newTitle);
                            selectedNote.setDescription(newDescription);
                            selectedNote.setTime(newTimestamp);

                            DatabaseReference selectedNoteRef = databaseReference.child("users").child(userId).child("notes").child(selectedNote.getKey());
                            selectedNoteRef.setValue(selectedNote).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Notes_Edit.this, "Note updated in Firebase", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Log.e("Notes_Edit.java", "Database operation failed: " + task.getException().getMessage());
                                        Toast.makeText(Notes_Edit.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    saveButton.setEnabled(true); // Re-enable the button
                                    progressDialog.dismiss(); // Dismiss the dialog
                                    Log.d("Notes_Edit.java", "Button enabled and dialog dismissed");
                                }
                            });
                        } else {
                            Log.e("Notes_Edit.java", "selectedNote key is null");
                            saveButton.setEnabled(true);
                            progressDialog.dismiss();
                            finish();
                            Log.d("Notes_Edit.java", "Button enabled and dialog dismissed");
                        }
                    }
                }
            });

        }
    }
}