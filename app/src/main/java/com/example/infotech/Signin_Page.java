package com.example.infotech;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.infotech.databinding.SigninBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signin_Page extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private SigninBinding binding;

    private TextView signupView;
    FirebaseAuth auth;
    EditText email, password;
    Button signin;
    Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        email = findViewById(R.id.logemail);
        password = findViewById(R.id.logpassword);
        signin = findViewById(R.id.signinbtn);

        auth = FirebaseAuth.getInstance();






        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emaill = email.getText().toString();
                String pw = password.getText().toString();


                if (TextUtils.isEmpty(emaill)) {
                    email.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(pw)) {
                    password.setError("Password is required");
                    return;
                }

                if (password.length() < 7) {
                    password.setError("Password must be 8 characters long");
                    return;
                }


                auth.signInWithEmailAndPassword(emaill, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                long creationTimestamp = user.getMetadata().getCreationTimestamp();
                                long lastSignInTimestamp = user.getMetadata().getLastSignInTimestamp();

                                if (lastSignInTimestamp - creationTimestamp <= 24 * 60 * 60 * 1000) {
                                    // The user signed up within the last 24 hours, show GetStarted_Page
                                    Toast.makeText(Signin_Page.this, "New user, going to Get Started Page", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(Signin_Page.this, GetStarted_Page.class);
                                    startActivity(intent);
                                } else {
                                    // The user is not new or signed up more than 24 hours ago, go to the dashboard (MainActivity)
                                    Toast.makeText(Signin_Page.this, "Logged in Successfully, going to Dashboard", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(Signin_Page.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                finish();
                            }
                        } else {
                            Toast.makeText(Signin_Page.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


        signupView = findViewById(R.id.Signupview);

        // Add a click listener to the signupView
        signupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to Signup_Page
                Intent intent = new Intent(Signin_Page.this, Register_Page.class);
                startActivity(intent);
            }
        });








    }



}
