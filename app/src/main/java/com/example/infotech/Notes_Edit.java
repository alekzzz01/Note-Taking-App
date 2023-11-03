package com.example.infotech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;


import android.app.ProgressDialog;
import android.content.DialogInterface;
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
    private boolean isEditing = false;

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
                        if (item.getItemId() == R.id.move) {
                            // Do something for Item 1
                        } else if (item.getItemId() == R.id.ChangeColor) {
                            // Do something for Item 2
                        } else if (item.getItemId() == R.id.Delete) {
                            // Handle the "Delete" option
                            if (selectedNote != null) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Notes_Edit.this);
                                builder.setTitle("Confirm Deletion");
                                builder.setMessage("Are you sure you want to delete this note?");

                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // User confirmed deletion
                                        // Get a reference to the Firebase Database
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        if (currentUser != null) {
                                            String userId = currentUser.getUid();

                                            if (userId != null) {
                                                // Delete the selected note
                                                DatabaseReference selectedNoteRef = databaseReference
                                                        .child("users")
                                                        .child(userId)
                                                        .child("notes")
                                                        .child(selectedNote.getKey());

                                                selectedNoteRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(Notes_Edit.this, "Note deleted from Firebase", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        } else {
                                                            Log.e("Notes_Edit.java", "Database operation failed: " + task.getException().getMessage());
                                                            Toast.makeText(Notes_Edit.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                        dialog.dismiss();
                                    }
                                });

                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // User canceled deletion, do nothing
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
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

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("Notes_Edit.java", "User is authenticated");
            String userId = currentUser.getUid();
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

                    if (selectedNote != null) {
                        // Check if the key is null and generate one if necessary
                        if (selectedNote.getKey() == null) {
                            DatabaseReference newNoteRef = databaseReference.child("users").child(userId).child("notes").push();
                            String noteKey = newNoteRef.getKey();
                            selectedNote.setKey(noteKey); // Set the key for the note
                        }

                        selectedNote.setTitle(newTitle);
                        selectedNote.setDescription(newDescription);
                        selectedNote.setTimestamp(newTimestamp);

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
                        // Save the note
                        Note note = new Note(newTitle, newTimestamp, newDescription);

                        DatabaseReference newNoteRef = databaseReference.child("users").child(userId).child("notes").push();
                        String noteKey = newNoteRef.getKey();
                        note.setKey(noteKey); // Set the key for the new note

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
                    }
                }
            });
        }
    }

}