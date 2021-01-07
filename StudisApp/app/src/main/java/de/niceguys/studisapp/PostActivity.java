package de.niceguys.studisapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    // initialize
    ImageView btn_close;
    Button btn_post;
    EditText post_text;
    StorageReference storageReferencere;
    DatabaseReference reference;
    String category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        btn_close = findViewById(R.id.btn_close);
        btn_post = findViewById(R.id.btn_post);
        post_text = findViewById(R.id.post_text);

        Spinner spinner = (Spinner) findViewById(R.id.post_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PostActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        // spinner for selecting the category for a post
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        category = "Events";
                        break;
                    case 1:
                        category = "Discounts";
                        break;
                    case 2:
                        category = "Jobs";
                        break;
                    case 3:
                        category = "Tutoring";
                        break;
                    case 4:
                        category = "Apartments";
                        break;
                    case 5:
                        category = "Others";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        storageReferencere = FirebaseStorage.getInstance().getReference("posts");

        // close the post activity
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });

        // close the Post activity and uploads the Post to the DB
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ProgressDialog progressDialog = new ProgressDialog(PostActivity.this);
                progressDialog.setMessage(getResources().getString(R.string.createPost));
                progressDialog.show();

                String str_post_text = post_text.getText().toString();

                if (TextUtils.isEmpty(str_post_text)) {
                    Toast.makeText(PostActivity.this, getResources().getString(R.string.fillFields), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else
                {
                    uploadPost(str_post_text, category);
                    progressDialog.dismiss();
                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

    // uploads the post to the DB
    private void uploadPost(String postText, String category) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        String postid = reference.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("category", category);
        hashMap.put("postid", postid);
        hashMap.put("postText", postText);
        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.child(postid).setValue(hashMap);
    }
}