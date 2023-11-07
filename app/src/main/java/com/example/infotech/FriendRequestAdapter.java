package com.example.infotech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {
    private List<FriendRequest> friendRequestList;
    private Context context;

    public FriendRequestAdapter(Context context, List<FriendRequest> friendRequestList) {
        this.context = context;
        this.friendRequestList = friendRequestList;
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.accept_friendview, parent, false);
        return new FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        FriendRequest friendRequest = friendRequestList.get(position);
        holder.bind(friendRequest);
    }

    @Override
    public int getItemCount() {
        return friendRequestList.size();
    }

    public class FriendRequestViewHolder extends RecyclerView.ViewHolder {
        private TextView senderNameTextView;

        private TextView emailTextView;
        private ImageButton acceptButton;
        private ImageButton rejectButton;

        private ImageView profileImageView;

        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            senderNameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            profileImageView =  itemView.findViewById(R.id.profileImageView);
            acceptButton = itemView.findViewById(R.id.acceptButton);

            rejectButton = itemView.findViewById(R.id.rejectButton);
        }

        public void bind(FriendRequest friendRequest) {
            // Display the sender's name in the TextView
            senderNameTextView.setText(friendRequest.getSenderName());
            emailTextView.setText(friendRequest.getSenderEmail());


            Glide.with(context)
                    .load(friendRequest.getSenderProfileImage())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true) // Optional: If you don't want to cache images
                    .into(profileImageView);


            // Handle the accept button click
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle accepting the friend request (e.g., update the database)
                    acceptFriendRequest(friendRequest);
                }
            });

            // Handle the reject button click
            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle rejecting the friend request (e.g., update the database)
                    rejectFriendRequest(friendRequest);
                }
            });
        }

        private void acceptFriendRequest(FriendRequest friendRequest) {
            // Update the sender's friend list
            DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference("users").child(friendRequest.getSenderId());
            senderRef.child("friends").child(friendRequest.getReceiverId()).setValue(true);

            // Update the receiver's friend list
            DatabaseReference receiverRef = FirebaseDatabase.getInstance().getReference("users").child(friendRequest.getReceiverId());
            receiverRef.child("friends").child(friendRequest.getSenderId()).setValue(true);

            // Optionally, you can provide feedback to the user
            Toast.makeText(context, "Friend request from " + friendRequest.getSenderName() + " accepted", Toast.LENGTH_SHORT).show();
        }

        private void rejectFriendRequest(FriendRequest friendRequest) {
            // Implement the logic to reject the friend request and update the database
            // You should remove the friend request from the list after rejecting it

            // Optionally, you can provide feedback to the user
            Toast.makeText(context, "Friend request from " + friendRequest.getSenderName() + " rejected", Toast.LENGTH_SHORT).show();
        }
    }
}
