package com.example.infotech;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.bumptech.glide.Glide;


import java.util.Calendar;



public class Profile_Settings extends AppCompatActivity {

    ImageButton backbtn;

    TextView fullNameTextView, dobView, ageView, emailTextView;
    Button uploadImageButton, saveInfoBtn;
    ImageView profileImageView;
    FirebaseAuth auth;
    DatabaseReference userRef;
    StorageReference storageReference;
    Uri imageUri; // Stores the selected image URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        backbtn = findViewById(R.id.backButton);
        fullNameTextView = findViewById(R.id.fullNameTextView);
        emailTextView = findViewById(R.id.emailtextView);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        profileImageView = findViewById(R.id.profileImageView);
        dobView = findViewById(R.id.dobView);
        ageView = findViewById(R.id.ageView);
        saveInfoBtn = findViewById(R.id.saveInfoBtn);
        ImageButton editDateButton = findViewById(R.id.editDateButton);




        RadioGroup genderRadioGroup = findViewById(R.id.genderRadioGroup);







        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);

            // Now you can add the ValueEventListener
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve and populate the birthday
                        String dob = dataSnapshot.child("dob").getValue(String.class);
                        dobView.setText(dob);

                        // Calculate and populate the age
                        int age = calculateAge(dob);
                        ageView.setText(age + " years");

                        // Retrieve and populate the gender
                        String gender = dataSnapshot.child("gender").getValue(String.class);

                        if (gender != null) {
                            if (gender.equals("Male")) {
                                // Select the Male radio button
                                genderRadioGroup.check(R.id.MaleButton);
                            } else if (gender.equals("Female")) {
                                // Select the Female radio button
                                genderRadioGroup.check(R.id.FemaleButton);
                            }
                        }

                        // Fetch the image URL from the database
                        String imageUrl = dataSnapshot.child("profileImage").getValue(String.class);

                        if (imageUrl != null) {
                            // Load the profile image using Glide
                            Glide.with(Profile_Settings.this).load(imageUrl).into(profileImageView);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle any errors that occur during the database query
                }
            });
        } else {
            // User is not authenticated, redirect to the sign-in activity
            Intent signInIntent = new Intent(Profile_Settings.this, Signin_Page.class);
            startActivity(signInIntent);
        }




        saveInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if an image is uploaded
                if (imageUri != null) {
                    // Create a unique file name for the image
                    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String imageName = "profile_images/" + currentUserId + ".jpg";

                    StorageReference imageRef = storageReference.child(imageName);

                    imageRef.putFile(imageUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                // Image uploaded successfully, get the download URL
                                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();

                                    // Save the image URL to the user's database node
                                    userRef.child("profileImage").setValue(imageUrl);

                                    // Inform the user that the image was uploaded successfully
                                    Toast.makeText(Profile_Settings.this, "Image uploaded and saved successfully", Toast.LENGTH_SHORT).show();
                                });
                            })
                            .addOnFailureListener(e -> {
                                // Handle any errors that occur during image upload
                                Toast.makeText(Profile_Settings.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                            });
                }

                // Check if a date of birth (DOB) is selected
                String dob = dobView.getText().toString();
                if (!dob.isEmpty()) {
                    // Save the DOB to the user's database node
                    userRef.child("dob").setValue(dob);

                    // Calculate the age based on the DOB
                    int age = calculateAge(dob);

                    // Update the TextView with the calculated age
                    ageView.setText(age + " years");
                }

                // Get the selected gender from the RadioGroup
                int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
                String gender = "";
                if (selectedGenderId == R.id.MaleButton) {
                    gender = "Male";
                } else if (selectedGenderId == R.id.FemaleButton) {
                    gender = "Female";
                }

                // Save the gender to the user's database node
                userRef.child("gender").setValue(gender);

                // Inform the user that the information was saved successfully
                Toast.makeText(Profile_Settings.this, "Profile information saved successfully", Toast.LENGTH_SHORT).show();
            }
        });



        editDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(); // Call the method to show the date picker
            }
        });


        // Set an OnClickListener for the image upload button
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create an intent to pick an image from the gallery
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Get the currently logged-in user's UID
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Reference to the user's data in the Firebase database
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the first name, middle name, and last name from dataSnapshot
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String middleName = dataSnapshot.child("middleName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);

                    // Convert the middleName to uppercase
                    if (middleName != null) {
                        middleName = middleName.toUpperCase();
                    }

                    // Combine the three name parts into a full name
                    String fullName = firstName + " " + middleName + "." + " " + lastName;

                    // Set the full name and email in the TextViews
                    emailTextView.setText(email);
                    fullNameTextView.setText(fullName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur during the database query
            }
        });
    }




    // Override onActivityResult to handle the image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);

            // Upload the image to Firebase Storage and save the URL to the user's database
            uploadImageToFirebaseStorage();

        }
    }


    private void uploadImageToFirebaseStorage() {
        if (imageUri != null) {
            // Create a unique file name for the image
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            String imageName = "profile_images/" + currentUserId + ".jpg";


            // Reference to the user's data in the Firebase database
            userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);

            StorageReference imageRef = storageReference.child(imageName);

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully, get the download URL
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();

                                    // Save the image URL to the user's database node
                                    userRef.child("profileImage").setValue(imageUrl);

                                    // Inform the user that the image was uploaded successfully
                                    Toast.makeText(Profile_Settings.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    // Handle any errors that occur when getting the download URL
                                    Toast.makeText(Profile_Settings.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        // Handle any errors that occur during image upload
                        Toast.makeText(Profile_Settings.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    public void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Update the TextView with the selected date
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                // Update your TextView with the selected date
                dobView.setText(selectedDate);
            }
        };

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        // Show the date picker dialog
        datePickerDialog.show();
    }

    // Calculate the age based on the DOB
// Modify the calculateAge method to accept the date of birth as a parameter
    private int calculateAge(String dob) {
        if (dob != null) {
            // Split the date of birth into day, month, and year
            String[] dateParts = dob.split("/");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            // Get the current date
            Calendar currentDate = Calendar.getInstance();
            int currentYear = currentDate.get(Calendar.YEAR);
            int currentMonth = currentDate.get(Calendar.MONTH) + 1; // Month is zero-based
            int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

            // Calculate the age
            int age = currentYear - year;

            // Adjust age if the birthday has not occurred this year yet
            if (currentMonth < month || (currentMonth == month && currentDay < day)) {
                age--;
            }

            return age;
        } else {
            // Show a message to the user indicating they need to input their date of birth

            return 0; // Return a default value, or handle this case as needed
        }
    }
}