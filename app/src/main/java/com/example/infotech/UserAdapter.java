package com.example.infotech;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private Context context;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_friendview, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profileImageView;
        private TextView nameTextView;
        private TextView emailTextView;

        private ImageButton addUserButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);

            addUserButton = itemView.findViewById(R.id.addUserButton);
            addUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        User user = userList.get(position);
                        sendFriendRequest(user);
                    }
                }
            });
        }

        public void bind(User user) {
            String fullName = user.getFirstname() + " " + user.getMiddlename() + " " + user.getLastname();
            nameTextView.setText(fullName);
            emailTextView.setText(user.getEmail());

            Glide.with(context)
                    .load(user.getprofileImage())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true) // Optional: If you don't want to cache images
                    .into(profileImageView);
        }

        // When sending a friend request, set the sender's name
        private void sendFriendRequest(User user) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null) {
                String currentUserId = currentUser.getUid();
                String currentUserEmail = currentUser.getEmail();

                // Check if the friend request is not sent to the current user
                if (!currentUserId.equals(user.getUserId())) {
                    // Reference to the current user's node in the database
                    DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);

                    currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String firstName = dataSnapshot.child("firstName").getValue(String.class);
                                String lastName = dataSnapshot.child("lastName").getValue(String.class);
                                String middleName = dataSnapshot.child("middleName").getValue(String.class);
                                String profileImage = dataSnapshot.child("profileImage").getValue(String.class);

                                String fullName = firstName + " " + (middleName != null ? middleName + " " : "") + lastName;

                                // Log important information for debugging
                                Log.d("FriendRequest", "Current User ID: " + currentUserId);
                                Log.d("FriendRequest", "Receiver User ID: " + user.getUserId());
                                Log.d("FriendRequest", "Sender's Name: " + user.getEmail());
                                Log.d("FriendRequest", "Sender's Name: " + fullName);
                                Log.d("FriendRequest", "Sender's Name: " + profileImage);

                                // Create a new friend request with the receiverId set to the userId of the search user
                                FriendRequest friendRequest = new FriendRequest(currentUserId, user.getUserId(), fullName, currentUserEmail, profileImage);

                                // Log the content of the FriendRequest object
                                Log.d("FriendRequest", "Friend Request Object: " + friendRequest.getSenderId() + ", " + friendRequest.getReceiverId() + ", " + friendRequest.getSenderName());

                                // Store the friend request in the Firebase Realtime Database
                                DatabaseReference friendRequestsRef = FirebaseDatabase.getInstance().getReference().child("friend_requests");
                                String requestId = friendRequestsRef.push().getKey();
                                friendRequestsRef.child(requestId).setValue(friendRequest);

                                // Optionally, you can provide feedback to the user (e.g., a Toast message) to confirm the request was sent
                                Toast.makeText(context, "Friend request sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle the database error
                            Log.e("FriendRequest", "Error reading user data: " + databaseError.getMessage());
                        }
                    });
                } else {
                    // Handle the case where the user is trying to send a request to themselves
                    Toast.makeText(context, "You cannot send a friend request to yourself.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle the case where the user is not signed in or there is no current user
                Toast.makeText(context, "You are not signed in. Please log in.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}