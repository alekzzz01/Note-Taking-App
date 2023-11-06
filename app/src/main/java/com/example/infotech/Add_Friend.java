package com.example.infotech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Add_Friend extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView userListRecyclerView;
    private UserAdapter userAdapter;
    private List<User> userList; // This list will hold the search results
    private DatabaseReference usersRef; // Reference to your Firebase "users" node


    private RecyclerView friendRequestsRecyclerView;
    private FriendRequestAdapter friendRequestAdapter;
    private List<FriendRequest> friendRequests; // This list will hold friend requests
    private DatabaseReference friendRequestsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);


        searchView = findViewById(R.id.searchView);
        userListRecyclerView = findViewById(R.id.userListRecyclerView);
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(this, userList);

        // Set up the RecyclerView
        userListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userListRecyclerView.setAdapter(userAdapter);

        // Initialize the Firebase database reference
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");


        // Set a query listener for the SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform the user search and populate the RecyclerView
                searchUsers(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle real-time search updates if needed
                return true;
            }
        });


        // Initialize the Firebase database reference for friend requests
        friendRequestsRef = FirebaseDatabase.getInstance().getReference().child("friend_requests");

        // Initialize the RecyclerView for friend requests
        friendRequestsRecyclerView = findViewById(R.id.friendRequestsRecyclerView);
        friendRequests = new ArrayList<>();
        friendRequestAdapter = new FriendRequestAdapter(this, friendRequests);
        friendRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendRequestsRecyclerView.setAdapter(friendRequestAdapter);

        // Load and display friend requests
        loadFriendRequests();
    }


    public void searchUsers(String query) {
        // Clear the previous search results
        userList.clear();
        userAdapter.notifyDataSetChanged();

        // Query Firebase for users with emails matching the search query
        usersRef.orderByChild("email").startAt(query).endAt(query + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);

                            if (user != null) {
                                // Add the user ID to the user object
                                user.setUserId(snapshot.getKey());

                                userList.add(user);
                            }
                        }
                        userAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Add_Friend.this, "Search failed: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void loadFriendRequests() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = currentUser.getUid();

        Query query = friendRequestsRef.orderByChild("receiverId").equalTo(currentUserId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friendRequests.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FriendRequest friendRequest = snapshot.getValue(FriendRequest.class);
                    friendRequests.add(friendRequest);
                }
                friendRequestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Add_Friend.this, "Failed to load friend requests: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}