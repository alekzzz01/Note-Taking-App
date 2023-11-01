package com.example.infotech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;


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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Notes_Edit extends AppCompatActivity {

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


        // Set an onClickListener for the "Save" button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get a reference to the Firebase Database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                // Get the current user's UID from Firebase Authentication
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if (selectedNote == null) {
                    // Creating a new note
                    String title = titleInput.getText().toString();
                    String description = descriptionInput.getText().toString();
                    long timestamp = System.currentTimeMillis();

                    // Create a new Note SS
                    Note note = new Note(title, timestamp, description);

                    // Save the new note to the database under a unique key
                    DatabaseReference newNoteRef = databaseReference.child("users").child(userId).child("notes").push();
                    newNoteRef.setValue(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Notes_Edit.this, "Note saved to Firebase", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(Notes_Edit.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // Updating an existing note
                    String updatedTitle = titleInput.getText().toString();
                    String updatedDescription = descriptionInput.getText().toString();

                    // Create an updated Note object
                    Note updatedNote = new Note(selectedNote.getKey(), updatedTitle, selectedNote.getTimestamp(), updatedDescription);

                    // Update the note in the database using its unique key
                    DatabaseReference updatedNoteRef = databaseReference.child("users").child(userId).child("notes").child(selectedNote.getKey());


                    if (selectedNote != null) {
                        Log.d("Notes_Edit", "Selected note key: " + selectedNote.getKey());
                    }

                    updatedNoteRef.setValue(updatedNote).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Notes_Edit.this, "Note updated and saved to Firebase", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(Notes_Edit.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

}