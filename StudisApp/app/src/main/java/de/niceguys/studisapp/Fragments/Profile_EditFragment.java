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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.niceguys.studisapp.EditProfileActivity;
import de.niceguys.studisapp.HtmlParser;
import de.niceguys.studisapp.Interfaces.Interface_Parser;
import de.niceguys.studisapp.MainActivity;
import de.niceguys.studisapp.Manager;
import de.niceguys.studisapp.Model.User;
import de.niceguys.studisapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile_EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile_EditFragment extends Fragment implements Interface_Parser {

    private View view;
    private Button mBtn_saveProfile;
    private EditText mEditUserName,  mEditPostalCode, mEditUniversity;
    private Spinner mSpinnerCourseOfStudy, mSpinnerSemester;
    private ImageView prof_image;
    private String newUsername, newCourseOfStudy,newCourseOfStudyId, newPostalCode, newSemester, newSemesterId, newUniversity;
    private DatabaseReference userRef;
    private FirebaseDatabase database;
    private static final String USER = "Users";
    private static final String UNAME = "username";
    private static final String COURSE = "courseOfStudy";
    private static final String COURSEID = "courseOfStudyId";
    private static final String POSTCODE = "postalCode";
    private static final String SEM = "semester";
    private static final String SEMID = "semesterId";
    private static final String UNI = "university";
    StorageReference storageReference;
    FirebaseUser firebaseUser;
    private Uri mImageUri;
    private StorageTask uploadTask;
            // Mobile Computing, MC, 3 WS 2020, 3%23SS%23SS
    private String degree, degree_id, semester, semester_id;
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
        mSpinnerCourseOfStudy = view.findViewById(R.id.mSpinnerCourseOfStudy);
        mEditPostalCode = view.findViewById(R.id.edit_postal_code);
        mSpinnerSemester = view.findViewById(R.id.mSpinnerSemester);
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

                try {

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.child("id").getValue().equals(user.getUid())) {
                            User user1 = ds.getValue(User.class);
                            Glide.with(getActivity().getApplicationContext()).load(user1.getImgurl()).into(prof_image);
                            mEditUserName.setText(ds.child(UNAME).getValue(String.class));
                            //TODO insert if selected //mEditCourseOfStudy.setText((ds.child(COURSE).getValue(String.class) == null) ? ("") : ds.child(COURSE).getValue(String.class));
                            mEditPostalCode.setText(ds.child(POSTCODE).getValue(String.class));
                            mEditUniversity.setText(ds.child(UNI).getValue(String.class));

                        }
                    }

                } catch (Exception e) {
                    //
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new HtmlParser(this).parse(Manager.Parser.degrees);

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

        newPostalCode = mEditPostalCode.getText().toString();

        newUniversity = mEditUniversity.getText().toString();

        newSemester = semester;

        newSemesterId = semester_id;

        newCourseOfStudy = degree;

        newCourseOfStudyId = degree_id;

        //DB Changes
        //TODO
        userRef.child(USERID).child(UNAME).setValue(newUsername);
        userRef.child(USERID).child(COURSE).setValue(newCourseOfStudy);
        userRef.child(USERID).child(COURSEID).setValue(newCourseOfStudyId);
        userRef.child(USERID).child(POSTCODE).setValue(newPostalCode);
        userRef.child(USERID).child(SEM).setValue(newSemester);
        userRef.child(USERID).child(SEMID).setValue(newSemesterId);
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

    @Override
    public void parsed(Map<String, String> values, String mode) {

        if (mode.equals("degrees")) {

            fillDegrees(values);

        } else if (mode.equals("semester")) {

            fillSemester(values);

        }

    }

    private void fillSemester(Map<String, String> values) {

        ArrayList<String> temp = new ArrayList<>(values.values());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, temp);
        mSpinnerSemester.setAdapter(arrayAdapter);

        mSpinnerSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                semester = mSpinnerSemester.getItemAtPosition(position).toString();
                if (!semester.equals("Semester wählen")) {

                    for (String s : values.keySet()) {

                        if (Objects.equals(values.get(s), semester)) {

                            semester_id = s;
                            break;

                        }

                    }

                } else semester = "";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    //Mobile Computing / MC
    private void fillDegrees(Map<String, String> values) {


        ArrayList<String> temp = new ArrayList<>(values.values());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, temp);
        mSpinnerCourseOfStudy.setAdapter(arrayAdapter);

        mSpinnerCourseOfStudy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                degree = mSpinnerCourseOfStudy.getItemAtPosition(position).toString();
                if (!degree.equals("Studiengang wählen")) {

                    for (String s : values.keySet()) {

                        if (Objects.equals(values.get(s), degree)) {

                            degree_id = s;
                            break;

                        }

                    }

                    downloadSemester();

                } else degree = "";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void downloadSemester() {

        new HtmlParser(this).parse(Manager.Parser.semester, degree_id);

    }

}