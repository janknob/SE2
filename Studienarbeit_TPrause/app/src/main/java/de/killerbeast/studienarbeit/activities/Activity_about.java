package de.killerbeast.studienarbeit.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import de.killerbeast.studienarbeit.BuildConfig;
import de.killerbeast.studienarbeit.R;


public class Activity_about extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        switch (getSharedPreferences("settings", MODE_PRIVATE).getString("appTheme", "System")) {

            case "Hell":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "Dunkel":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        TextView tv_version = findViewById(R.id.tv_about_version);
        LinearLayout ll = findViewById(R.id.ll_about);

        tv_version.setText(String.format("Version: %s @%s", BuildConfig.VERSION_NAME, BuildConfig.BUILD_TYPE));
        String changelog_raw = getSharedPreferences("settings", MODE_PRIVATE).getString("changelogs", "");

        String[] changelog = changelog_raw.split("\n");

        for (String s : changelog) {

            TextView tv = new TextView(getApplicationContext());
            tv.setTextColor(ContextCompat.getColor(this, R.color.normalText));
            if (s.contains("Changelogs")) {

                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setTextSize(16);
                tv.setText(s);
                tv.setPadding(0,20,0,2);

            } else {

                tv.setTextSize(14);
                tv.setText(String.format("-%s", s));
                tv.setPadding(20,0,0,0);

            }

            ll.addView(tv);

        }

        setResult(11);

    }

    public void openSemify() {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.semifyDe)));
        startActivity(browserIntent);

    }

}
