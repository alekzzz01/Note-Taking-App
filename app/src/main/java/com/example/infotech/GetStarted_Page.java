package com.example.infotech;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GetStarted_Page extends AppCompatActivity {

    private Button getStartedBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getstarted_page);

        auth = FirebaseAuth.getInstance();

        getStartedBtn = findViewById(R.id.GetStartedBtn);

        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the user is authenticated
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    // The user is authenticated, go to the dashboard
                    Intent intent = new Intent(GetStarted_Page.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // The user is not authenticated, redirect to the sign-in page
                    Intent signInIntent = new Intent(GetStarted_Page.this, Signin_Page.class);
                    startActivity(signInIntent);
                }
            }
        });
    }
}