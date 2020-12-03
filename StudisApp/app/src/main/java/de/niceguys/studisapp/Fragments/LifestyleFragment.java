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
    private Lifestyle_EventsFragment eventsFragment;
    private Lifestyle_DiscountsFragment discountsFragment;
    private Lifestyle_SpecialsFragment specialsFragment;
    private Lifestyle_JobsFragment jobsFragment;
    private Lifestyle_TutoringFragment tutoringFragment;
    private Lifestyle_ApartmentFragment apartmentFragment;
    private Lifestyle_TimelineFragment timelineFragment;


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

                    //Change fragment
                    Log.w("LifestyleFragment", "Events selected");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    if (discountsFragment != null && discountsFragment.isAdded()) ft.hide(discountsFragment);
                    if (specialsFragment != null && specialsFragment.isAdded()) ft.hide(specialsFragment);
                    if (jobsFragment != null && jobsFragment.isAdded()) ft.hide(jobsFragment);
                    if (tutoringFragment != null && tutoringFragment.isAdded()) ft.hide(tutoringFragment);
                    if (apartmentFragment != null && apartmentFragment.isAdded()) ft.hide(apartmentFragment);
                    ft.show(eventsFragment);
                    ft.commit();
                    tb.setTitle(getResources().getString(R.string.events));

                    break;

                case R.id.menu_lifestyle_discounts:

                    /*
                    if (newsFragment == null) {
                        newsFragment = University_NewsFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_university_fragmentContainer, newsFragment);
                        ft2.commit();
                    }


                    FragmentTransaction ft3 = getFragmentManager().beginTransaction();
                    if (eventsFragment != null && eventsFragment.isAdded()) ft3.hide(eventsFragment);
                    if (tasksFragment != null && tasksFragment.isAdded()) ft3.hide(tasksFragment);
                    ft3.hide(timetableFragment);
                    ft3.show(newsFragment);
                    ft3.commit();
                    tb.setTitle(getResources().getString(R.string.news));
                    */
                    break;

                case R.id.menu_lifestyle_specials:

                    /*if (eventsFragment == null) {
                        eventsFragment = University_EventsFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_university_fragmentContainer, eventsFragment);
                        ft2.commit();
                    }

                    FragmentTransaction ft4 = getFragmentManager().beginTransaction();

                    if (newsFragment != null && newsFragment.isAdded()) ft4.hide(newsFragment);
                    if (tasksFragment != null && tasksFragment.isAdded()) ft4.hide(tasksFragment);
                    ft4.hide(timetableFragment);
                    ft4.show(eventsFragment);
                    ft4.commit();
                    tb.setTitle(getResources().getString(R.string.universityEvents));
                    */
                    break;

                case R.id.menu_lifestyle_jobs:

                    /*
                    if (tasksFragment == null) {
                        tasksFragment = University_TasksFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_university_fragmentContainer, tasksFragment);
                        ft2.commit();
                    }

                    FragmentTransaction ft5 = getFragmentManager().beginTransaction();
                    if (newsFragment != null && newsFragment.isAdded()) ft5.hide(newsFragment);
                    if (eventsFragment != null && eventsFragment.isAdded()) ft5.hide(eventsFragment);
                    ft5.hide(timetableFragment);
                    ft5.show(tasksFragment);
                    ft5.commit();
                    tb.setTitle(getResources().getString(R.string.tasks));
                    */
                    break;

                case R.id.menu_lifestyle_tutoring:

                    /*
                    if (tasksFragment == null) {
                        tasksFragment = University_TasksFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_university_fragmentContainer, tasksFragment);
                        ft2.commit();
                    }

                    FragmentTransaction ft5 = getFragmentManager().beginTransaction();
                    if (newsFragment != null && newsFragment.isAdded()) ft5.hide(newsFragment);
                    if (eventsFragment != null && eventsFragment.isAdded()) ft5.hide(eventsFragment);
                    ft5.hide(timetableFragment);
                    ft5.show(tasksFragment);
                    ft5.commit();
                    tb.setTitle(getResources().getString(R.string.tasks));
                    */
                    break;

                case R.id.menu_lifestyle_apartments:

                    /*
                    if (tasksFragment == null) {
                        tasksFragment = University_TasksFragment.newInstance();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_university_fragmentContainer, tasksFragment);
                        ft2.commit();
                    }

                    FragmentTransaction ft5 = getFragmentManager().beginTransaction();
                    if (newsFragment != null && newsFragment.isAdded()) ft5.hide(newsFragment);
                    if (eventsFragment != null && eventsFragment.isAdded()) ft5.hide(eventsFragment);
                    ft5.hide(timetableFragment);
                    ft5.show(tasksFragment);
                    ft5.commit();
                    tb.setTitle(getResources().getString(R.string.tasks));
                    */
                    break;

            }
            dL.closeDrawer(GravityCompat.START);
            return false;
        });

        /*timelineFragment = Lifestyle_TimelineFragment.newInstance();

        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.add(R.id.fl_lifestyle_fragmentContainer, timelineFragment);
        ft.commit();

         */

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


