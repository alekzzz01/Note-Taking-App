package com.example.infotech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FlashcardSetAdapter extends RecyclerView.Adapter<FlashcardSetAdapter.FlashcardSetViewHolder> {
    private List<FlashcardSet> flashcardSets;

    public FlashcardSetAdapter(List<FlashcardSet> flashcardSets) {
        this.flashcardSets = flashcardSets;
    }

    @NonNull
    @Override
    public FlashcardSetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flashcardsview_view, parent, false);
        return new FlashcardSetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardSetViewHolder holder, int position) {
        FlashcardSet flashcardSet = flashcardSets.get(position);
        holder.titleTextView.setText(flashcardSet.getTitle());
        holder.flashcardCountTextView.setText("Flashcards: " + flashcardSet.getFlashcardCount());
    }

    @Override
    public int getItemCount() {
        return flashcardSets.size();
    }

    public class FlashcardSetViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView flashcardCountTextView;

        public FlashcardSetViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            flashcardCountTextView = itemView.findViewById(R.id.flashcardCountTextView);
        }
    }
}
