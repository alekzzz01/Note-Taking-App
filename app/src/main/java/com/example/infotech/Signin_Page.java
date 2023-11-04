package com.example.infotech;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.infotech.databinding.SigninBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signin_Page extends AppCompatActivity {

    // Define a constant for SharedPreferences
    private static final String PREFS_NAME = "MyPrefsFile";

    private SigninBinding binding;
    private TextView signupView;
    private EditText email, password;
    private Button signin;
    private Handler h = new Handler();
    private FirebaseAuth auth;

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
                                if (user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()) {
                                    // The user is new, direct them to GetStarted_Page
                                    storeUserAuthState(true); // User is authenticated
                                    Intent intent = new Intent(Signin_Page.this, GetStarted_Page.class);
                                    startActivity(intent);
                                } else {
                                    // The user is not new, direct them to the dashboard (MainActivity)
                                    storeUserAuthState(true); // User is authenticated
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

        signupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signin_Page.this, Register_Page.class);
                startActivity(intent);
            }
        });

        // Check and handle user authentication state
        if (checkUserAuthState()) {
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                Intent intent;
                if (user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()) {
                    // The user is new, direct them to GetStarted_Page
                    intent = new Intent(Signin_Page.this, GetStarted_Page.class);
                } else {
                    // The user is not new, direct them to the dashboard (MainActivity)
                    intent = new Intent(Signin_Page.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }
    }

    // Store user authentication state in SharedPreferences
    private void storeUserAuthState(boolean isAuthenticated) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("isAuthenticated", isAuthenticated);
        editor.apply();
    }

    // Check user authentication state from SharedPreferences
    private boolean checkUserAuthState() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean("isAuthenticated", false);
    }
}
