package de.niceguys.studisapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.VideoView;

import de.niceguys.studisapp.RegistrationActivity;

public class StartActivity extends AppCompatActivity {

    Animation splash_anim;
    ImageView logo;
    private static int SPLASH_SCREEN = 5000;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
        try {
            VideoView videoView = new VideoView(this);
            setContentView(videoView);
            Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.city_background);
            videoView.setVideoURI(path);
            videoView.requestFocus();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    jump();
                }
            });
            videoView.start();
        } catch (Exception e) {
            jump();
        }
    }
    private void jump() {
        if (isFinishing())
            return;
        startActivity(new Intent(this, RegistrationActivity.class));
        finish();
        }

    /*private void setDimension() {
        // Adjust the size of the video
        // so it fits on the screen
        float videoProportion = getVideoProportion();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        float screenProportion = (float) screenHeight / (float) screenWidth;
        android.view.ViewGroup.LayoutParams lp = videoView.getLayoutParams();

        if (videoProportion < screenProportion) {
            lp.height= screenHeight;
            lp.width = (int) ((float) screenHeight / videoProportion);
        } else {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth * videoProportion);
        }
        videoView.setLayoutParams(lp);
    }

    // This method gets the proportion of the video that you want to display.
// I already know this ratio since my video is hardcoded, you can get the
// height and width of your video and appropriately generate  the proportion
//    as :height/width
    private float getVideoProportion(){
        return 1.5f;
    }

     */
}