package de.niceguys.studisapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import de.niceguys.studisapp.LoginActivity;
import de.niceguys.studisapp.MainActivity;
import de.niceguys.studisapp.Manager;
import de.niceguys.studisapp.R;
import de.niceguys.studisapp.StartActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UniversityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private TextView textView;
    private DrawerLayout dL;
    private Toolbar tb;
    private NavigationView nV;
    private Profile_MainFragment mainFragment;
    private Profile_EditFragment editFragment;
    private Profile_SettingsFragment settingsFragment;


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dL = view.findViewById(R.id.dL_profile);
        tb = view.findViewById(R.id.tb_profile);
        textView = view.findViewById(R.id.logout);

        tb.setNavigationOnClickListener(view1 -> dL.openDrawer(GravityCompat.START));

        nV = view.findViewById(R.id.nav_profile);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Manager.getInstance().getData("settings").edit().clear().apply();
                Manager.getInstance().getData("course").edit().clear().apply();
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
            }
        });
        nV.setNavigationItemSelectedListener(item -> {
            assert getFragmentManager() != null;
            switch (item.getItemId()) {

                case R.id.menu_profile_main:


                    //Change fragment
                    Log.w("ProfileFragment", "Profile selected");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    if (editFragment != null && editFragment.isAdded()) ft.hide(editFragment);
                    if (editFragment != null && editFragment.isAdded()) ft.hide(editFragment);
                    ft.show(mainFragment);
                    ft.commit();
                    tb.setTitle(getResources().getString(R.string.profile));

                    break;

                case R.id.menu_profile_editProfile:


                    if (editFragment == null) {
                        editFragment = Profile_EditFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_profile_fragmentContainer, editFragment);
                        ft2.commit();
                    }


                    FragmentTransaction ft3 = getFragmentManager().beginTransaction();
                    if (settingsFragment != null && settingsFragment.isAdded()) ft3.hide(settingsFragment);
                    if (mainFragment != null && mainFragment.isAdded()) ft3.hide(mainFragment);
                    ft3.hide(mainFragment);
                    ft3.show(editFragment);
                    ft3.commit();
                    tb.setTitle(getResources().getString(R.string.editProfile));



                    break;

                case R.id.menu_profile_settings:

                    if (settingsFragment == null) {
                        settingsFragment = Profile_SettingsFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_profile_fragmentContainer, settingsFragment);
                        ft2.commit();
                    }


                    FragmentTransaction ft4 = getFragmentManager().beginTransaction();
                    if (mainFragment != null && mainFragment.isAdded()) ft4.hide(mainFragment);
                    if (editFragment != null && editFragment.isAdded()) ft4.hide(editFragment);
                    ft4.hide(mainFragment);
                    ft4.show(settingsFragment);
                    ft4.commit();
                    tb.setTitle(getResources().getString(R.string.settings));

                    break;
            }
            dL.closeDrawer(GravityCompat.START);
            return false;
        });

        mainFragment = Profile_MainFragment.newInstance();
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.add(R.id.fl_profile_fragmentContainer, mainFragment);
        ft.commit();
    }
}


