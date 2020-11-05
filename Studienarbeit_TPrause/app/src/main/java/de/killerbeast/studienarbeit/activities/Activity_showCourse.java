package de.killerbeast.studienarbeit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import java.util.Objects;

import de.killerbeast.studienarbeit.Course;
import de.killerbeast.studienarbeit.Manager;
import de.killerbeast.studienarbeit.R;
import de.killerbeast.studienarbeit.fragments.Fragment_showCourse;
import de.killerbeast.studienarbeit.fragments.Fragment_showCourse_chat;
import de.killerbeast.studienarbeit.fragments.Fragment_showCourse_prof;
import de.killerbeast.studienarbeit.fragments.Fragment_showCourse_room;
import de.killerbeast.studienarbeit.interfaces.Interface_Fragmenthandler;

public class Activity_showCourse extends AppCompatActivity implements Interface_Fragmenthandler {

    private Fragment_showCourse fragment_showCourse;
    private Fragment_showCourse_prof fragment_showCourse_prof;
    private Fragment_showCourse_room fragment_showCourse_room;
    private Course course;


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_showcourse);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        course = new Course(Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getString("course")));

        Manager.getInstance().setContext(this);

        TextView tv_title = findViewById(R.id.tv_showCourse_name);
        tv_title.setText(course.getCourseName());

        fragment_showCourse = Fragment_showCourse.newInstance(this, course);
        fragment_showCourse_prof = Fragment_showCourse_prof.newInstance(this, course);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fl_showCourse, fragment_showCourse);
        ft.add(R.id.fl_showCourse, fragment_showCourse_prof);
        ft.commit();
        showOverview();

    }

    @Override
    public void updateContext() {

        Manager.getInstance().setContext(this);

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Intent resultIntent = new Intent();
        setResult(5, resultIntent);
        //TODO add resultcode to manager as const
        finish();

    }

    public void showOverview() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(fragment_showCourse);
        ft.hide(fragment_showCourse_prof);
        ft.commit();

    }

    public void showProf(){

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(fragment_showCourse);
        ft.show(fragment_showCourse_prof);
        ft.addToBackStack("");
        ft.commit();

    }

    public void showRoom(Course course){

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment_showCourse_room != null && fragment_showCourse_room.isAdded()) {
            ft.remove(fragment_showCourse_room);
        }
        fragment_showCourse_room = Fragment_showCourse_room.newInstance(course);
        ft.add(R.id.fl_showCourse, fragment_showCourse_room);
        if (fragment_showCourse.isVisible()) ft.hide(fragment_showCourse);
        if (fragment_showCourse_prof.isVisible()) ft.hide(fragment_showCourse_prof);
        ft.addToBackStack("");
        ft.commit();

    }

    public void showChat() {

        Fragment_showCourse_chat fragment_showCourse_chat = Fragment_showCourse_chat.newInstance(this, course);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fl_showCourse, fragment_showCourse_chat);
        ft.hide(fragment_showCourse);
        ft.show(fragment_showCourse_chat);
        ft.addToBackStack("");
        ft.commit();

    }

    @Override
    public void showFragment(String name, Object...args) {

        Log.wtf("showFragment", name);
        switch (name) {

            case "room":
                showRoom((Course)args[0]);
                break;
            case "prof":
                showProf();
                break;
            case "chat":
                showChat();
                break;

        }

    }

    public Activity getActivity() {

        return this;

    }


}
