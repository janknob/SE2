package de.niceguys.studisapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;

import de.niceguys.studisapp.Model.CurrentUser;

public class Manager {

    private static Manager instance;
    private WeakReference<Context> context;

    public enum Parser {
        degrees, semester, courses, news, modulbook, person, scheduleChanges, event, meals
    }

    private Manager() {

    }

    public static Manager getInstance() {

        if (instance == null) instance = new Manager();

        return instance;

    }

    public SharedPreferences getData(String name) {

        return context.get().getSharedPreferences(name, Context.MODE_PRIVATE );

    }

    public void setContext(Context context) {
        this.context = new WeakReference<Context>(context);
    }

    public Context getContext() {
        return context.get();
    }

    public String getCourseId() {

        //DataSnapshot ds = getUser();
        //return ds.child("courseOfStudyId").getValue(String.class);
        return CurrentUser.getInstance().getDegreeId();

    }

    public String getCourse() {

        //DataSnapshot ds = getUser();
        //return ds.child("courseOfStudy").getValue(String.class);

        return CurrentUser.getInstance().getDegree();
    }

    public String getSemesterId() {

        //DataSnapshot ds = getUser();
        //return ds.child("semesterId").getValue(String.class);
        return CurrentUser.getInstance().getSemesterId();

    }

    public String getSemester() {

        //DataSnapshot ds = getUser();
        //return ds.child("semester").getValue(String.class);
        return CurrentUser.getInstance().getSemester();
    }

}
