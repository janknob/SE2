package de.niceguys.studisapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;



public class StartActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
        try {
            VideoView videoView = findViewById(R.id.background_video);
            //setContentView(videoView);
            Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.city_background);
            videoView.setVideoURI(path);
            videoView.requestFocus();
            videoView.start();
        } catch (Exception e) {
            e.printStackTrace();
            jump();
        }
    }
    private void jump() {
        if (isFinishing())
            return;
        startActivity(new Intent(this, RegistrationActivity.class));
        finish();
        }
}