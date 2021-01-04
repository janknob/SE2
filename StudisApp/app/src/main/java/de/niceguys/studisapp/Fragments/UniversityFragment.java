package de.niceguys.studisapp.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import de.niceguys.studisapp.Manager;
import de.niceguys.studisapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UniversityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UniversityFragment extends Fragment {

    private DrawerLayout dL;
    private Toolbar tb;
    private NavigationView nV;
    private University_TimetableFragment timetableFragment;
    private University_NewsFragment newsFragment;
    private University_EventsFragment eventsFragment;
    private University_TasksFragment tasksFragment;
    private University_MealsFragment mealsFragment;

    public static UniversityFragment newInstance() {
        UniversityFragment fragment = new UniversityFragment();
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
        return inflater.inflate(R.layout.fragment_university, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dL = view.findViewById(R.id.dL_university);
        tb = view.findViewById(R.id.tb_university);

        tb.setNavigationOnClickListener(view1 -> dL.openDrawer(GravityCompat.START));

        nV = view.findViewById(R.id.nV_university);
        nV.setNavigationItemSelectedListener(item -> {
            assert requireActivity().getSupportFragmentManager() != null;
            switch (item.getItemId()) {

                case R.id.menu_university_timetable:

                    //Change fragment
                    Log.w("UniversityFragment", "Timetable selected");
                    Manager.getInstance().getData("settings").edit().putBoolean("downloadShedule", true).apply();
                    FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                    if (newsFragment != null && newsFragment.isAdded()) ft.hide(newsFragment);
                    if (eventsFragment != null && eventsFragment.isAdded()) ft.hide(eventsFragment);
                    if (tasksFragment != null && tasksFragment.isAdded()) ft.hide(tasksFragment);
                    if (mealsFragment != null && mealsFragment.isAdded()) ft.hide(mealsFragment);
                    ft.remove(timetableFragment);
                    timetableFragment = University_TimetableFragment.newInstance();
                    ft.add(R.id.fl_university_fragmentContainer, timetableFragment);
                    ft.show(timetableFragment);
                    ft.commit();
                    tb.setTitle(getResources().getString(R.string.timetable));

                    break;

                case R.id.menu_university_news:

                    if (newsFragment == null) {
                        newsFragment = University_NewsFragment.newInstance();
                        FragmentTransaction ft2 = requireActivity().getSupportFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_university_fragmentContainer, newsFragment);
                        ft2.commit();
                    }


                    FragmentTransaction ft3 = requireActivity().getSupportFragmentManager().beginTransaction();
                    if (mealsFragment != null && mealsFragment.isAdded()) ft3.hide(mealsFragment);
                    if (eventsFragment != null && eventsFragment.isAdded()) ft3.hide(eventsFragment);
                    if (tasksFragment != null && tasksFragment.isAdded()) ft3.hide(tasksFragment);
                    ft3.hide(timetableFragment);
                    ft3.show(newsFragment);
                    ft3.commit();
                    tb.setTitle(getResources().getString(R.string.news));

                    break;

                case R.id.menu_university_events:

                    if (eventsFragment == null) {
                        eventsFragment = University_EventsFragment.newInstance();
                        FragmentTransaction ft2 = requireActivity().getSupportFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_university_fragmentContainer, eventsFragment);
                        ft2.commit();
                    }

                    FragmentTransaction ft4 = requireActivity().getSupportFragmentManager().beginTransaction();

                    if (newsFragment != null && newsFragment.isAdded()) ft4.hide(newsFragment);
                    if (tasksFragment != null && tasksFragment.isAdded()) ft4.hide(tasksFragment);
                    if (mealsFragment != null && mealsFragment.isAdded()) ft4.hide(mealsFragment);
                    ft4.hide(timetableFragment);
                    ft4.show(eventsFragment);
                    ft4.commit();
                    tb.setTitle(getResources().getString(R.string.universityEvents));

                    break;

                case R.id.menu_university_tasks:

                    if (tasksFragment == null) {
                        tasksFragment = University_TasksFragment.newInstance();
                        FragmentTransaction ft2 = requireActivity().getSupportFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_university_fragmentContainer, tasksFragment);
                        ft2.commit();
                    }

                    FragmentTransaction ft5 = requireActivity().getSupportFragmentManager().beginTransaction();
                    if (newsFragment != null && newsFragment.isAdded()) ft5.hide(newsFragment);
                    if (eventsFragment != null && eventsFragment.isAdded()) ft5.hide(eventsFragment);
                    if (mealsFragment != null && mealsFragment.isAdded()) ft5.hide(mealsFragment);
                    ft5.hide(timetableFragment);
                    ft5.show(tasksFragment);
                    ft5.commit();
                    tb.setTitle(getResources().getString(R.string.tasks));

                    break;


                case R.id.menu_university_meals:

                    if (mealsFragment == null) {
                        mealsFragment = University_MealsFragment.newInstance();
                        FragmentTransaction ft2 = requireActivity().getSupportFragmentManager().beginTransaction();
                        ft2.add(R.id.fl_university_fragmentContainer, mealsFragment);
                        ft2.commit();
                    }

                    FragmentTransaction ft6 = requireActivity().getSupportFragmentManager().beginTransaction();
                    if (newsFragment != null && newsFragment.isAdded()) ft6.hide(newsFragment);
                    if (eventsFragment != null && eventsFragment.isAdded()) ft6.hide(eventsFragment);
                    if (tasksFragment != null && tasksFragment.isAdded()) ft6.hide(tasksFragment);
                    ft6.hide(timetableFragment);
                    ft6.show(mealsFragment);
                    ft6.commit();
                    tb.setTitle(getResources().getString(R.string.meals));

                    break;
            }
            dL.closeDrawer(GravityCompat.START);
            return false;
        });

        timetableFragment = University_TimetableFragment.newInstance();

        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.add(R.id.fl_university_fragmentContainer, timetableFragment);
        ft.commit();

    }

    //TODO  ???
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


