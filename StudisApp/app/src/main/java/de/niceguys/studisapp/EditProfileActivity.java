package de.niceguys.studisapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class EditProfileActivity extends AppCompatActivity {

    Button mBtn_saveProfile;
    EditText mEditName, mEditPlace, mEditSex, mEditAge, mEditDesc;
    ImageView profileImageView;
    String newName, newPlace, newSex, newAge, newDec;
    private DatabaseReference userRef;
    private FirebaseDatabase database;
    private static final String USER = "Users";

    String PROFILE_IMAGE_URL = null;
    int TAKE_IMAGE_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //initialize Hooks
        mBtn_saveProfile = (Button) findViewById(R.id.btn_saveProfile);
        mEditName = findViewById(R.id.edit_name);
        mEditPlace = findViewById(R.id.edit_place);
        mEditSex = findViewById(R.id.edit_sex);
        mEditAge = findViewById(R.id.edit_age);
        mEditDesc = findViewById(R.id.edit_desc);
        profileImageView = findViewById(R.id.image_edit_profile);

        //DB references
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(USER);



        //Get current Information
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                for (DataSnapshot ds : snapshot.getChildren()) {
                    if(ds.child("id").getValue().equals(user.getUid())) {
                        mEditName.setText(ds.child("username").getValue(String.class));
                        mEditPlace.setText(ds.child("place").getValue(String.class));
                        mEditAge.setText(ds.child("age").getValue(String.class));
                        mEditSex.setText(ds.child("sex").getValue(String.class));
                        mEditDesc.setText(ds.child("desc").getValue(String.class));

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Button Click
        mBtn_saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                startActivity(intent);
                applyChanges();
            }
        });
    }

    //save changes
    void applyChanges(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String USERID = user.getUid();

        newName = mEditName.getText().toString();
        newPlace = mEditPlace.getText().toString();
        newSex = mEditSex.getText().toString();
        newAge = mEditAge.getText().toString();
        newDec = mEditDesc.getText().toString();

        //DB Changes
        userRef.child(USERID).child("username").setValue(newName);
        userRef.child(USERID).child("place").setValue(newPlace);
        userRef.child(USERID).child("sex").setValue(newSex);
        userRef.child(USERID).child("age").setValue(newAge);
        userRef.child(USERID).child("desc").setValue(newDec);



        //sending confirmation toast
        Context context = getApplicationContext();
        CharSequence text = "Änderungen wurden übernommen";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_IMAGE_CODE){
            switch (resultCode){
                case RESULT_OK:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    profileImageView.setImageBitmap(bitmap);
                    handleUpload(bitmap);
            }
        }
    }

    private void handleUpload(Bitmap bitmap){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //DB Reference
        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("ProfileImages")
                .child("" + ".jpg");

        reference.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        getDownloadUrl(reference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "onFailure: ", e.getCause());
                    }
                });
    }

    private void getDownloadUrl(StorageReference reference) {

        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("TAG", "onSucess: " + uri);
                setUserUrl(uri);
            }

        });
    }

    public void handleImageClick(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, TAKE_IMAGE_CODE);
        }
    }

    private void setUserUrl(Uri uri){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditProfileActivity.this, "Uploades succesfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, "Profile image failed...", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
