package com.example.infotech;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        // Bind data to your list item view here
        holder.bind(note);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleoutput); // Example view ID
        }

        public void bind(Note note) {
            // Bind the data from the Note object to your views
            titleTextView.setText(note.getTitle());
        }



    }


}
