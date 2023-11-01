package com.example.infotech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Add_Flashcards extends AppCompatActivity {

    int setNumber = 1;
    ImageButton backbtn;

    EditText question, answer;
    Button add;
    FirebaseDatabase db;
    FirebaseAuth auth;
    int value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flashcards);

        question = findViewById(R.id.etquestion);
        answer = findViewById(R.id.etanswer);
        add = findViewById(R.id.addbtn);
        auth = FirebaseAuth.getInstance();



        backbtn = findViewById(R.id.backButton);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity (Notes_Edit) to go back to the previous activity (Dashboard_Fragment)
                finish();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if (value < 5 ){

                String Question = question.getText().toString();
                String Answer = answer.getText().toString();


                if (TextUtils.isEmpty(Question) || TextUtils.isEmpty(Answer)){
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    saveUserQuestionAnswerToFirebase(Question, Answer);
                    Toast.makeText(getApplicationContext(), "Question and Answer saved", Toast.LENGTH_SHORT).show();
                    question.setText("");
                    answer.setText("");
                    value++;
                }

            } else {
                Toast.makeText(getApplicationContext(), "Saved already", Toast.LENGTH_SHORT).show();
            }

            }

            private void saveUserQuestionAnswerToFirebase(String question, String answer) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                String userID = firebaseUser.getUid();

                DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("flashcards").child("set" + setNumber);

                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() < 5) {
                            //Add new flashcard in existing set
                            long flashcardCount = snapshot.getChildrenCount();
                            String flashcardNumber = "flashcard" + (flashcardCount + 1);

                            DatabaseReference newFlashcard = userReference.child(flashcardNumber);
                            newFlashcard.child("Question").setValue(question);
                            newFlashcard.child("Answer").setValue(answer);
                        } else {
                            //Add 1 in the setNumber and create a new set for new flashcard
                            setNumber++;
                            DatabaseReference newSetReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("flashcards").child("set" + setNumber);
                            DatabaseReference newFlashcardReference = newSetReference.push();
                            newFlashcardReference.child("Question").setValue(question);
                            newFlashcardReference.child("Answer").setValue(answer);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}