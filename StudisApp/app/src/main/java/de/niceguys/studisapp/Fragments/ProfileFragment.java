package de.niceguys.studisapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import de.niceguys.studisapp.Manager;
import de.niceguys.studisapp.Model.CurrentUser;
import de.niceguys.studisapp.R;
import de.niceguys.studisapp.StartLoginRegisterActivity;

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


        textView.setOnClickListener(view12 -> {
            CurrentUser.clear();
            String lang = Manager.getInstance().getData("settings").getString("language", "de");
            Manager.getInstance().getData("settings").edit().clear().apply();
            Manager.getInstance().getData("settings").edit().putString("language", lang).apply();
            Manager.getInstance().getData("course").edit().clear().apply();
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getActivity(), StartLoginRegisterActivity.class);
            i.putExtra("registration", false);
            startActivity(i);
        });


        nV.setNavigationItemSelectedListener(item -> {
            assert requireActivity().getSupportFragmentManager() != null;
            switch (item.getItemId()) {

                case R.id.menu_profile_main:


                    //Change fragment
                    Log.w("ProfileFragment", "Profile selected");
                    
                    FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                    if (settingsFragment != null && settingsFragment.isAdded()) ft.hide(settingsFragment);
                    if (editFragment != null && editFragment.isAdded()) ft.hide(editFragment);
                    ft.show(mainFragment);
                    ft.commit();
                    tb.setTitle(getResources().getString(R.string.profile));

                    break;

                case R.id.menu_profile_editProfile:


                    if (editFragment == null) {
                        editFragment = Profile_EditFragment.newInstance();
                        FragmentTransaction ft2 = requireActivity().getSupportFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_profile_fragmentContainer, editFragment);
                        ft2.commit();
                    }


                    FragmentTransaction ft3 = requireActivity().getSupportFragmentManager().beginTransaction();
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
                        FragmentTransaction ft2 = requireActivity().getSupportFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_profile_fragmentContainer, settingsFragment);
                        ft2.commit();
                    }


                    FragmentTransaction ft4 = requireActivity().getSupportFragmentManager().beginTransaction();
                    if (mainFragment != null && mainFragment.isAdded()) ft4.hide(mainFragment);
                    if (editFragment != null && editFragment.isAdded()) ft4.hide(editFragment);
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


