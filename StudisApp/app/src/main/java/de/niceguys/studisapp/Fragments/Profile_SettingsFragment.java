package de.niceguys.studisapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.niceguys.studisapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile_SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile_SettingsFragment extends Fragment {

    View view;
    private DatabaseReference userRef;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile__settings, container, false);


        return view;
    }
    public static Profile_SettingsFragment newInstance() {

        Profile_SettingsFragment fragment = new Profile_SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }
}