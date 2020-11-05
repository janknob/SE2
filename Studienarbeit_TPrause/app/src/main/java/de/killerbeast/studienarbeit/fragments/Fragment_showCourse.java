package de.killerbeast.studienarbeit.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.tabs.TabLayout;
import java.util.Objects;

import de.killerbeast.studienarbeit.Course;
import de.killerbeast.studienarbeit.Manager;
import de.killerbeast.studienarbeit.R;
import de.killerbeast.studienarbeit.interfaces.Interface_Fragmenthandler;

public class Fragment_showCourse extends Fragment implements Interface_Fragmenthandler {

    private Interface_Fragmenthandler parent;
    private Fragment_showCourse_overview fragment_showCourse_overview;
    private Fragment_showCourse_modulbook fragment_showCourse_modulbook;
    private Fragment_showCourse_notes fragment_showCourse_notes;
    private Course course;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_showcourse, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    public static Fragment_showCourse newInstance(Interface_Fragmenthandler parent, Course course) {

        Fragment_showCourse fragment = new Fragment_showCourse();
        fragment.setParent(parent);
        Bundle args = new Bundle();
        args.putString("course", course.saveCourse());
        fragment.setArguments(args);
        return fragment;

    }

    private void setParent(Interface_Fragmenthandler parent) {

        this.parent = parent;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view,savedInstance);

        if (requireArguments().containsKey("course")) course = new Course(Objects.requireNonNull(requireArguments().getString("course")));

        fragment_showCourse_overview = Fragment_showCourse_overview.newInstance(this, course);
        fragment_showCourse_notes = Fragment_showCourse_notes.newInstance(this, course);

        TabLayout tabLayout = view.findViewById(R.id.tab_showcourse);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {

                    case 0:

                        showOvierview();

                        break;

                    case 1:

                        showModulbook();

                        break;

                    case 2:

                        showNotes();

                        break;


                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.add(R.id.fl_showcourse_tab, fragment_showCourse_notes);
        ft.add(R.id.fl_showcourse_tab, fragment_showCourse_overview);
        ft.hide(fragment_showCourse_notes);
        ft.commit();

    }

    private void showOvierview(){

        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        if (fragment_showCourse_modulbook != null && fragment_showCourse_modulbook.isAdded()) ft.hide(fragment_showCourse_modulbook);
        ft.hide(fragment_showCourse_notes);
        ft.show(fragment_showCourse_overview);
        ft.commit();

    }

    private void showModulbook() {

        if (fragment_showCourse_modulbook == null) fragment_showCourse_modulbook = Fragment_showCourse_modulbook.newInstance(course);

        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        if (!fragment_showCourse_modulbook.isAdded()) ft.add(R.id.fl_showcourse_tab, fragment_showCourse_modulbook);
        ft.show(fragment_showCourse_modulbook);
        ft.hide(fragment_showCourse_notes);
        ft.hide(fragment_showCourse_overview);
        ft.commit();

    }

    private void showNotes() {

        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        if (fragment_showCourse_modulbook != null && fragment_showCourse_modulbook.isAdded()) ft.hide(fragment_showCourse_modulbook);
        ft.show(fragment_showCourse_notes);
        ft.hide(fragment_showCourse_overview);
        ft.commit();

    }

    public void updateFragment(String which) {

        if (which.equals(Manager.getInstance().getContext().getResources().getString(R.string.notes))) {

            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.remove(fragment_showCourse_notes);
            fragment_showCourse_notes = Fragment_showCourse_notes.newInstance(this, course);
            ft.add(R.id.fl_showcourse_tab, fragment_showCourse_notes);
            ft.commit();
            showNotes();

        }

    }

    public void showFragment(String fragmentname, Object...args) {

        parent.showFragment(fragmentname, args);

    }

}
