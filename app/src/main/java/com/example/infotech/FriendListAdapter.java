package com.example.infotech;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendViewHolder> {
    private List<FriendRequest> friendList;
    private Context context;

    public FriendListAdapter(Context context, List<FriendRequest> friendList) {
        this.context = context;
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboardfriend_view, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        FriendRequest friend = friendList.get(position);
        holder.bind(friend);
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFriends(List<FriendRequest> friends) {
        // Update the list of friends and notify the adapter of the data change
        friendList = friends;
        notifyDataSetChanged();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        private TextView fullNameTextView;
        private ImageView profileImageView;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
        }

        public void bind(FriendRequest friend) {
            fullNameTextView.setText(friend.getSenderName());

            // Load the profile image using Glide or your preferred image loading library
            Glide.with(context)
                    .load(friend.getSenderProfileImage())
                    .into(profileImageView);
        }
    }
}
