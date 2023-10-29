package com.example.infotech;

import static android.app.ProgressDialog.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_Page extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase db;

    Button signin;
    EditText emaill, password, repass;


    private TextView signInView;

    Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        auth = FirebaseAuth.getInstance();
        signin = findViewById(R.id.signinbtn2);
        emaill = findViewById(R.id.etemail2);
        password = findViewById(R.id.etpassword2);
        repass = findViewById(R.id.etrepass2);

        signInView = findViewById(R.id.SigninView);

        // Add a click listener to the signupView
        signInView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to Signin_Page
                Intent intent = new Intent(Register_Page.this, Signin_Page.class);
                startActivity(intent);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emaill.getText().toString();
                String pw = password.getText().toString();
                String pw2 = repass.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pw) || TextUtils.isEmpty(pw2)) {
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {



                    auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                saveUserDataToFirebase(email, pw);
                                Toast.makeText(getApplicationContext(), "Register Successful", Toast.LENGTH_SHORT).show();

                                // Navigate to the Information_Page after successful registration
                                Intent intent = new Intent(Register_Page.this, Information_Page.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        private void saveUserDataToFirebase(String email, String pw) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userID = firebaseUser.getUid();

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                            databaseReference.child(userID).child("email").setValue(email);
                            databaseReference.child(userID).child("password").setValue(pw);
                        }
                    });
                }
            }
        });
    }
}