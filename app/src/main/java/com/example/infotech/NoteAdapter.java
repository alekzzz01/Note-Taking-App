package com.example.infotech;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private final List<Note> noteList; // List of Note objects

    // Constructor
    public NoteAdapter(List<Note> notes) {
        this.noteList = notes;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setNotes(List<Note> notes) {
        noteList.clear(); // Clear the existing notes
        noteList.addAll(notes); // Add the new notes
        notifyDataSetChanged(); // Notify the adapter that the dataset has changed
    }

    public Note getItem(int position) {
        if (position >= 0 && position < noteList.size()) {
            return noteList.get(position);
        }
        return null;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your list item layout here
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new NoteViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.bind(note);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the Notes_Edit activity for editing when a list item is clicked
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, Notes_Edit.class);
                intent.putExtra("selectedNote", note); // Pass the selected note to the edit activity
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final ProgressBar progressBar;
        private final TextView percentTextView;


        public NoteViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleoutput); // Example view ID
            progressBar = itemView.findViewById(R.id.progressBar); // Change to your ProgressBar ID
            progressBar.getProgressDrawable().setColorFilter(0xFF000000, PorterDuff.Mode.SRC_IN);
            percentTextView = itemView.findViewById(R.id.percent);
        }

        public void bind(Note note) {
            titleTextView.setText(note.getTitle());

            String description = note.getDescription();
            if (description != null) {
                int maxLength = 500; // 500 letters length
                int currentLength = description.length();

                // Calculate the percentage
                int percent = (int) ((currentLength / (float) maxLength) * 100);

                // Update the ProgressBar and TextView
                progressBar.setProgress(percent);
                percentTextView.setText(String.valueOf(percent) + "%");
            } else {
                // Handle the case where description is null (e.g., set ProgressBar and TextView to default values)
                progressBar.setProgress(0);
                percentTextView.setText("0%");
            }
        }
    }
}


