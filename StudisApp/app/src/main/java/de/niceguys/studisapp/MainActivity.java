package de.niceguys.studisapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.niceguys.studisapp.Fragments.LifestyleFragment;
import de.niceguys.studisapp.Fragments.ProfileFragment;
import de.niceguys.studisapp.Fragments.UniversityFragment;

public class MainActivity extends AppCompatActivity {

    Fragment selectedFragment = null;
    BottomNavigationView bottomNavigationView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the view with XML file acitvity_main which contains the bottom navigation bar
        setContentView(R.layout.activity_main);


        Manager.getInstance().setContext(this);

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
                        recreate();
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

}