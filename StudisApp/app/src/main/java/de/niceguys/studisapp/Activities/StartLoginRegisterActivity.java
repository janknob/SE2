package de.niceguys.studisapp.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import de.niceguys.studisapp.Model.CurrentUser;
import de.niceguys.studisapp.Model.Manager;
import de.niceguys.studisapp.R;

public class StartLoginRegisterActivity extends AppCompatActivity {

    private final static int SPLASHSCREENLENGTH = 5000;
    private TextView logo;
    private LinearLayout loginFormular;
    private LinearLayout registrationFormular;
    private boolean registration = true;
    private ProgressDialog pd;
    private EditText et_reg_username, et_reg_email, et_reg_password, et_log_email, et_log_password;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_login_register);
        Manager.getInstance().setContext(this);

        if (getIntent().hasExtra("registration")) {

            registration = getIntent().getBooleanExtra("registration", true);

        }
        auth = FirebaseAuth.getInstance();
        loginFormular = findViewById(R.id.ll_start_login);
        registrationFormular = findViewById(R.id.ll_start_registration);

        registrationFormular.setTranslationX(-1000);
        loginFormular.setTranslationX(-1000);
        registrationFormular.setEnabled(false);
        loginFormular.setEnabled(false);

        et_reg_username  = findViewById(R.id.et_registration_username);
        et_reg_email = findViewById(R.id.et_registration_email);
        et_reg_password = findViewById(R.id.et_registration_password);
        et_log_email = findViewById(R.id.et_login_email);
        et_log_password = findViewById(R.id.et_login_password);
        loginFormular.setAlpha(0);
        registrationFormular.setAlpha(0);

        logo = findViewById(R.id.tv_start_logo);

        VideoView vv = findViewById(R.id.vv_startLoginRegister);
        vv.setVisibility(View.VISIBLE);
        Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.city_background);
        vv.setVideoURI(path);
        vv.requestFocus();
        vv.start();
        vv.setOnCompletionListener(mediaPlayer -> vv.start());

        vv.postDelayed(this::checkForUser, SPLASHSCREENLENGTH);

        new Thread(this::getUserFromDatabase).start();

        TextView tv_regToLog = findViewById(R.id.tv_registration_toLogin);
        tv_regToLog.setOnClickListener(view -> toggleFormular());

        TextView tv_logToReg = findViewById(R.id.tv_login_toRegistration);
        tv_logToReg.setOnClickListener(view -> toggleFormular());

        Button btn_reg = findViewById(R.id.btn_registration);
        Button btn_log = findViewById(R.id.btn_login);

        btn_reg.setOnClickListener((v)-> register());
        btn_log.setOnClickListener((v)-> login());

    }

    // switch between login and registration formular
    private void toggleFormular() {

        if (registration) {

            new Thread(this::hideRegistration).start();
            new Thread(this::showLogin).start();
            registration = false;

        } else {

            new Thread(this::hideLogin).start();
            new Thread(this::showRegistration).start();
            registration = true;

        }
    }

    /**
     * Checks if user is already logged in
     * if so, it starts directly the MainActivity.class
     */
    private void checkForUser() {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            startActivity(new Intent(this, MainActivity.class));
            this.finish();

        } else {
            moveLogo();
        }

    }

    private void moveLogo() {

        ObjectAnimator animator = ObjectAnimator.ofFloat(logo, "TranslationY",-800);
        animator.setDuration(1000);
        animator.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        showFormulars();
    }

    private void showFormulars() {
        //default: Registration
        if (registration) {

            showRegistration();

        } else {

            showLogin();

        }
    }

    // show registration formular when login is hidden
    private void showRegistration() {

        System.out.println("Show Registration");
        for (int i = 0; i < registrationFormular.getChildCount(); i++) {
            int finalI = i;
            runOnUiThread(()->registrationFormular.getChildAt(finalI).setEnabled(true));
        }

        runOnUiThread(()->{
            registrationFormular.setEnabled(true);
            registrationFormular.setTranslationX(-800);
        });

        ObjectAnimator animatorTranslation = ObjectAnimator.ofFloat(registrationFormular, "TranslationX", 0f);
        animatorTranslation.setDuration(1000);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(registrationFormular, "alpha", 1);
        animatorAlpha.setDuration(1000);

        runOnUiThread( () -> {

            animatorTranslation.start();

            animatorAlpha.start();

        });
    }

    // hide Registration when login is displayed
    private void hideRegistration() {

        System.out.println("Hide Registration");

        for (int i = 0; i < registrationFormular.getChildCount(); i++) {
            int finalI = i;
            runOnUiThread(()->registrationFormular.getChildAt(finalI).setEnabled(false));
        }

        ObjectAnimator animatorTranslation = ObjectAnimator.ofFloat(registrationFormular, "TranslationX", 400f);
        animatorTranslation.setDuration(1000);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(registrationFormular, "alpha", 0);
        animatorAlpha.setDuration(1000);
        runOnUiThread( () -> {

            animatorTranslation.start();

            animatorAlpha.start();

            registrationFormular.setEnabled(false);

        });

    }

    // show login method when registration is hidden
    private void showLogin() {

        System.out.println("Show Login");
        for (int i = 0; i < loginFormular.getChildCount(); i++) {
            int finalI = i;
            runOnUiThread(()->loginFormular.getChildAt(finalI).setEnabled(true));
        }


        runOnUiThread(()->{
            loginFormular.setVisibility(View.VISIBLE);
            loginFormular.setEnabled(true);
            loginFormular.setTranslationX(-800);
        });
        ObjectAnimator animatorTranslation = ObjectAnimator.ofFloat(loginFormular, "TranslationX", 0f);
        animatorTranslation.setDuration(1000);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(loginFormular, "alpha", 1);
        animatorAlpha.setDuration(1000);
        runOnUiThread( () -> {

            animatorTranslation.start();

            animatorAlpha.start();

        });

    }

    // hide login method when registration is displayed
    private void hideLogin() {

        System.out.println("Hide Login");
        for (int i = 0; i < loginFormular.getChildCount(); i++) {
            int finalI = i;
            runOnUiThread(()->loginFormular.getChildAt(finalI).setEnabled(false));
        }

        ObjectAnimator animatorTranslation = ObjectAnimator.ofFloat(loginFormular, "TranslationX", 400f);
        animatorTranslation.setDuration(1000);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(loginFormular, "alpha", 0);
        animatorAlpha.setDuration(1000);
        animatorAlpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                loginFormular.setVisibility(View.GONE);
            }
        });
        runOnUiThread( () -> {

            animatorTranslation.start();

            animatorAlpha.start();

            loginFormular.setEnabled(false);

        });
    }

    private void getUserFromDatabase() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("Users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                try {

                    for (DataSnapshot ds : snapshot.getChildren()) {

                        assert user != null;
                        if (Objects.equals(ds.child("id").getValue(), user.getUid())) {

                            System.out.println(ds.child("username").getValue(String.class));

                            CurrentUser temp = CurrentUser.getInstance();

                            String deg = ds.child("courseOfStudy").getValue(String.class);
                            String degId = ds.child("courseOfStudyId").getValue(String.class);
                            String sem = ds.child("semester").getValue(String.class);
                            String semId = ds.child("semesterId").getValue(String.class);
                            if (sem != null)
                                temp.setSemester(sem);
                            if (semId != null)
                                temp.setSemesterId(semId);
                            if (degId != null)
                                temp.setDegreeId(degId);
                            if (deg != null)
                                temp.setDegree(deg);
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

    // register Method
    private void register() {
        pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(R.string.pleaseWait));
        pd.show();

        String str_username = et_reg_username.getText().toString();
        String str_eMail = et_reg_email.getText().toString();
        String str_password = et_reg_password.getText().toString();

        if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_eMail) || TextUtils.isEmpty(str_password))
        {
            Toast.makeText(this, getResources().getString(R.string.fillFields), Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }
        else if (str_password.length() < 6)
        {
            Toast.makeText(this, getResources().getString(R.string.minPassword), Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }
        else
        {
            registerUser(str_username, str_eMail, str_password);
        }
    }

    // upload the new User to the DB
    private void registerUser(final String username, String eMail, String password) {

        auth.createUserWithEmailAndPassword(eMail, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful())
            {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                assert firebaseUser != null;
                String userid = firebaseUser.getUid();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", userid);
                hashMap.put("username", username.toLowerCase());
                hashMap.put("semester", "");
                hashMap.put("semesterId", "");
                hashMap.put("courseOfStudy", "");
                hashMap.put("courseOfStudyId", "");
                hashMap.put("postalCode", "");
                hashMap.put("university", "");
                hashMap.put("imgUrl", "https://firebasestorage.googleapis.com/v0/b/studisapp-45ebf.appspot.com/o/146p9b.jpg?alt=media&token=4d3dd331-ee28-4400-8e68-922b0f92ec5d");
                // Method which is called when the Registration was successful and automatically switch to the MainActivity
                reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful())
                    {

                        pd.dismiss();
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        this.finish();

                    }
                });
            }
            else {

                pd.dismiss();
                Toast.makeText(this, getResources().getString(R.string.wrongPwOrMail), Toast.LENGTH_SHORT).show();

            }
        });
    }

    // login Method
    private void login() {

        pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(R.string.pleaseWait));
        pd.show();

        String str_email = et_log_email.getText().toString();
        String str_password = et_log_password.getText().toString();

        if (TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)) {

            Toast.makeText(this, getResources().getString(R.string.fillFields), Toast.LENGTH_SHORT).show();
            pd.dismiss();

        } else {

            auth.signInWithEmailAndPassword(str_email, str_password).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()) {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(auth.getCurrentUser()).getUid());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            pd.dismiss();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            CurrentUser temp = CurrentUser.getInstance();

                            String deg = snapshot.child("courseOfStudy").getValue(String.class);
                            String degId = snapshot.child("courseOfStudyId").getValue(String.class);
                            String sem = snapshot.child("semester").getValue(String.class);
                            String semId = snapshot.child("semesterId").getValue(String.class);
                            if (sem != null)
                                temp.setSemester(sem);
                            if (semId != null)
                                temp.setSemesterId(semId);
                            if (degId != null)
                                temp.setDegreeId(degId);
                            if (deg != null)
                                temp.setDegree(deg);

                            startActivity(intent);
                            finish();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            pd.dismiss();
                        }
                    });

                } else {

                    pd.dismiss();
                    Toast.makeText(this, getResources().getString(R.string.failedLogin), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}

