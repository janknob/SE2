package de.niceguys.studisapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.niceguys.studisapp.Model.User;
import de.niceguys.studisapp.R;


public class Profile_MainFragment extends Fragment {

    private TextView tvUserName, tvCourseOfStudy, tvPostalCode, tvSemester, tvUniversity;
    private ImageView image;
    View view;
    private DatabaseReference userRef;
    private FirebaseDatabase database;
    private static final String USER = "Users";
    private static final String UNAME = "username";
    private static final String COURSE = "courseOfStudy";
    private static final String POSTCODE = "postalCode";
    private static final String SEM = "semester";
    private static final String UNI = "university";


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile__main, container, false);


        //initialize
        tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        image = view.findViewById(R.id.iv_profileImage);
        tvCourseOfStudy = view.findViewById(R.id.tv_course);
        tvPostalCode = view.findViewById(R.id.tv_postalcode);
        tvSemester = view.findViewById(R.id.tv_semester);
        tvUniversity = view.findViewById(R.id.tv_university);


        //DB Reference
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(USER);


        //get data and set it in the view
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                for (DataSnapshot ds : snapshot.getChildren()) {
                    if(ds.child("id").getValue().equals(user.getUid())) {
                        tvUserName.setText(ds.child(UNAME).getValue(String.class));
                        User user1 = ds.getValue(User.class);
                        tvCourseOfStudy.setText(ds.child(COURSE).getValue(String.class));
                        tvPostalCode.setText(ds.child(POSTCODE).getValue(String.class));
                        tvSemester.setText(ds.child(SEM).getValue(String.class));
                        tvUniversity.setText(ds.child(UNI).getValue(String.class));
                        Glide.with(requireActivity()).load(user1.getImgUrl()).into(image);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
    public static Profile_MainFragment newInstance() {

        Profile_MainFragment fragment = new Profile_MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }
}