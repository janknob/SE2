package de.niceguys.studisapp;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Locale;

import de.niceguys.studisapp.Fragments.LifestyleFragment;
import de.niceguys.studisapp.Fragments.ProfileFragment;
import de.niceguys.studisapp.Fragments.UniversityFragment;

public class MainActivity extends AppCompatActivity {

    Fragment selectedFragment = null;
    BottomNavigationView bottomNavigationView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the view with XML file acitvity_main which contains the bottom navigation bar
        Manager.log("1", this);
        Manager.getInstance().setContext(this);
        Manager.log("2", this);
        setLocale();
        Manager.log("3", this);


        switch (getSharedPreferences("settings", MODE_PRIVATE).getString("appTheme", "Hell")) {

            case "Hell":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "Dunkel":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;

        }
        setTheme(R.style.Theme_StudisApp);
        setContentView(R.layout.activity_main);

        Manager.log("4", this);


        if (savedInstanceState != null) {
            savedInstanceState.clear();

            FragmentTransaction ftToKill = getSupportFragmentManager().beginTransaction();


            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

            for (Fragment toClear : fragmentList) {

                ftToKill.remove(toClear);

            }



            ftToKill.commit();

        }

        Manager.log("5", this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_lifestyle:
                    selectedFragment = new LifestyleFragment();
                    break;
                case R.id.nav_university:

                    //TO-DO

                    if (Manager.getInstance().getData("settings").getBoolean("UniversityStuff_selected", false))
                    {
                        selectedFragment = UniversityFragment.newInstance();
                    }
                    else
                    {
                        selectedFragment = new ProfileFragment();
                        openDialog();
                    }

                    break;
                case R.id.nav_profile:
                    //SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    //editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    //editor.apply();
                    selectedFragment = new ProfileFragment();
                    break;
            }
            if (selectedFragment != null)
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        });

        Manager.log("6", this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LifestyleFragment()).commit();

        Manager.log("7 - OnCreate finished", this);
    }

    public void openDialog()
    {
        UniversitySemesterDialog universitySemesterDialog = new UniversitySemesterDialog();
        universitySemesterDialog.show(getSupportFragmentManager(), "UniversityStudinegang");
        universitySemesterDialog.setCancelable(false);
    }


    protected void setLocale() {

        Locale locale = new Locale(Manager.getInstance().getData("settings").getString("language", "de"));
        Locale.setDefault(locale);
        Manager.log(Locale.getDefault() + "");
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

    }
}