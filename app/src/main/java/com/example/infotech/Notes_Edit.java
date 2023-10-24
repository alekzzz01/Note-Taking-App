package com.example.infotech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

public class Notes_Edit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_edit);


        ImageButton settingsButton = findViewById(R.id.settingsButton);
        ImageButton backbutton = findViewById(R.id.backButton);

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
                Intent intent = new Intent(Notes_Edit.this, DashboardFragment.class);
                startActivity(intent);
            }
        });
    }
}
