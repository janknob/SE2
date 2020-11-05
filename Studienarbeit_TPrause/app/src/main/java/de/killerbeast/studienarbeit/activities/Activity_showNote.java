package de.killerbeast.studienarbeit.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import de.killerbeast.studienarbeit.R;

public class Activity_showNote extends AppCompatActivity {

    private String note = "";
    private String coursename = "";

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_shownote);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getApplicationContext();

        if (Objects.requireNonNull(getIntent().getExtras()).containsKey("note")) note = getIntent().getExtras().getString("note");
        if (Objects.requireNonNull(getIntent().getExtras()).containsKey("coursename")) coursename = getIntent().getExtras().getString("coursename");

        if (note != null) {

            String date = note.substring(0, note.indexOf("|"));

            String text = note.substring(note.indexOf("|")+1);

            TextView tv = findViewById(R.id.tv_showNote);
            tv.setText(date);

            EditText et = findViewById(R.id.et_showNote);
            et.setText(text);

            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    String tosave = String.format("%s|%s", date, et.getText().toString());

                    SharedPreferences sp_notes = getApplication().getSharedPreferences("notes", MODE_PRIVATE);
                    Set<String> set = sp_notes.getStringSet(coursename, null);

                    if (set == null) set = new HashSet<>();

                    for (String string : Objects.requireNonNull(set)) {

                        if (string.substring(0,string.indexOf("|")).equals(date)) {

                            set.remove(string);
                            break;

                        }

                    }

                    set.add(tosave);

                    SharedPreferences.Editor editor = sp_notes.edit();
                    editor.remove(coursename);
                    editor.apply();
                    editor.putStringSet(coursename, set);
                    editor.apply();

                    Log.wtf("EditText", "text changed");

                    Log.wtf("saved" , sp_notes.getStringSet(coursename, new HashSet<>()).toString());

                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            });

        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Intent resultIntent = new Intent();
        setResult(6, resultIntent);
        //TODO add resultcode to manager as const
        finish();

    }


}
