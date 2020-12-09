package de.niceguys.studisapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.niceguys.studisapp.Fragments.LifestyleFragment;
import de.niceguys.studisapp.Fragments.ProfileFragment;
import de.niceguys.studisapp.Fragments.UniversityFragment;
import de.niceguys.studisapp.Model.User;

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
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LifestyleFragment()).commit();
    }

    //switch Case Listener which switch to the framgment which is selected by the user
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_lifestyle:
                    selectedFragment = new LifestyleFragment();
                    break;
                case R.id.nav_university:
                    selectedFragment = UniversityFragment.newInstance();

                    //TO-DO
                    /*
                    if (User.getInstance().getDegree() != null)
                    {
                        selectedFragment = UniversityFragment.newInstance();

                    }
                    else
                    {
                        openDialog();
                    }

                     */

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
        }};
    public void openDialog()
    {
        UniversitySemesterDialog universitySemesterDialog = new UniversitySemesterDialog();
        universitySemesterDialog.show(getSupportFragmentManager(), "UniversityStudinegang");
    }
}