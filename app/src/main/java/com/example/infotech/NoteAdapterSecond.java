package com.example.infotech;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NoteAdapterSecond extends RecyclerView.Adapter<NoteAdapterSecond.NoteViewHolder> {
    private final List<Note> noteListSecond; // List of Note objects for the second RecyclerView

    public NoteAdapterSecond(List<Note> notesSecond) {
        this.noteListSecond = notesSecond;
    }

    // Add the setNotes method to update the data in the adapter
    public void setNotes(List<Note> notes) {
        noteListSecond.clear();
        noteListSecond.addAll(notes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notesitem_view, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        final Note note = noteListSecond.get(position);
        holder.bind(note);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the Notes_Edit activity for editing when a list item is clicked
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, Notes_Edit.class);
                intent.putExtra("selectedNote", note);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteListSecond.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView timestampTextView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.specialTitleOutput);
            timestampTextView = itemView.findViewById(R.id.timestamp);
        }

        public void bind(Note note) {
            titleTextView.setText(note.getTitle());

            // Debug log to check the timestamp
            Log.d("TimestampDebug", "Timestamp value in bind: " + note.getTimestamp());

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
            String formattedTimestamp = dateFormat.format(note.getTimestamp());
            timestampTextView.setText(formattedTimestamp);
        }

    }
}