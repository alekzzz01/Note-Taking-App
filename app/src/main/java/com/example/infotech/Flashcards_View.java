package com.example.infotech;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Flashcards_View extends AppCompatActivity {

    private CardView Card;
    private View frontView;
    private View backView;
    private boolean isCardFlipped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards_view);

        Card = findViewById(R.id.card);
        frontView = findViewById(R.id.frontView);
        backView = findViewById(R.id.backView);
        
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