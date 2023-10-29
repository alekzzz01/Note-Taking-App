package com.example.infotech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Flashcardsview_All extends AppCompatActivity {


    ImageButton backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcardsview_all);



        backbtn = findViewById(R.id.backButton);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity (Notes_Edit) to go back to the previous activity (Dashboard_Fragment)
                finish();
            }
        });






    }
}