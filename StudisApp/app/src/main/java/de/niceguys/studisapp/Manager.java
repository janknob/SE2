package de.niceguys.studisapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.ref.WeakReference;

import de.niceguys.studisapp.Model.CurrentUser;

public class Manager {

    private static Manager instance;
    private WeakReference<Context> context;

    public enum Parser {degrees, semester, courses, news, modulbook, person, scheduleChanges, event, meals}

    private Manager() { }

    public static Manager getInstance() {

        if (instance == null) instance = new Manager();

        return instance;

    }

    public SharedPreferences getData(String name) {return context.get().getSharedPreferences(name, Context.MODE_PRIVATE );}

    public void setContext(Context context) {this.context = new WeakReference<>(context);}

    public Context getContext() {return context.get();}

    public String getCourseId() {return CurrentUser.getInstance().getDegreeId();}

    public String getCourse() {return CurrentUser.getInstance().getDegree();}

    public String getSemesterId() {return CurrentUser.getInstance().getSemesterId();}

    public String getSemester() {return CurrentUser.getInstance().getSemester();}

    public static void log(String toLog, Object...args) {

        if (args.length == 1)

            if (args[0] instanceof String)
                Log.println(Log.ASSERT, args[0].toString(), toLog);
            else
                Log.println(Log.ASSERT, args[0].getClass().getSimpleName(), toLog);

        else
            Log.println(Log.ASSERT, "_", toLog);

    }

}
