package de.niceguys.studisapp.Fragments;

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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import de.niceguys.studisapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UniversityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LifestyleFragment extends Fragment {

    private DrawerLayout dL;
    private Toolbar tb;
    private NavigationView nV;

    private Lifestyle_TimelineFragment timelineFragment;
    private Lifestyle_TimelineFragment eventFragment;
    private Lifestyle_TimelineFragment discountsFragment;
    private Lifestyle_TimelineFragment specialsFragment;
    private Lifestyle_TimelineFragment jobsFragment;
    private Lifestyle_TimelineFragment tutoringFragment;
    private Lifestyle_TimelineFragment apartmentFragment;

    DatabaseReference reference;




    public static LifestyleFragment newInstance() {
        LifestyleFragment fragment = new LifestyleFragment();
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
        return inflater.inflate(R.layout.fragment_lifestyle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dL = view.findViewById(R.id.dL_lifestyle);
        tb = view.findViewById(R.id.tb_lifestyle);

        tb.setNavigationOnClickListener(view1 -> dL.openDrawer(GravityCompat.START));

        nV = view.findViewById(R.id.nV_lifestyle);
        nV.setNavigationItemSelectedListener(item -> {
            assert getFragmentManager() != null;
            switch (item.getItemId()) {

                case R.id.menu_lifestyle_events:

                    reference = FirebaseDatabase.getInstance().getReference("Posts").child("Events");

                    //Change fragment
                    if (eventFragment == null) {
                        eventFragment = Lifestyle_TimelineFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_lifestyle_fragmentContainer, eventFragment);
                        ft2.commit();
                    }

                    Log.w("LifestyleFragment", "Events selected");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    eventFragment.setReference(reference);
                    if (discountsFragment != null && discountsFragment.isAdded()) ft.hide(discountsFragment);
                    if (specialsFragment != null && specialsFragment.isAdded()) ft.hide(specialsFragment);
                    if (jobsFragment != null && jobsFragment.isAdded()) ft.hide(jobsFragment);
                    if (tutoringFragment != null && tutoringFragment.isAdded()) ft.hide(tutoringFragment);
                    if (apartmentFragment != null && apartmentFragment.isAdded()) ft.hide(apartmentFragment);
                    ft.show(eventFragment);
                    ft.commit();
                    tb.setTitle(getResources().getString(R.string.events));



                    break;

                case R.id.menu_lifestyle_discounts:

                    reference = FirebaseDatabase.getInstance().getReference("Posts").child("Discounts");

                    if (discountsFragment == null) {
                        discountsFragment = Lifestyle_TimelineFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_lifestyle_fragmentContainer, discountsFragment);
                        ft2.commit();
                    }

                    FragmentTransaction ft3 = getFragmentManager().beginTransaction();
                    discountsFragment.setReference(reference);
                    if (eventFragment != null && eventFragment.isAdded()) ft3.hide(eventFragment);
                    if (specialsFragment != null && specialsFragment.isAdded()) ft3.hide(specialsFragment);
                    if (jobsFragment != null && jobsFragment.isAdded()) ft3.hide(jobsFragment);
                    if (tutoringFragment != null && tutoringFragment.isAdded()) ft3.hide(tutoringFragment);
                    if (apartmentFragment != null && apartmentFragment.isAdded()) ft3.hide(apartmentFragment);
                    ft3.hide(eventFragment);
                    ft3.show(discountsFragment);
                    ft3.commit();
                    tb.setTitle(getResources().getString(R.string.discounts));

                    break;

                case R.id.menu_lifestyle_specials:

                    reference = FirebaseDatabase.getInstance().getReference("Posts").child("Specials");

                    if (specialsFragment == null) {
                        specialsFragment = Lifestyle_TimelineFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_lifestyle_fragmentContainer, specialsFragment);
                        ft2.commit();
                    }

                    FragmentTransaction ft4 = getFragmentManager().beginTransaction();
                    specialsFragment.setReference(reference);
                    if (eventFragment != null && eventFragment.isAdded()) ft4.hide(eventFragment);
                    if (discountsFragment != null && discountsFragment.isAdded()) ft4.hide(discountsFragment);
                    if (jobsFragment != null && jobsFragment.isAdded()) ft4.hide(jobsFragment);
                    if (tutoringFragment != null && tutoringFragment.isAdded()) ft4.hide(tutoringFragment);
                    if (apartmentFragment != null && apartmentFragment.isAdded()) ft4.hide(apartmentFragment);
                    ft4.hide(eventFragment);
                    ft4.show(specialsFragment);
                    ft4.commit();
                    tb.setTitle(getResources().getString(R.string.specials));
                    break;

                case R.id.menu_lifestyle_jobs:

                    reference = FirebaseDatabase.getInstance().getReference("Posts").child("Jobs");

                    if (jobsFragment == null) {
                        jobsFragment = Lifestyle_TimelineFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_lifestyle_fragmentContainer, jobsFragment);
                        ft2.commit();
                    }

                    FragmentTransaction ft5 = getFragmentManager().beginTransaction();
                    jobsFragment.setReference(reference);
                    if (eventFragment != null && eventFragment.isAdded()) ft5.hide(eventFragment);
                    if (discountsFragment != null && discountsFragment.isAdded()) ft5.hide(discountsFragment);
                    if (specialsFragment != null && specialsFragment.isAdded()) ft5.hide(specialsFragment);
                    if (tutoringFragment != null && tutoringFragment.isAdded()) ft5.hide(tutoringFragment);
                    if (apartmentFragment != null && apartmentFragment.isAdded()) ft5.hide(apartmentFragment);
                    ft5.hide(eventFragment);
                    ft5.show(jobsFragment);
                    ft5.commit();
                    tb.setTitle(getResources().getString(R.string.jobs));
                    break;

                case R.id.menu_lifestyle_tutoring:

                    reference = FirebaseDatabase.getInstance().getReference("Posts").child("Tutoring");

                    if (tutoringFragment == null) {
                        tutoringFragment = Lifestyle_TimelineFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_lifestyle_fragmentContainer, tutoringFragment);
                        ft2.commit();
                    }

                    FragmentTransaction ft6 = getFragmentManager().beginTransaction();
                    tutoringFragment.setReference(reference);
                    if (eventFragment != null && eventFragment.isAdded()) ft6.hide(eventFragment);
                    if (discountsFragment != null && discountsFragment.isAdded()) ft6.hide(discountsFragment);
                    if (specialsFragment != null && specialsFragment.isAdded()) ft6.hide(specialsFragment);
                    if (jobsFragment != null && jobsFragment.isAdded()) ft6.hide(jobsFragment);
                    if (apartmentFragment != null && apartmentFragment.isAdded()) ft6.hide(apartmentFragment);
                    ft6.hide(eventFragment);
                    ft6.show(tutoringFragment);
                    ft6.commit();
                    tb.setTitle(getResources().getString(R.string.tutoring));
                    break;

                case R.id.menu_lifestyle_apartments:

                    reference = FirebaseDatabase.getInstance().getReference("Posts").child("Apartments");

                    if (apartmentFragment == null) {
                        apartmentFragment = Lifestyle_TimelineFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_lifestyle_fragmentContainer, apartmentFragment);
                        ft2.commit();
                    }

                    FragmentTransaction ft7 = getFragmentManager().beginTransaction();
                    apartmentFragment.setReference(reference);
                    if (eventFragment != null && eventFragment.isAdded()) ft7.hide(eventFragment);
                    if (discountsFragment != null && discountsFragment.isAdded()) ft7.hide(discountsFragment);
                    if (specialsFragment != null && specialsFragment.isAdded()) ft7.hide(specialsFragment);
                    if (jobsFragment != null && jobsFragment.isAdded()) ft7.hide(jobsFragment);
                    if (tutoringFragment != null && tutoringFragment.isAdded()) ft7.hide(tutoringFragment);
                    ft7.hide(eventFragment);
                    ft7.show(apartmentFragment);
                    ft7.commit();
                    tb.setTitle(getResources().getString(R.string.apartments));
                    break;

            }

            dL.closeDrawer(GravityCompat.START);
            return false;
        });

        reference = FirebaseDatabase.getInstance().getReference("Posts").child("Events");
        eventFragment = Lifestyle_TimelineFragment.newInstance();
        eventFragment.setReference(reference);

        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.add(R.id.fl_lifestyle_fragmentContainer, eventFragment);
        ft.commit();





    }

    //@Override
    //public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
    //    switch (item.getItemId()) {
//
    //        case R.id.menu_university_timetable:
    //            Log.w("UniversityFragment", "Menu item Clicked!");
    //            break;
//
    //    }
//
    //    requireActivity().runOnUiThread(() -> dL.closeDrawer(GravityCompat.START));
//
    //    return super.onOptionsItemSelected(item);
//
    //}


}


