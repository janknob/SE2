package de.killerbeast.studienarbeit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.Objects;

import de.killerbeast.studienarbeit.Course;
import de.killerbeast.studienarbeit.R;
import de.killerbeast.studienarbeit.Task;

public class Activity_showTask extends AppCompatActivity {

    private Task task;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_task);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent i = getIntent();
        if (i.hasExtra("task")) {
            this.task = new Task(Objects.requireNonNull(Objects.requireNonNull(i.getExtras()).getString("task")), this.getApplicationContext());

            TextView tv_task_title = findViewById(R.id.tv_showTask_title);
            tv_task_title.setText(task.getTitle());

            TextView tv_task_description = findViewById(R.id.tv_showTask_description);
            tv_task_description.setText(task.getDescription());

            if (task.getCourse() != null) {
                this.course = task.getCourse();

                TextView tv_showTask_coursename = findViewById(R.id.tv_showTask_coursename);
                tv_showTask_coursename.setVisibility(View.VISIBLE);

                TextView tv_showTask_course = findViewById(R.id.tv_showTask_course);
                tv_showTask_course.setText(course.getCourseName());

                Button btn_showTask_course  = findViewById(R.id.btn_showTask_course);
                btn_showTask_course.setOnClickListener((v)-> startActivity(new Intent().setClass(getApplicationContext(), Activity_showCourse.class).putExtra("course", course.saveCourse())));

                tv_showTask_course.setVisibility(View.VISIBLE);
                btn_showTask_course.setVisibility(View.VISIBLE);


            }

            if (!task.getDeadline().equals("NAN")) {

                TextView tv_showTask_deadlinetitle = findViewById(R.id.tv_showTask_deadlinetitle);
                tv_showTask_deadlinetitle.setVisibility(View.VISIBLE);

                TextView tv_showTask_deadline = findViewById(R.id.tv_showTask_deadline);
                tv_showTask_deadline.setText(task.getDeadline());
                tv_showTask_deadline.setVisibility(View.VISIBLE);

            }

            CheckBox cb_finish = findViewById(R.id.cb_showTask_finished);
            cb_finish.setChecked(task.isFinished());
            cb_finish.setOnCheckedChangeListener((v,checked) -> {

                SharedPreferences sp_tasks = getSharedPreferences("tasks", MODE_PRIVATE);

                int task_id=0;
                String temp = task.getCompressedString();
                String temp1 = sp_tasks.getString(""+task_id, "");
                while (!temp1.equals(temp)){
                    task_id++;
                    temp1 = sp_tasks.getString(""+task_id, "");
                }

                task.setFinished(checked);

                SharedPreferences.Editor editor = sp_tasks.edit();

                editor.putString(""+task_id, task.getCompressedString());

                editor.apply();

            });

        }

    }

    @Override
    public void onBackPressed(){

        setResult(7);
        finish();

    }
}
