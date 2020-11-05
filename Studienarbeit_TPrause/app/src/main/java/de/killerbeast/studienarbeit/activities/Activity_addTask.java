package de.killerbeast.studienarbeit.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import de.killerbeast.studienarbeit.Course;
import de.killerbeast.studienarbeit.R;

public class Activity_addTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button btn_options = findViewById(R.id.btn_addnewtask_options);
        btn_options.setOnClickListener((v)-> showOptionMenu());

        Button btn_save = findViewById(R.id.btn_addnewtask_save);
        btn_save.setOnClickListener((v)->save());

    }

    private void showOptionMenu() {

        TextView tv_course = findViewById(R.id.tv_addnewtask_course);
        TextView tv_deadline = findViewById(R.id.tv_addnewtask_deadline);
        Context dialogMenu = new ContextThemeWrapper(this, R.style.dialogMenu);
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogMenu);
        builder.setTitle(getResources().getString(R.string.addField));

        String[] options = {getResources().getString(R.string.course), getResources().getString(R.string.deadline)};
        boolean[] checkedItems = {tv_course.getVisibility() == View.VISIBLE, tv_deadline.getVisibility() == View.VISIBLE};
        builder.setMultiChoiceItems(options, checkedItems, (dialog, which, isChecked) -> {

            if (which == 0) modifyCourseVisibility(isChecked);
            else modifyDeadlineVisibility(isChecked);

        });

        builder.setPositiveButton(getResources().getString(R.string.save), (dialog, which) -> {

        });
        AlertDialog dialog = builder.create();
        Log.wtf("dialog", dialog.getListView().getChildCount()+"");
        dialog.show();


    }

    private void save() {

        EditText et_title = findViewById(R.id.et_addnewtask_titel);
        EditText et_description = findViewById(R.id.et_addnewtask_description);
        DatePicker dp_deadline = findViewById(R.id.dp_addnewtask_deadline);
        Spinner sp_course = findViewById(R.id.sp_addnewtask_course);

        if (et_title.getText().toString().equals("")) {

            AlertDialog.Builder warning = new AlertDialog.Builder(this);
            warning.setTitle(getResources().getString(R.string.warning_no_title));
            warning.setMessage(getResources().getString(R.string.warning_message2));
            warning.setPositiveButton(getResources().getString(R.string.warning_ok), (dialog, which) -> {
            });

            AlertDialog warn = warning.create();
            warn.show();

        } else {

            String title = et_title.getText().toString();
            String description = et_description.getText().toString().equals("") ? ("n/a") : (et_description.getText().toString());
            String course = "NAN";
            if (sp_course.getVisibility() == View.VISIBLE) {

                course = sp_course.getSelectedItem().toString();

            }
            String deadline = "NAN";
            if (dp_deadline.getVisibility() == View.VISIBLE) {

                deadline = String.format("%s.%s.%s", dp_deadline.getDayOfMonth(), dp_deadline.getMonth()+1, dp_deadline.getYear());

            }

            SharedPreferences sp_tasks = getSharedPreferences("tasks", MODE_PRIVATE);

            int counter = 0;
            while (!sp_tasks.getString(""+counter, "").equals("")) counter++;

            SharedPreferences.Editor editor = sp_tasks.edit();

            editor.putString(""+ counter, String.format("%s|%s|%s|%s|%s|false", title, description, course, deadline, getCurrentDate()));

            editor.apply();

            setResult(7);
            this.finish();

        }


    }

    private void modifyCourseVisibility(boolean show) {

        TextView tv_course = findViewById(R.id.tv_addnewtask_course);
        Spinner sp_course = findViewById(R.id.sp_addnewtask_course);

        if (show) {

            tv_course.setVisibility(View.VISIBLE);

            sp_course.setVisibility(View.VISIBLE);
            sp_course.setAdapter(null);
            ArrayList<String> courseNames = new ArrayList<>();

            SharedPreferences sp_courses = getSharedPreferences("courses", MODE_PRIVATE);

            int counter = 0;
            while (!sp_courses.getString(""+counter, "").equals("")){

                String course = sp_courses.getString(""+counter,"");
                Course c = new Course(course);
                courseNames.add(c.getCourseName());
                counter++;

            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courseNames);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_course.setAdapter(arrayAdapter);

        } else {

            tv_course.setVisibility(View.GONE);
            sp_course.setVisibility(View.GONE);

        }

    }

    private void modifyDeadlineVisibility(boolean show) {

        DatePicker dp_deadline = findViewById(R.id.dp_addnewtask_deadline);
        TextView tv_deadline = findViewById(R.id.tv_addnewtask_deadline);

        if (show) {

            dp_deadline.setVisibility(View.VISIBLE);
            tv_deadline.setVisibility(View.VISIBLE);

        } else {

            dp_deadline.setVisibility(View.GONE);
            tv_deadline.setVisibility(View.GONE);

        }

    }

    private String getCurrentDate(){

        Date currentTime = Calendar.getInstance().getTime();

        return currentTime.toString();

    }

    @Override
    public void onBackPressed(){

        Context cw = new ContextThemeWrapper(this, R.style.dialogMenu);
        AlertDialog.Builder warning = new AlertDialog.Builder(cw);
        warning.setTitle(getResources().getString(R.string.warning_cancle_add_task));
        warning.setMessage(getResources().getString(R.string.warning_message3));
        warning.setPositiveButton(getResources().getString(R.string.warning_cancle), (dialog, which) -> {
        });

        warning.setNegativeButton(getResources().getString(R.string.warning_backToShedule), (dialog, which) -> {

            setResult(7);
            this.finish();

        });

        AlertDialog warn = warning.create();
        warn.show();

    }

}
