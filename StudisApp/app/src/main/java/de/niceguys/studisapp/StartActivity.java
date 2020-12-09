package de.niceguys.studisapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.niceguys.studisapp.Model.User;


public class StartActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Manager.getInstance().setContext(this);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(StartActivity.this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN);
        try {
            VideoView videoView = findViewById(R.id.background_video);
            //setContentView(videoView);
            Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vertical_video);
            videoView.setVideoURI(path);
            videoView.requestFocus();
            videoView.start();

            {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference userRef = database.getReference("Users");
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        try {

                            for (DataSnapshot ds : snapshot.getChildren()) {
                                if (ds.child("id").getValue().equals(user.getUid())) {

                                    System.out.println(ds.child("username").getValue(String.class));

                                    User temp = User.getInstance();
                                    temp.setSemester(ds.child("semester").getValue(String.class));
                                    temp.setSemesterId(ds.child("semesterId").getValue(String.class));
                                    temp.setDegreeId(ds.child("courseOfStudyId").getValue(String.class));
                                    temp.setDegree(ds.child("courseOfStudy").getValue(String.class));
                                    System.out.println("RDY");
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }


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