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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

import de.niceguys.studisapp.Fragments.LifestyleFragment;
import de.niceguys.studisapp.Fragments.ProfileFragment;
import de.niceguys.studisapp.Fragments.UniversityFragment;

public class MainActivity extends AppCompatActivity {

    Fragment selectedFragment = null;
    BottomNavigationView bottomNavigationView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("Users");

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the view with XML file acitvity_main which contains the bottom navigation bar

        Manager.getInstance().setContext(this);

        switch (getSharedPreferences("settings", MODE_PRIVATE).getString("appTheme", "System")) {

            case "Hell":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "Dunkel":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }

        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            savedInstanceState.clear();

            FragmentTransaction ftToKill = getSupportFragmentManager().beginTransaction();


            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

            for (Fragment toClear : fragmentList) {

                ftToKill.remove(toClear);

            }



            ftToKill.commit();

        }



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


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LifestyleFragment()).commit();
    }

    public void openDialog()
    {
        UniversitySemesterDialog universitySemesterDialog = new UniversitySemesterDialog();
        universitySemesterDialog.show(getSupportFragmentManager(), "UniversityStudinegang");
        universitySemesterDialog.setCancelable(false);
    }


    @Override
    protected void onResume() {
        super.onResume();

        Locale locale = new Locale(Manager.getInstance().getData("settings").getString("language", "de"));
        Locale.setDefault(locale);
        Manager.log(Locale.getDefault() + "");
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

    }
}