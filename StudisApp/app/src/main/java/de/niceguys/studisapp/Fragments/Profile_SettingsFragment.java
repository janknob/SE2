package de.niceguys.studisapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import de.niceguys.studisapp.Interfaces.Interface_Parser;
import de.niceguys.studisapp.Manager;
import de.niceguys.studisapp.R;
import de.niceguys.studisapp.HtmlParser;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile_SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile_SettingsFragment extends Fragment implements Interface_Parser {

    private View view;
    private DatabaseReference userRef;
    private String degree, degree_id, semester, semester_id;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile__settings, container, false);

    }
    public static Profile_SettingsFragment newInstance() {

        Profile_SettingsFragment fragment = new Profile_SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        HtmlParser parser = new HtmlParser(this);
        parser.parse(Manager.Parser.degrees);

    }

}





















