package com.example.infotech;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class Activity_Fragment extends Fragment {

    LinearLayout NewFC;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activity_, container, false);
        Context context = getActivity();

        NewFC = v.findViewById(R.id.newflashcardbtn);

        NewFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog customDialog = new Dialog(context);
                customDialog.setContentView(R.layout.dialog_layout);

                customDialog.setTitle("Custom Dialog Title");
                 customDialog.show();
            }
        });
        return v;
    }
}