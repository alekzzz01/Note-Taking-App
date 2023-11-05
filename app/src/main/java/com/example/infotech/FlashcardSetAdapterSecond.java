package com.example.infotech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FlashcardSetAdapterSecond extends RecyclerView.Adapter<FlashcardSetAdapterSecond.FlashcardViewHolder> {
    private List<Flashcard> flashcards;

    public FlashcardSetAdapterSecond(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.front_view_flashcards, parent, false);
        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        Flashcard flashcard = flashcards.get(position);
        holder.questionTextView.setText(flashcard.getQuestion());
        holder.answerTextView.setText(flashcard.getAnswer());


    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }

    public static class FlashcardViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView questionTextView;
        TextView answerTextView;
        boolean isFrontVisible = true;

        public FlashcardViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card);
            questionTextView = itemView.findViewById(R.id.question1);
            answerTextView = itemView.findViewById(R.id.answer1);

            // Example: Add a click listener to flip the card when clicked
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flipCard();
                }
            });
        }

        private void flipCard() {
            if (isFrontVisible) {
                cardView.animate().rotationY(180).setDuration(400).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        isFrontVisible = false;
                        questionTextView.setVisibility(View.GONE);
                        answerTextView.setVisibility(View.VISIBLE);
                        cardView.setRotationY(0); // Reset rotation to 0 degrees for the next flip

                    }
                });
            } else {
                cardView.animate().rotationY(180).setDuration(400).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        isFrontVisible = true;
                        answerTextView.setVisibility(View.GONE);
                        questionTextView.setVisibility(View.VISIBLE);
                        cardView.setRotationY(0); // Reset rotation to 0 degrees for the next flip
                    }
                });
            }

            isFrontVisible = !isFrontVisible;

        }

    }
}
