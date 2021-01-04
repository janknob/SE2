package de.niceguys.studisapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import de.niceguys.studisapp.Model.Post;
import de.niceguys.studisapp.Model.User;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>
{
    public Context mContext;
    public List<Post> mPost;
    private FirebaseUser firebaseUser;
    // Constructor for PostAdapter
    public  PostAdapter (Context mContext, List<Post> mPost)
    {
        this.mContext = mContext;
        this.mPost = mPost;
    }
    // Method for displaying the posts
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, viewGroup, false);

        return new ViewHolder(view);
    }
    // Method for displaying the postText, username and image
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post = mPost.get(i);
        viewHolder.post_text.setText(mPost.get(i).getPostText());
        viewHolder.username.setText(mPost.get(i).getPublisher());
        if (mPost.get(i).getCategory().equals("Events"))
        {
            viewHolder.category.setText("Events");
        }
        else if (mPost.get(i).getCategory().equals("Discounts"))
        {
            viewHolder.category.setText("Angebote");
        }
        else if (mPost.get(i).getCategory().equals("Specials"))
        {
            viewHolder.category.setText("Specials");
        }
        else if (mPost.get(i).getCategory().equals("Jobs"))
        {
            viewHolder.category.setText("Jobangebote");
        }
        else if (mPost.get(i).getCategory().equals("Tutoring"))
        {
            viewHolder.category.setText("Nachhilfe");
        }
        else
        {
            viewHolder.category.setText("Wohnungen");
        }

        publisherInfo(viewHolder.image_profile, viewHolder.username, post.getPublisher());
        isLiked(post.getPostid(), viewHolder.like);
        nrLikes(viewHolder.likes, post.getPostid());
        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.like.getTag().equals("like"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);
                }
                else
                {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_post_edit:
                                editPost(post.getPostid());
                                return true;
                            case R.id.menu_post_delete:
                                FirebaseDatabase.getInstance().getReference("Posts").child(post.getPostid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(mContext, "Gelöscht!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                return true;
                            case R.id.menu_post_report:
                                Toast.makeText(mContext, "Post wurde gemeldet!", Toast.LENGTH_SHORT).show();
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.post_menu);
                if (!post.getPublisher().equals(firebaseUser.getUid()))
                {
                 popupMenu.getMenu().findItem(R.id.menu_post_edit).setVisible(false);
                 popupMenu.getMenu().findItem(R.id.menu_post_delete).setVisible(false);
                }
                popupMenu.show();
            }
        });
    }
    // Returns the number of posts
    @Override
    public int getItemCount() {
        return mPost.size();
    }
    // Class for one post item
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView image_profile, like, dislike, more;
        public TextView username, post_text, likes, category;

        public ViewHolder(View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            like = itemView.findViewById(R.id.like);
            dislike = itemView.findViewById(R.id.dislike);
            username = itemView.findViewById(R.id.username);
            post_text = itemView.findViewById(R.id.post_text);
            likes = itemView.findViewById(R.id.likes);
            more = itemView.findViewById(R.id.more);
            category = itemView.findViewById(R.id.post_category);

        }
    }
    // Method for liking posts
    private void isLiked (String postid, ImageView imageView)
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists())
                {
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("liked");
                }
                else
                {
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Method for numbers of likes per post
    private void nrLikes(TextView likes, String postid)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount()+" 'Gefällt mir' Angaben ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    // Method for Saving the publisherInfo in the Model User Class from database
    private void publisherInfo (final ImageView image_profile, final TextView username, String userid )
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImgUrl()).into(image_profile);
                username.setText(user.getUsername());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void editPost (String postid) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Post Bearbeiten");

        EditText editText = new EditText (mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        getText(postid, editText);

        alertDialog.setPositiveButton("Bearbeiten", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("postText", editText.getText().toString());

                FirebaseDatabase.getInstance().getReference("Posts").child(postid).updateChildren(hashMap);
            }
        });
        alertDialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void getText (String postid, EditText editText)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts").child(postid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                editText.setText(snapshot.getValue(Post.class).getPostText());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
