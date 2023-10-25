package com.example.infotech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Notes_Edit extends AppCompatActivity {

    TextView dateTimeTextView;

    Button saveButton;
    EditText titleEditText, bodyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_edit);


        ImageButton settingsButton = findViewById(R.id.settingsButton);
        ImageButton backbutton = findViewById(R.id.backButton);

        dateTimeTextView = findViewById(R.id.date_time);

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        String formattedDate = dateFormat.format(date);
        dateTimeTextView.setText(formattedDate);
        saveButton = findViewById(R.id.save);
        titleEditText = findViewById(R.id.title);
        bodyEditText = findViewById(R.id.body);


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
                // Create an intent to go back to the previous activity
                Intent intent = new Intent(Notes_Edit.this, Dashboard_Fragment.class);
                startActivity(intent);
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Notes_Edit.this, Dashboard_Fragment.class);
                startActivity(a);
            }
        });


    }
}