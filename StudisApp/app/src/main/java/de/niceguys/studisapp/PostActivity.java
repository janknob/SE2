package de.niceguys.studisapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    ImageView btn_close;
    Button btn_post;
    EditText post_text;
    StorageReference storageReferencere;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        btn_close = findViewById(R.id.btn_close);
        btn_post = findViewById(R.id.btn_post);
        post_text = findViewById(R.id.post_text);

        storageReferencere = FirebaseStorage.getInstance().getReference("posts");

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ProgressDialog progressDialog = new ProgressDialog(PostActivity.this);
                progressDialog.setMessage("Post wird erstellt");
                progressDialog.show();

                String str_post_text = post_text.getText().toString();

                if (TextUtils.isEmpty(str_post_text)) {
                    Toast.makeText(PostActivity.this, "Sie müssen einen Text eingeben!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else
                {
                    uploadPost(str_post_text);
                    progressDialog.dismiss();
                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }
    private void uploadPost(String postText) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

        String postid = reference.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("postid", postid);
        hashMap.put("postText", postText);
        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.child(postid).setValue(hashMap);
    }
}