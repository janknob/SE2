package de.niceguys.studisapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.niceguys.studisapp.Activities.CreatePostActivity;
import de.niceguys.studisapp.Model.Post;
import de.niceguys.studisapp.Model.PostAdapter;
import de.niceguys.studisapp.R;


public class Lifestyle_TimelineFragment extends Fragment {


    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private FloatingActionButton floatingActionButton;
    private String category;
    // Method which is called when the Fragment is clicked
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // set attributes
        View view = inflater.inflate(R.layout.fragment_lifestyle__timeline, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);
        floatingActionButton = view.findViewById(R.id.btn_new_tweet);

        // Button for new Posts
        floatingActionButton.setOnClickListener(view1 -> {
            Intent i = new Intent(getActivity(), CreatePostActivity.class);
            i.putExtra("category", category);
            startActivity(i);
        });
        readPosts(category);
        return view;
    }
    // creates new Fragment Instance when the fragment is called
    public static Lifestyle_TimelineFragment newInstance() {

        Lifestyle_TimelineFragment fragment = new Lifestyle_TimelineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }
    // set the Category for the post
    public void setCategory (String category)
    {
        this.category = category;
    }

    // reads all the Post in the Database
    private void readPosts(String postCategory) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Post post = snapshot.getValue(Post.class);
                    if ("Timeline".equals(postCategory))
                    {
                        postList.add(post);
                    }
                    if (post.getCategory().equals(postCategory))
                    {
                        postList.add(post);
                    }

                    postAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }

        });
    }
}