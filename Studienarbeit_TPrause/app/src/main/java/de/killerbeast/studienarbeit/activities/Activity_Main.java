package de.killerbeast.studienarbeit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import de.killerbeast.studienarbeit.Course;
import de.killerbeast.studienarbeit.Manager;
import de.killerbeast.studienarbeit.R;
import de.killerbeast.studienarbeit.UpdateChecker;
import de.killerbeast.studienarbeit.fragments.Fragment_events;
import de.killerbeast.studienarbeit.fragments.Fragment_news;
import de.killerbeast.studienarbeit.fragments.Fragment_settings;
import de.killerbeast.studienarbeit.fragments.Fragment_shedule;
import de.killerbeast.studienarbeit.fragments.Fragment_tasks;
import de.killerbeast.studienarbeit.interfaces.Interface_Fragmenthandler;
import de.killerbeast.studienarbeit.interfaces.Interface_UpdateChecker;

public class Activity_Main extends AppCompatActivity implements Interface_Fragmenthandler, Interface_UpdateChecker {

    private Fragment_shedule fragment_shedule;
    private Fragment_tasks fragment_tasks;
    private Fragment_news fragment_news;
    private Fragment_settings fragment_settings;
    private Fragment_events fragment_events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Manager.getInstance().setContext(this);
        checkForUpdates(false);
        if (savedInstanceState != null) {

            Log.wtf("savedInstance", savedInstanceState.toString());
            List<Fragment> ft = getSupportFragmentManager().getFragments();
            Manager.getInstance().setContext(this);
            Log.wtf("FragmentList", "" + ft.size());

            FragmentTransaction fT = getSupportFragmentManager().beginTransaction();

            for (Fragment f : ft) {

                System.out.println(f.getId());
                fT.remove(f);

            }

            fragment_shedule = Fragment_shedule.newInstance(this);
            fragment_tasks = Fragment_tasks.newInstance(this);
            fragment_settings = Fragment_settings.newInstance(this);
            fragment_news = Fragment_news.newInstance(this);
            fragment_events = Fragment_events.newInstance(this);
            fT.add(R.id.fl_main_container, fragment_tasks, Manager.STRING_FRAGMENT_TASKS);
            fT.add(R.id.fl_main_container, fragment_settings, Manager.STRING_FRAGMENT_SETTINGS);
            fT.add(R.id.fl_main_container, fragment_shedule, Manager.STRING_FRAGMENT_SHEDULE);
            fT.add(R.id.fl_main_container, fragment_news, Manager.STRING_FRAGMENT_NEWS);
            fT.add(R.id.fl_main_container, fragment_events, Manager.STRING_FRAGMENT_EVENTS);
            fT.hide(fragment_settings);
            fT.hide(fragment_tasks);
            fT.hide(fragment_shedule);
            fT.hide(fragment_news);
            fT.hide(fragment_events);
            fT.commit();


        } else {

            if (getSupportFragmentManager().findFragmentByTag(Manager.STRING_FRAGMENT_SHEDULE) == null) {

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                fragment_shedule = Fragment_shedule.newInstance(this);
                fragment_tasks = Fragment_tasks.newInstance(this);
                fragment_settings = Fragment_settings.newInstance(this);
                ft.add(R.id.fl_main_container, fragment_tasks, Manager.STRING_FRAGMENT_TASKS);
                ft.add(R.id.fl_main_container, fragment_settings, Manager.STRING_FRAGMENT_SETTINGS);
                ft.add(R.id.fl_main_container, fragment_shedule, Manager.STRING_FRAGMENT_SHEDULE);
                ft.show(fragment_shedule);

                ft.commit();

            } else {

                fragment_shedule = (Fragment_shedule) getSupportFragmentManager().findFragmentByTag(Manager.STRING_FRAGMENT_SHEDULE);
                fragment_tasks = (Fragment_tasks) getSupportFragmentManager().findFragmentByTag(Manager.STRING_FRAGMENT_TASKS);
                fragment_settings = (Fragment_settings) getSupportFragmentManager().findFragmentByTag(Manager.STRING_FRAGMENT_SETTINGS);

            }

        }

        Toolbar tb = findViewById(R.id.tb_main);
        setSupportActionBar(tb);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);
        bottomNavigationView.setSelectedItemId(R.id.menu_shedule);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.menu_shedule:
                    showShedule();
                    break;
                case R.id.menu_tasks:
                    showTasks();
                    break;
                case R.id.menu_news:
                    showNews();
                    break;
                case R.id.menu_settings:
                    showSettings();
                    break;

                case R.id.menu_events:
                    showEvents();
                    break;

            }

            return true;

        });

        if (getIntent().getExtras() != null) {
            if (Objects.requireNonNull(getIntent().getExtras()).containsKey("select")) {

                String which = (String) getIntent().getExtras().get("select");

                assert which != null;
                switch (which) {

                    case "shedule":

                        showShedule();
                        bottomNavigationView.setSelectedItemId(R.id.menu_shedule);
                        break;

                    case "tasks":

                        showTasks();
                        bottomNavigationView.setSelectedItemId(R.id.menu_tasks);
                        break;

                    case "news":

                        showNews();
                        bottomNavigationView.setSelectedItemId(R.id.menu_news);
                        break;

                    case "settings":

                        showSettings();
                        bottomNavigationView.setSelectedItemId(R.id.menu_settings);
                        break;

                    case "events":
                        showEvents();
                        bottomNavigationView.setSelectedItemId(R.id.menu_events);
                        break;

                }

            } else showShedule();

        }
        else
        if (savedInstanceState != null && savedInstanceState.get("select") != null) {
            Log.wtf("Saved Instance State", savedInstanceState.toString());
            String which = (String) savedInstanceState.get("select");
            assert which != null;
            switch (which) {

                case "shedule":

                    showShedule();
                    bottomNavigationView.setSelectedItemId(R.id.menu_shedule);
                    break;

                case "tasks":

                    showTasks();
                    bottomNavigationView.setSelectedItemId(R.id.menu_tasks);
                    break;

                case "news":

                    showNews();
                    bottomNavigationView.setSelectedItemId(R.id.menu_news);
                    break;

                case "settings":

                    showSettings();
                    bottomNavigationView.setSelectedItemId(R.id.menu_settings);
                    break;

                case "events":
                    showEvents();
                    bottomNavigationView.setSelectedItemId(R.id.menu_events);
                    break;
            }
        }
        else showShedule();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);


        if (fragment_news != null && fragment_news.isVisible()) outState.putString("select", "news");
        if (fragment_shedule.isVisible()) outState.putString("select", "shedule");
        if (fragment_tasks.isVisible()) outState.putString("select", "tasks");
        if (fragment_settings.isVisible()) outState.putString("select", "settings");

    }

    @Override

    public void updateFragment(String which) {

        if (Manager.STRING_FRAGMENT_SHEDULE.equals(which)) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(fragment_shedule);
            fragment_shedule = Fragment_shedule.newInstance(this);
            ft.add(R.id.fl_main_container, fragment_shedule, "shedule");
            ft.commit();
            showShedule();

        }
        else if (Manager.STRING_FRAGMENT_NEWS.equals(which)) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (fragment_news.isAdded())  ft.remove(fragment_news);
            fragment_news = null;
            ft.commit();
            showNews();

        }
        else if (Manager.STRING_FRAGMENT_TASKS.equals(which)) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(fragment_tasks);
            fragment_tasks = Fragment_tasks.newInstance(this);
            ft.add(R.id.fl_main_container, fragment_tasks, "tasks");
            ft.commit();
            showTasks();

        } else if (Manager.STRING_FRAGMENT_EVENTS.equals(which)) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (fragment_events.isAdded())  ft.remove(fragment_events);
            fragment_events = null;
            ft.commit();
            showEvents();

        }

    }

    private void showShedule() {

        ((Toolbar) findViewById(R.id.tb_main)).setTitle(getResources().getString(R.string.shedule));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(fragment_tasks);
        if (fragment_news != null && fragment_news.isAdded()) ft.hide(fragment_news);
        ft.hide(fragment_settings);
        ft.show(fragment_shedule);
        if (fragment_events != null && fragment_events.isAdded()) ft.hide(fragment_events);
        ft.commit();

    }

    private void showTasks() {

        ((Toolbar) findViewById(R.id.tb_main)).setTitle(getResources().getString(R.string.tasks));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(fragment_shedule);
        ft.show(fragment_tasks);
        if (fragment_news != null && fragment_news.isAdded()) ft.hide(fragment_news);
        ft.hide(fragment_settings);
        if (fragment_events != null && fragment_events.isAdded()) ft.hide(fragment_events);
        ft.commit();

    }

    private void showNews() {

        ((Toolbar) findViewById(R.id.tb_main)).setTitle(getResources().getString(R.string.news));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment_news == null || !fragment_news.isAdded()) {

            fragment_news = Fragment_news.newInstance(this);

            ft.add(R.id.fl_main_container, fragment_news, "news");

        }
        if (fragment_events != null && fragment_events.isAdded()) ft.hide(fragment_events);
        ft.hide(fragment_shedule);
        ft.hide(fragment_tasks);
        ft.hide(fragment_settings);
        ft.show(fragment_news);
        ft.commit();

    }

    private void showSettings() {

        ((Toolbar) findViewById(R.id.tb_main)).setTitle(getResources().getString(R.string.settings));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(fragment_shedule);
        ft.hide(fragment_tasks);
        if (fragment_news != null && fragment_news.isAdded()) ft.hide(fragment_news);
        if (fragment_events != null && fragment_events.isAdded()) ft.hide(fragment_events);
        ft.show(fragment_settings);
        ft.commit();

    }

    private void showEvents() {

        ((Toolbar) findViewById(R.id.tb_main)).setTitle(getResources().getString(R.string.events));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (fragment_events == null || !fragment_events.isAdded()) {

            fragment_events = Fragment_events.newInstance(this);

            ft.add(R.id.fl_main_container, fragment_events, "events");

        }

        ft.hide(fragment_shedule);
        ft.hide(fragment_tasks);
        if (fragment_news != null && fragment_news.isAdded()) ft.hide(fragment_news);
        ft.hide(fragment_settings);
        ft.show(fragment_events);
        ft.commit();


    }

    public void updateContext() {

        Manager.getInstance().setContext(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        Context cw = new ContextThemeWrapper(this, R.style.popupmenu);
        PopupMenu popupMenu = new PopupMenu(cw, findViewById(R.id.toolbarOption));
        MenuInflater menuInflater = popupMenu.getMenuInflater();

        if (fragment_shedule.isVisible()) {

            menuInflater.inflate(R.menu.shedule_menu, popupMenu.getMenu());
            popupMenu.getMenu().findItem(R.id.mi_shedule_removeFilter).setVisible(!Objects.equals(getSharedPreferences("settings", MODE_PRIVATE).getString("courseFilters", ""), ""));

        } else if (fragment_tasks.isVisible()) {

            menuInflater.inflate(R.menu.tasks_menu, popupMenu.getMenu());
            popupMenu.getMenu().findItem(R.id.mi_tasks_deleteFinished).setChecked(getSharedPreferences("settings", MODE_PRIVATE).getBoolean("deleteFinishedTasks", false));

        } else if (fragment_news != null && fragment_news.isVisible()) {

            menuInflater.inflate(R.menu.news_menu, popupMenu.getMenu());

        } else if (fragment_settings.isVisible()) {

            menuInflater.inflate(R.menu.settings_menu, popupMenu.getMenu());

        } else if (fragment_events.isVisible()) {

            menuInflater.inflate(R.menu.events_menu, popupMenu.getMenu());

        }

        popupMenu.setOnMenuItemClickListener(item -> {
            Context cw2 = new ContextThemeWrapper(Manager.getInstance().getContext(), R.style.dialogMenu);
            AlertDialog.Builder warning = new AlertDialog.Builder(cw2);
            AlertDialog warn;
            SharedPreferences.Editor editor;
            ArrayList<String> arrayList;
            ArrayList<Course> courses = new ArrayList<>();
            for (Object s : getSharedPreferences("courses", MODE_PRIVATE).getAll().values()) {

                courses.add(new Course((String)s));

            }

            switch (item.getItemId()) {

                case R.id.mi_shedule_refresh:

                    getSharedPreferences("settings", MODE_PRIVATE).edit().putBoolean("downloadShedule", true).apply();
                    updateFragment(Manager.STRING_FRAGMENT_SHEDULE);
                    break;

                case R.id.mi_shedule_edit:

                    startActivityForResult(new Intent().setClass(this, Activity_editShedule.class), Manager.REQUEST_UPDATE_SHEDULE_FRAGMENT);
                    break;

                case R.id.mi_shedule_removeFilter:

                    editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
                    editor.remove("courseFilters");
                    editor.apply();
                    updateFragment(Manager.STRING_FRAGMENT_SHEDULE);

                    break;


                case R.id.mi_shedule_course:


                    warning.setTitle(getResources().getString(R.string.course));
                    warning.setMessage(this.getResources().getString(R.string.chooseValue));
                    Spinner spinner = new Spinner(getApplicationContext());
                    warning.setPositiveButton(this.getResources().getString(R.string.save), (dialog, which) -> {

                        getSharedPreferences("settings", MODE_PRIVATE).edit()
                                .putString("courseFilters", String.format("coursename|%s", spinner.getSelectedItem().toString()))
                                .apply();
                        updateFragment(Manager.STRING_FRAGMENT_SHEDULE);

                    });

                    arrayList = new ArrayList<>();
                {

                    for (Course c : courses)
                        if (!arrayList.contains(c.getCourseName()))
                            arrayList.add(c.getCourseName());

                    Collections.sort(arrayList);

                }

                spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList));
                warning.setView(spinner);
                warn = warning.create();
                warn.show();

                break;

                case R.id.mi_shedule_prof:

                    warning.setTitle(getResources().getString(R.string.prof));
                    warning.setMessage(this.getResources().getString(R.string.chooseValue));
                    spinner = new Spinner(getApplicationContext());
                    warning.setPositiveButton(this.getResources().getString(R.string.save), (dialog, which) -> {

                        getSharedPreferences("settings", MODE_PRIVATE).edit()
                                .putString("courseFilters", String.format("professor|%s", spinner.getSelectedItem().toString()))
                                .apply();
                        updateFragment(Manager.STRING_FRAGMENT_SHEDULE);

                    });

                    arrayList = new ArrayList<>();
                {

                    for (Course c : courses)
                        if (!arrayList.contains(c.getProfessor())) arrayList.add(c.getProfessor());

                    Collections.sort(arrayList);

                }

                spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList));
                warning.setView(spinner);
                warn = warning.create();
                warn.show();

                break;

                case R.id.mi_shedule_startTime:

                    spinner = new Spinner(getApplicationContext());
                    warning.setTitle(getResources().getString(R.string.startTime));
                    warning.setMessage(this.getResources().getString(R.string.chooseValue));
                    warning.setPositiveButton(this.getResources().getString(R.string.save), (dialog, which) -> {

                        getSharedPreferences("settings", MODE_PRIVATE).edit()
                                .putString("courseFilters", String.format("starttime|%s", spinner.getSelectedItem().toString()))
                                .apply();
                        updateFragment(Manager.STRING_FRAGMENT_SHEDULE);

                    });

                    arrayList = new ArrayList<>();

                {

                    for (Course c : courses)
                        if (!arrayList.contains(c.getStartTime())) arrayList.add(c.getStartTime());

                    Collections.sort(arrayList);

                }

                spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList));
                warning.setView(spinner);
                warn = warning.create();
                warn.show();

                break;

                case R.id.mi_shedule_endTime:

                    warning.setTitle(getResources().getString(R.string.endTime));
                    warning.setMessage(this.getResources().getString(R.string.chooseValue));
                    spinner = new Spinner(getApplicationContext());
                    warning.setPositiveButton(this.getResources().getString(R.string.save), (dialog, which) -> {

                        getSharedPreferences("settings", MODE_PRIVATE).edit()
                                .putString("courseFilters", String.format("endtime|%s", spinner.getSelectedItem().toString()))
                                .apply();
                        showShedule();
                        updateFragment(Manager.STRING_FRAGMENT_SHEDULE);

                    });

                    arrayList = new ArrayList<>();

                {

                    for (Course c : courses)
                        if (!arrayList.contains(c.getEndTime())) arrayList.add(c.getEndTime());

                    Collections.sort(arrayList);

                }

                spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList));
                warning.setView(spinner);
                warn = warning.create();
                warn.show();

                break;

                case R.id.mi_shedule_room:

                    warning.setTitle(getResources().getString(R.string.room));
                    warning.setMessage(this.getResources().getString(R.string.chooseValue));
                    spinner = new Spinner(getApplicationContext());
                    warning.setPositiveButton(this.getResources().getString(R.string.save), (dialog, which) -> {

                        getSharedPreferences("settings", MODE_PRIVATE).edit()
                                .putString("courseFilters", String.format("room|%s", spinner.getSelectedItem().toString()))
                                .apply();
                        updateFragment(Manager.STRING_FRAGMENT_SHEDULE);

                    });

                    arrayList = new ArrayList<>();

                {

                    for (Course c : courses)
                        if (!arrayList.contains(c.getRoomNumber()))
                            arrayList.add(c.getRoomNumber());

                    Collections.sort(arrayList);

                }

                spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList));
                warning.setView(spinner);
                warn = warning.create();
                warn.show();

                break;

                case R.id.mi_shedule_type:

                    warning.setTitle(getResources().getString(R.string.kind));
                    warning.setMessage(this.getResources().getString(R.string.chooseValue));
                    spinner = new Spinner(getApplicationContext());
                    warning.setPositiveButton(this.getResources().getString(R.string.save), (dialog, which) -> {

                        getSharedPreferences("settings", MODE_PRIVATE).edit()
                                .putString("courseFilters", String.format("kind|%s", spinner.getSelectedItem().toString()))
                                .apply();
                        updateFragment(Manager.STRING_FRAGMENT_SHEDULE);

                    });


                    arrayList = new ArrayList<>();

                {

                    for (Course c : courses)
                        if (!arrayList.contains(c.getKind())) arrayList.add(c.getKind());

                    Collections.sort(arrayList);

                }

                spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList));
                warning.setView(spinner);
                warn = warning.create();
                warn.show();

                break;

            } //for shedule

            switch (item.getItemId()) {

                case R.id.mi_tasks_deleteFinished:

                    getSharedPreferences("settings", MODE_PRIVATE).edit().putBoolean("deleteFinishedTasks", !getSharedPreferences("settings", MODE_PRIVATE).getBoolean("deleteFinishedTasks", false)).apply();

                    break;

                case R.id.mi_tasks_del:

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle(getResources().getString(R.string.howToDeleteTasks));
                    alertDialog.setPositiveButton(getResources().getString(R.string.OK), null);
                    AlertDialog warn2 = alertDialog.create();
                    warn2.show();

                    break;


            } //for tasks

            switch (item.getItemId()) {

                case R.id.mi_news_refresh:

                    getSharedPreferences("news", MODE_PRIVATE).edit().clear().apply();
                    updateFragment(Manager.STRING_FRAGMENT_NEWS);
                    break;

                case R.id.mi_news_towebsite:
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.url_news)));
                    startActivity(browserIntent);
                    break;


            } //for news

            switch (item.getItemId()) {

                case R.id.mi_settings_info:
                    startActivityForResult(new Intent().setClass(this, Activity_about.class), 11);
                    break;

                case R.id.mi_settings_updates:
                    checkForUpdates(true);
                    break;

            } //for settings

            switch (item.getItemId()) {

                case R.id.mi_events_refresh:

                    getSharedPreferences("events", MODE_PRIVATE).edit().clear().apply();
                    updateFragment(Manager.STRING_FRAGMENT_EVENTS);
                    break;

                case R.id.mi_events_towebsite:
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.url_events)));
                    startActivity(browserIntent);
                    break;

            } //for events

            return true;

        });

        popupMenu.show();

        return true;
    }

    private void checkForUpdates(boolean showNotification) {

        String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 10 );
        } else {

            new UpdateChecker(this, showNotification).execute(getResources().getString(R.string.url_versionCheck));

        }

    }

    @Override
    public void  onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkForUpdates(false);
            } else {
                Toast.makeText(this, getResources().getString(R.string.warning_noStorageWritePermission), Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void showUpToDate() {

        Snackbar.make(findViewById(R.id.fl_main_container), R.string.upToDate, BaseTransientBottomBar.LENGTH_LONG).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateContext();
        if (requestCode == Manager.REQUEST_UPDATE_SHEDULE_FRAGMENT) updateFragment(Manager.STRING_FRAGMENT_SHEDULE);

    }

    public void dismissTextview(View view) {
        view.setVisibility(View.GONE);
    }

}
