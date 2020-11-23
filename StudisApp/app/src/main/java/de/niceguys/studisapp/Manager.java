package de.niceguys.studisapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;

public class Manager {

    private static Manager instance;
    private WeakReference<Context> context;

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

    public String getCourse() {

        return "MC"; //TODO: this is a dummy! Replace with actual fetching from database!

    }

    public String getSemester() {

        return "3%23WS%232020"; //TODO: this is a dummy! Replace with actual fetching from database!

    }

    public String getSemesterFull() {

        return "3 - WS 2020"; //TODO: this is a dummy! Replace with actual fetching from database!

    }

}
