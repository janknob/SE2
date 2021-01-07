package de.niceguys.studisapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.niceguys.studisapp.Model.Post;
import de.niceguys.studisapp.PostActivity;
import de.niceguys.studisapp.PostAdapter;
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

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PostActivity.class);
                startActivity(i);
            }
        });
        readPosts(category);
        return view;
    }
    public static Lifestyle_TimelineFragment newInstance() {

        Lifestyle_TimelineFragment fragment = new Lifestyle_TimelineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }
    public void setCategory (String category)
    {
        this.category = category;
    }


    private void readPosts(String postCategory)
    {
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
            public void onCancelled(@NonNull DatabaseError error)
            {
            }
        });

    }
}