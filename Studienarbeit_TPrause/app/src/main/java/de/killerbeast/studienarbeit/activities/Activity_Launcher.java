package de.killerbeast.studienarbeit.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;
import com.koushikdutta.ion.Ion;
import de.killerbeast.studienarbeit.Manager;
import de.killerbeast.studienarbeit.R;

public class Activity_Launcher extends AppCompatActivity {

    private SharedPreferences sp_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (getSharedPreferences("settings", MODE_PRIVATE).getString("appTheme", "System")) {

            case "Hell":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "Dunkel":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
        setContentView(R.layout.activity_launcher);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Ion.getDefault(this).getConscryptMiddleware().enable(false);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Manager manager = Manager.getInstance();
        manager.setContext(getApplicationContext());
        sp_settings = manager.getSharedPreferences("settings");

        ImageView iv1 = findViewById(R.id.iv_launcher_fhlogo_text);
        iv1.post(this::startApp);

    }

    private void startApp(){

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();

        if (sp_settings.getBoolean("firstRun", true))

            intent.setClass(getApplicationContext(), Activity_SetupAssistant.class);

        else

            intent.setClass(getApplicationContext(), Activity_Main.class);

        startActivity(intent);
        this.finish();

    }

}
