package com.example.infotech;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
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



        // Set a click listener for the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the new activity when the item is clicked
                Intent intent = new Intent(view.getContext(), Flashcards_View.class);

                // Pass the flashcardSet data to the next activity
                intent.putExtra("flashcardSet", flashcardSet);

                view.getContext().startActivity(intent);
            }
        });
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
