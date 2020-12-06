package de.niceguys.studisapp.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import de.niceguys.studisapp.EditProfileActivity;
import de.niceguys.studisapp.MainActivity;
import de.niceguys.studisapp.Model.User;
import de.niceguys.studisapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile_EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile_EditFragment extends Fragment {

    View view;
    Button mBtn_saveProfile;
    EditText mEditUserName, mEditCourseOfStudy, mEditPostalCode, mEditSemester, mEditUniversity;
    ImageView prof_image;
    String newUsername, newCourseOfStudy, newPostalCode, newSemester, newUniversity;
    private DatabaseReference userRef;
    private FirebaseDatabase database;
    private static final String USER = "Users";
    private static final String UNAME = "username";
    private static final String COURSE = "courseOfStudy";
    private static final String POSTCODE = "postalCode";
    private static final String SEM = "semester";
    private static final String UNI = "university";
    StorageReference storageReference;
    FirebaseUser firebaseUser;
    private Uri mImageUri;
    private StorageTask uploadTask;

    String PROFILE_IMAGE_URL = null;
    int TAKE_IMAGE_CODE = 10001;

    public Profile_EditFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Profile_EditFragment newInstance () {
        Profile_EditFragment fragment = new Profile_EditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile__edit, container, false);
        //initialize Hooks
        mBtn_saveProfile = (Button) view.findViewById(R.id.btn_save_profile);
        mEditUserName = view.findViewById(R.id.edit_user_ame);
        mEditCourseOfStudy = view.findViewById(R.id.edit_course_of_study);
        mEditPostalCode = view.findViewById(R.id.edit_postal_code);
        mEditSemester = view.findViewById(R.id.edit_semester);
        mEditUniversity = view.findViewById(R.id.edit_university);
        prof_image = view.findViewById(R.id.image_edit_profile);

        //DB references
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(USER);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");



        //Get current Information
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                for (DataSnapshot ds : snapshot.getChildren()) {
                    if(ds.child("id").getValue().equals(user.getUid())) {
                        User user1 = ds.getValue(User.class);
                        Glide.with(getActivity().getApplicationContext()).load(user1.getImgurl()).into(prof_image);
                        mEditUserName.setText(ds.child(UNAME).getValue(String.class));
                        mEditCourseOfStudy.setText(ds.child(COURSE).getValue(String.class));
                        mEditPostalCode.setText(ds.child(POSTCODE).getValue(String.class));
                        mEditSemester.setText(ds.child(SEM).getValue(String.class));
                        mEditUniversity.setText(ds.child(UNI).getValue(String.class));

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
                //Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                //startActivity(intent);
                applyChanges();
            }
        });
        prof_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setAspectRatio(1,1).setCropShape(CropImageView.CropShape.OVAL).start(getActivity());
            }
        });
        return view;

    }

    // Method for getting the File Extension of the image
    private String getFileExtension(Uri uri){
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(getActivity().getContentResolver().getType(uri));
    }

    // Method for uploading the image from the app to the database storage
    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Wird hochgeladen");
        pd.show();

        if (mImageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));

            uploadTask = fileReference.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }

            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task <Uri> task) {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        String myUri = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("imgurl", ""+myUri);

                        reference.updateChildren(hashMap);
                        pd.dismiss();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Upload Fehlgeschlagen", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Kein Bild ausgewählt", Toast.LENGTH_SHORT).show();
        }
    }

    void applyChanges(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String USERID = user.getUid();

        newUsername = mEditUserName.getText().toString();
        newCourseOfStudy = mEditCourseOfStudy.getText().toString();
        newPostalCode = mEditPostalCode.getText().toString();
        newSemester = mEditSemester.getText().toString();
        newUniversity = mEditUniversity.getText().toString();

        //DB Changes
        userRef.child(USERID).child(UNAME).setValue(newUsername);
        userRef.child(USERID).child(COURSE).setValue(newCourseOfStudy);
        userRef.child(USERID).child(POSTCODE).setValue(newPostalCode);
        userRef.child(USERID).child(POSTCODE).setValue(newSemester);
        userRef.child(USERID).child(UNI).setValue(newUniversity);



        //sending confirmation toast

        CharSequence text = "Änderungen wurden übernommen";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getActivity(), text, duration);
        toast.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();
            uploadImage();
        }
        else {
            Toast.makeText(getActivity(), "Etwas ist Schief gelaufen", Toast.LENGTH_SHORT).show();
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
        //if(intent.resolveActivity(getPackageManager()) != null)
        {
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
                        Toast.makeText(getActivity(), "Uploades succesfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Profile image failed...", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}