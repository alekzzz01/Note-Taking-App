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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signin_Page extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    FirebaseAuth auth;
    EditText email, password;
    Button signin;
    TextView newacc;
    Handler h = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.logemail);
        password = findViewById(R.id.logpassword);
        signin = findViewById(R.id.signinbtn2);
        newacc = findViewById(R.id.newaccount);
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
                            Toast.makeText(Signin_Page.this, "Logged in Successfuly", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(Signin_Page.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        newacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(Signin_Page.this, Register_Page.class);
                        startActivity(i);
                    }
                },0);
            }
        });
    }
}