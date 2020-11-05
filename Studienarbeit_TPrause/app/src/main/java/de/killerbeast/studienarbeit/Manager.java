package de.killerbeast.studienarbeit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.lang.ref.WeakReference;
import java.util.Objects;

public class Manager extends Activity {

    private static Manager instance = null;
    public static final int REQUEST_UPDATE_SHEDULE_FRAGMENT = 10;
    public static final String STRING_FRAGMENT_SHEDULE = "shedule";
    public static final String STRING_FRAGMENT_TASKS = "tasks";
    public static final String STRING_FRAGMENT_NEWS = "news";
    public static final String STRING_FRAGMENT_SETTINGS = "settings";
    public static final String STRING_FRAGMENT_EVENTS = "events";

    public static final String STRING_SERVER_IP = "tobsns.ddns.net";
    public static final int INTEGER_SERVER_PORT = 9119;

    private WeakReference<Context> context;

    public static Manager getInstance() {

        if (instance == null)
            instance = new Manager();
        return instance;
    }

    public SharedPreferences getSharedPreferences(String name) {

        return context.get().getSharedPreferences(name, MODE_PRIVATE);

    }

    public void setContext(Context context) {

        this.context = new WeakReference<>(context);

    }

    public Context getContext(){

        return context.get();

    }

    public boolean isInternetAvaiable() {

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            return Objects.requireNonNull(Objects.requireNonNull(connectivityManager).getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED ||
                    Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED;
        } catch (Exception e ) {
             e.printStackTrace();
             //responsecode 400 ???
        }

        return false;


    }

}
