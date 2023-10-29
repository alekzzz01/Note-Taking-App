package com.example.infotech;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Information_Page extends AppCompatActivity {
    private EditText etFirstName, etLastName, etMiddleName;
    private Button saveButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_page);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etMiddleName = findViewById(R.id.etMiddleName);
        saveButton = findViewById(R.id.signinbtn2);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = etFirstName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                String middleName = etMiddleName.getText().toString().trim();

                // Check if the fields are not empty
                if (firstName.isEmpty() || lastName.isEmpty()) {
                    Toast.makeText(Information_Page.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Save user information to Firebase database
                    String userID = user.getUid();
                    DatabaseReference userRef = databaseReference.child(userID);

                    userRef.child("firstName").setValue(firstName);
                    userRef.child("lastName").setValue(lastName);
                    userRef.child("middleName").setValue(middleName);

                    // Provide feedback to the user
                    Toast.makeText(Information_Page.this, "Information saved successfully", Toast.LENGTH_SHORT).show();


                    // Navigate to the Signin_Page
                    Intent intent = new Intent(Information_Page.this, Signin_Page.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
