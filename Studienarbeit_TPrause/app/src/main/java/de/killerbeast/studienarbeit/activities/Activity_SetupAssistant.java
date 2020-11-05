package de.killerbeast.studienarbeit.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.killerbeast.studienarbeit.Manager;
import de.killerbeast.studienarbeit.Parser;
import de.killerbeast.studienarbeit.R;
import de.killerbeast.studienarbeit.SocketListener;
import de.killerbeast.studienarbeit.interfaces.Interface_Parser;
import de.killerbeast.studienarbeit.interfaces.Interface_SocketListener;

public class Activity_SetupAssistant extends Activity implements Interface_Parser, Interface_SocketListener {

    private Parser parser;
    private AlertDialog alertDialog;
    private SharedPreferences sp_settings;

    private String degree_short = "";
    private String degree = "";
    private String semester_short = "";
    private String semester = "";


    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setupassistant);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button btn_save = findViewById(R.id.btn_setupassistant_save);
        btn_save.setOnClickListener((v -> checkConfig()));

        Manager manager = Manager.getInstance();
        manager.setContext(this);
        sp_settings = manager.getSharedPreferences("settings");
        if (!sp_settings.getString("username", "").equals("")) {

            ((EditText)findViewById(R.id.et_setupassistant_username)).setText(sp_settings.getString("username", ""));
            Socket s;
            try {
                s = new Socket(Manager.STRING_SERVER_IP, Manager.INTEGER_SERVER_PORT);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                bw.write(String.format("removeUsername: %s\n", sp_settings.getString("username", "")));
                bw.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Context cw = new ContextThemeWrapper(this, R.style.dialogMenu);
        AlertDialog.Builder downloading = new AlertDialog.Builder(cw);LayoutInflater inflater = getLayoutInflater();
        downloading.setView(inflater.inflate(R.layout.dialog_downloading, null));
        alertDialog = downloading.create();
        alertDialog.show();

        TextView tv_percent = alertDialog.findViewById(R.id.tv_dialogdownload_percent);
        tv_percent.setText("0 %");



        parser = new Parser(this);
        parser.parse("degrees");

    }

    @Override
    public void parsed(Map<String, String> values, String mode){

        switch (mode) {

            case "degrees": addDegrees(values); break;
            case "semester": addSemester(values); break;

        }

    }

    private void addDegrees(Map<String, String> values) {

        Spinner sp = findViewById(R.id.sp_setupassistant_degree);

        ArrayList<String> temp = new ArrayList<>(values.values());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_items, temp);
        sp.setAdapter(arrayAdapter);


        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                degree = sp.getItemAtPosition(position).toString();
                if (!degree.equals("Studiengang wählen")) {

                    for (String s : values.keySet()) {

                        if (Objects.equals(values.get(s), degree)) {

                            degree_short = s;
                            break;

                        }

                    }
                    getSemester();

                } else degree = "";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        alertDialog.dismiss();
        ((TextView)alertDialog.findViewById(R.id.tv_dialogdownload_percent)).setText("0%");

    }

    private void getSemester(){

        alertDialog.show();
        parser.parse("semester", degree_short);

    }

    private void addSemester(Map<String, String> values) {

        Spinner sp = findViewById(R.id.sp_setupassistant_semester);

        ArrayList<String> temp = new ArrayList<>(values.values());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_items, temp);
        sp.setAdapter(arrayAdapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                semester = sp.getItemAtPosition(position).toString();
                if (!semester.equals("Studiensemester wählen")) {

                    for (String s : values.keySet()) {

                        if (Objects.equals(values.get(s), semester)) {

                            semester_short = s;
                            break;

                        }

                    }

                } else semester = "";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        alertDialog.dismiss();
        ((TextView)alertDialog.findViewById(R.id.tv_dialogdownload_percent)).setText("0%");


    }

    @Override
    public void updateProgress(int progress){

        ((TextView)alertDialog.findViewById(R.id.tv_dialogdownload_percent)).setText(String.format(Locale.GERMAN,"%d %%", progress));

    }

    private void checkConfig(){

        if (!(semester.equals("") || degree.equals(""))) {

            EditText et_username = findViewById(R.id.et_setupassistant_username);

            if (et_username.getText().toString().equals("")) { //no Username entered;
                Context cw = new ContextThemeWrapper(this, R.style.dialogMenu);
                AlertDialog.Builder warning = new AlertDialog.Builder(cw);
                warning.setTitle(getResources().getString(R.string.warning_no_username));
                warning.setMessage(getResources().getString(R.string.setupassistant_warning_message));
                warning.setPositiveButton(getResources().getString(R.string.warning_positiv), null);
                warning.setNegativeButton(getResources().getString(R.string.warning_negativ), (dialog, which) -> save());

                AlertDialog warn = warning.create();
                warn.show();

            } else checkIfNameNotUsed(et_username.getText().toString());

        }

    }

    private void save(){

        SharedPreferences.Editor editor = sp_settings.edit();

        editor.putBoolean("firstRun", false);
        editor.putString("degree", degree);
        editor.putString("degree_short", degree_short);
        editor.putString("semester_short", semester_short);
        editor.putString("semester", semester);
        editor.putString("username", ((EditText)findViewById(R.id.et_setupassistant_username)).getText().toString());

        editor.apply();

        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), Activity_Main.class);
        startActivity(intent);
        this.finish();

    }

    private void checkIfNameNotUsed(String username){

        try {

            Socket s = new Socket(Manager.STRING_SERVER_IP, Manager.INTEGER_SERVER_PORT);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            bw.write(String.format("checkIfAvaiable: %s\n", username));
            bw.flush();
            SocketListener sl = new SocketListener(this, s);
            Thread t = new Thread(sl);
            t.setDaemon(false);
            t.start();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void received(String received) {

        if (received.equals("Username avaiable")) save();

        else {

            Context cw = new ContextThemeWrapper(this, R.style.dialogMenu);
            AlertDialog.Builder builder = new AlertDialog.Builder(cw);
            builder.setTitle(getResources().getString(R.string.username_notAvaiable));
            builder.setMessage(getResources().getString(R.string.username_notAvaiable_message));
            builder.setPositiveButton(getResources().getString(R.string.username_ok), null);
            runOnUiThread(()-> {
                AlertDialog dialog = builder.create();
                dialog.show();

            });

        }

    }

}
