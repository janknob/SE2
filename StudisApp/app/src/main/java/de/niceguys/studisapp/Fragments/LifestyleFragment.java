package de.niceguys.studisapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;

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
    private Lifestyle_TimelineFragment othersFragment;
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

                case R.id.menu_lifestyle_timeline:


                    FragmentTransaction ft8 = getFragmentManager().beginTransaction();
                    timelineFragment.setCategory("Timeline");
                    if (eventFragment != null && eventFragment.isAdded()) ft8.hide(eventFragment);
                    if (apartmentFragment != null && apartmentFragment.isAdded()) ft8.hide(apartmentFragment);
                    if (discountsFragment != null && discountsFragment.isAdded()) ft8.hide(discountsFragment);
                    if (othersFragment != null && othersFragment.isAdded()) ft8.hide(othersFragment);
                    if (jobsFragment != null && jobsFragment.isAdded()) ft8.hide(jobsFragment);
                    if (tutoringFragment != null && tutoringFragment.isAdded()) ft8.hide(tutoringFragment);
                    ft8.remove(timelineFragment);
                    timelineFragment = Lifestyle_TimelineFragment.newInstance();
                    ft8.add(R.id.fl_lifestyle_fragmentContainer, timelineFragment);
                    timelineFragment.setCategory("Timeline");
                    ft8.show(timelineFragment);
                    ft8.commit();
                    tb.setTitle(getResources().getString(R.string.timeline));
                    break;

                case R.id.menu_lifestyle_events:

                    //reference = FirebaseDatabase.getInstance().getReference("Posts").child("Events");

                    //Change fragment
                    if (eventFragment == null) {
                        eventFragment = Lifestyle_TimelineFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_lifestyle_fragmentContainer, eventFragment);
                        ft2.commit();
                    }

                    Log.w("LifestyleFragment", "Events selected");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    eventFragment.setCategory("Events");
                    if (discountsFragment != null && discountsFragment.isAdded()) ft.hide(discountsFragment);
                    if (othersFragment != null && othersFragment.isAdded()) ft.hide(othersFragment);
                    if (jobsFragment != null && jobsFragment.isAdded()) ft.hide(jobsFragment);
                    if (tutoringFragment != null && tutoringFragment.isAdded()) ft.hide(tutoringFragment);
                    if (apartmentFragment != null && apartmentFragment.isAdded()) ft.hide(apartmentFragment);
                    ft.hide(timelineFragment);
                    ft.show(eventFragment);
                    ft.commit();
                    tb.setTitle(getResources().getString(R.string.events));



                    break;

                case R.id.menu_lifestyle_discounts:

                    //reference = FirebaseDatabase.getInstance().getReference("Posts").child("Discounts");

                    if (discountsFragment == null) {
                        discountsFragment = Lifestyle_TimelineFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_lifestyle_fragmentContainer, discountsFragment);
                        ft2.commit();
                    }

                    FragmentTransaction ft3 = getFragmentManager().beginTransaction();
                    discountsFragment.setCategory("Discounts");
                    if (eventFragment != null && eventFragment.isAdded()) ft3.hide(eventFragment);
                    if (othersFragment != null && othersFragment.isAdded()) ft3.hide(othersFragment);
                    if (jobsFragment != null && jobsFragment.isAdded()) ft3.hide(jobsFragment);
                    if (tutoringFragment != null && tutoringFragment.isAdded()) ft3.hide(tutoringFragment);
                    if (apartmentFragment != null && apartmentFragment.isAdded()) ft3.hide(apartmentFragment);
                    ft3.hide(timelineFragment);
                    ft3.show(discountsFragment);
                    ft3.commit();
                    tb.setTitle(getResources().getString(R.string.discounts));

                    break;


                case R.id.menu_lifestyle_jobs:

                    //reference = FirebaseDatabase.getInstance().getReference("Posts").child("Jobs");

                    if (jobsFragment == null) {
                        jobsFragment = Lifestyle_TimelineFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_lifestyle_fragmentContainer, jobsFragment);
                        ft2.commit();
                    }

                    FragmentTransaction ft5 = getFragmentManager().beginTransaction();
                    jobsFragment.setCategory("Jobs");
                    if (eventFragment != null && eventFragment.isAdded()) ft5.hide(eventFragment);
                    if (discountsFragment != null && discountsFragment.isAdded()) ft5.hide(discountsFragment);
                    if (othersFragment != null && othersFragment.isAdded()) ft5.hide(othersFragment);
                    if (tutoringFragment != null && tutoringFragment.isAdded()) ft5.hide(tutoringFragment);
                    if (apartmentFragment != null && apartmentFragment.isAdded()) ft5.hide(apartmentFragment);
                    ft5.hide(timelineFragment);
                    ft5.show(jobsFragment);
                    ft5.commit();
                    tb.setTitle(getResources().getString(R.string.jobs));
                    break;

                case R.id.menu_lifestyle_tutoring:

                    //reference = FirebaseDatabase.getInstance().getReference("Posts").child("Tutoring");

                    if (tutoringFragment == null) {
                        tutoringFragment = Lifestyle_TimelineFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_lifestyle_fragmentContainer, tutoringFragment);
                        ft2.commit();
                    }

                    FragmentTransaction ft6 = getFragmentManager().beginTransaction();
                    tutoringFragment.setCategory("Tutoring");
                    if (eventFragment != null && eventFragment.isAdded()) ft6.hide(eventFragment);
                    if (discountsFragment != null && discountsFragment.isAdded()) ft6.hide(discountsFragment);
                    if (othersFragment != null && othersFragment.isAdded()) ft6.hide(othersFragment);
                    if (jobsFragment != null && jobsFragment.isAdded()) ft6.hide(jobsFragment);
                    if (apartmentFragment != null && apartmentFragment.isAdded()) ft6.hide(apartmentFragment);
                    ft6.hide(timelineFragment);
                    ft6.show(tutoringFragment);
                    ft6.commit();
                    tb.setTitle(getResources().getString(R.string.tutoring));
                    break;

                case R.id.menu_lifestyle_apartments:

                    //reference = FirebaseDatabase.getInstance().getReference("Posts").child("Apartments");

                    if (apartmentFragment == null) {
                        apartmentFragment = Lifestyle_TimelineFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_lifestyle_fragmentContainer, apartmentFragment);
                        ft2.commit();
                    }

                    FragmentTransaction ft7 = getFragmentManager().beginTransaction();
                    apartmentFragment.setCategory("Apartments");
                    if (eventFragment != null && eventFragment.isAdded()) ft7.hide(eventFragment);
                    //if (eventFragment != null && eventFragment.isAdded()) ft7.hide(eventFragment);
                    if (discountsFragment != null && discountsFragment.isAdded()) ft7.hide(discountsFragment);
                    if (othersFragment != null && othersFragment.isAdded()) ft7.hide(othersFragment);
                    if (jobsFragment != null && jobsFragment.isAdded()) ft7.hide(jobsFragment);
                    if (tutoringFragment != null && tutoringFragment.isAdded()) ft7.hide(tutoringFragment);
                    ft7.hide(timelineFragment);
                    ft7.show(apartmentFragment);
                    ft7.commit();
                    tb.setTitle(getResources().getString(R.string.apartments));
                    break;

                case R.id.menu_lifestyle_others:

                    //reference = FirebaseDatabase.getInstance().getReference("Posts").child("Specials");

                    if (othersFragment == null) {
                        othersFragment = Lifestyle_TimelineFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_lifestyle_fragmentContainer, othersFragment);
                        ft2.commit();
                    }

                    FragmentTransaction ft4 = getFragmentManager().beginTransaction();
                    othersFragment.setCategory("Others");
                    if (eventFragment != null && eventFragment.isAdded()) ft4.hide(eventFragment);
                    if (discountsFragment != null && discountsFragment.isAdded()) ft4.hide(discountsFragment);
                    if (jobsFragment != null && jobsFragment.isAdded()) ft4.hide(jobsFragment);
                    if (tutoringFragment != null && tutoringFragment.isAdded()) ft4.hide(tutoringFragment);
                    if (apartmentFragment != null && apartmentFragment.isAdded()) ft4.hide(apartmentFragment);
                    ft4.hide(timelineFragment);
                    ft4.show(othersFragment);
                    ft4.commit();
                    tb.setTitle(getResources().getString(R.string.others));
                    break;

            }

            dL.closeDrawer(GravityCompat.START);
            return false;
        });

        //reference = FirebaseDatabase.getInstance().getReference("Posts").child("Events");
        timelineFragment = Lifestyle_TimelineFragment.newInstance();
        timelineFragment.setCategory("Timeline");

        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.add(R.id.fl_lifestyle_fragmentContainer, timelineFragment);
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


