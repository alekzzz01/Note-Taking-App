package com.example.infotech;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


public class Activity_Fragment extends Fragment {

    LinearLayout NewFC, NewNote, ViewNote, ViewFC;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activity_, container, false);
        Context context = getActivity();

        NewFC = v.findViewById(R.id.newflashcardbtn);
        NewNote = v.findViewById(R.id.addNote);

        ViewNote = v.findViewById(R.id.ViewallNote);
        ViewFC = v.findViewById(R.id.ViewAllFC);

        ViewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotesView_All.class);
                startActivity(intent);
            }
        });


        ViewFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Flashcardsview_All.class);
                startActivity(intent);
            }
        });




        NewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to Notes_Edit and pass an extra parameter
                Intent intent = new Intent(getActivity(), Notes_Edit.class);
                intent.putExtra("isNewNote", true);
                startActivity(intent);

            }
        });

        NewFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog customDialog = new Dialog(context);
                customDialog.setContentView(R.layout.dialog_layout);

                EditText titlefield = customDialog.findViewById(R.id.title);
                Button New = customDialog.findViewById(R.id.addtitlebtn);

                New.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String flashcardSetTitle = titlefield.getText().toString();
                        customDialog.dismiss();
                        Intent intent = new Intent(context, Add_Flashcards.class);
                        intent.putExtra("flashcardSetTitle", flashcardSetTitle);
                        context.startActivity(intent);
                    }
                });


                customDialog.setTitle("Custom Dialog Title");
                customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                 customDialog.show();



            }
        });


        return v;
    }
}