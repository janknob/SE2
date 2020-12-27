package de.niceguys.studisapp;

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
import de.niceguys.studisapp.Model.CurrentUser;

public class StartLoginRegisterActivity extends AppCompatActivity {

    private final static int SPLASHSCREENLENGTH = 5000;
    private TextView logo;
    private LinearLayout loginFormular;
    private LinearLayout registrationFormular;
    private boolean registration = true;
    private boolean registrationShown = true;
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

    private void toggleFormular() {

        if (registrationShown) {

            new Thread(this::hideRegistraion).start();
            new Thread(this::showLogin).start();
            registrationShown = false;

        } else {

            new Thread(this::hideLogin).start();
            new Thread(this::showRegistration).start();
            registrationShown = true;

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

    private void showRegistration() {

        System.out.println("Show Registration");

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

    private void hideRegistraion() {

        System.out.println("Hide Registration");

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

    private void showLogin() {

        System.out.println("Show Login");
        loginFormular.setVisibility(View.VISIBLE);
        runOnUiThread(()->{
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

    private void hideLogin() {

        System.out.println("Hide Login");

        ObjectAnimator animatorTranslation = ObjectAnimator.ofFloat(loginFormular, "TranslationX", 400f);
        animatorTranslation.setDuration(1000);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(loginFormular, "alpha", 0);
        animatorAlpha.setDuration(1000);
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

                        if (ds.child("id").getValue().equals(user.getUid())) {

                            System.out.println(ds.child("username").getValue(String.class));

                            CurrentUser temp = CurrentUser.getInstance();
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

    private void register() {

        pd = new ProgressDialog(this);
        pd.setMessage("Bitte warten Sie....");
        pd.show();

        String str_username = et_reg_username.getText().toString();
        String str_eMail = et_reg_email.getText().toString();
        String str_password = et_reg_password.getText().toString();

        if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_eMail) || TextUtils.isEmpty(str_password))
        {
            Toast.makeText(this, "Alle Felder müssen ausgefüllt sein!", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }
        else if (str_password.length() < 6)
        {
            Toast.makeText(this, "Das Passwort muss mindestenst 6 Zeichen lang sein!", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }
        else
        {
            registerUser(str_username, str_eMail, str_password);
        }

    }

    private void registerUser(final String username, String eMail, String password) {

        auth.createUserWithEmailAndPassword(eMail, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful())
            {
                FirebaseUser firebaseUser = auth.getCurrentUser();
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
                Toast.makeText(this, "Sie können sich leider nicht mit dieser Email oder Passwort registrieren", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void login() {

        pd = new ProgressDialog(this);
        pd.setMessage("Bitte warten Sie....");
        pd.show();

        String str_email = et_log_email.getText().toString();
        String str_password = et_log_password.getText().toString();

        if (TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)) {

            Toast.makeText(this, "Alle Felder müssen ausgefüllt sein!", Toast.LENGTH_SHORT).show();
            pd.dismiss();

        } else {

            auth.signInWithEmailAndPassword(str_email, str_password).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()) {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            pd.dismiss();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                    Toast.makeText(this, "Anmeldung fehlgeschlagen", Toast.LENGTH_SHORT).show();

                }
            });

        }

    }



}

