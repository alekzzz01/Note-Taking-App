package com.example.infotech;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import io.realm.Realm;


public class Dashboard_Fragment extends Fragment {

    ImageButton newNoteButton;

    TextView mainTitleEditText;

    RelativeLayout firstNoteLayout;

    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_dashboard_, container, false);
        recyclerView = v.findViewById(R.id.recyclerview);


        newNoteButton = v.findViewById(R.id.addnewnotebtn);

        newNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Notes_Edit activity
                Intent a = new Intent(getActivity(), Notes_Edit.class);
                startActivity(a);
            }
        });






    return v;

    }
}

