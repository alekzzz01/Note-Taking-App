package com.example.infotech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Flashcards_View extends AppCompatActivity {

    private CardView Card;
    TextView Q1, A1, Title;
    private View frontView;
    private View backView;
    private boolean isCardFlipped = false;
    DatabaseReference database, titlename;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards_view);

        Card = findViewById(R.id.card);
        Title = findViewById(R.id.title);
        frontView = findViewById(R.id.frontView);
        backView = findViewById(R.id.backView);
        A1 = findViewById(R.id.answer1);
        Q1 = findViewById(R.id.question1);


        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String userID = currentUser.getUid();
        database = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userID)
                .child("flashcards")
                .child("Science")
                .child("flashcard1");

        titlename = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userID)
                .child("Science")
                .child("flashcard1");

        titlename.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String titlename = snapshot.child("Title").getValue(String.class);

                Title.setText(titlename);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String Question = snapshot.child("Question").getValue(String.class);
                    String Answer = snapshot.child("Answer").getValue(String.class);

                    Q1.setText(Question);
                    A1.setText(Answer);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });


    }



    private void flipCard() {
        float start = isCardFlipped ? 180f : 0f;
        float end = isCardFlipped ? 0f : 180f;

        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(Card, "rotationY", start, end);
        rotateAnimator.setDuration(500);

        rotateAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                isCardFlipped = !isCardFlipped;
                toggleCardViewsVisibility();
            }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        rotateAnimator.start();

    }

    private void toggleCardViewsVisibility() {
        if (isCardFlipped) {
            // Card is flipped to the back, hide the front and show the back
            findViewById(R.id.frontView).setVisibility(View.GONE);
            findViewById(R.id.backView).setVisibility(View.VISIBLE);
        } else {
            // Card is flipped to the front, hide the back and show the front
            findViewById(R.id.frontView).setVisibility(View.VISIBLE);
            findViewById(R.id.backView).setVisibility(View.GONE);
        }
    }

}