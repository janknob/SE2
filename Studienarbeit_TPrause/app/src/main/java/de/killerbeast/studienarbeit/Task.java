package de.killerbeast.studienarbeit;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

public class Task implements Comparable<Object>{

    private final String title;
    private final String description;
    private final String deadline;
    private final String added;
    private Course course = null;
    private boolean finished;
    private final Context context;

    public Task(String compressed, Context context){

        this.context = context;

        //title|description|course|deadline|added|finished

        this.title = compressed.substring(0, compressed.indexOf('|'));
        compressed = compressed.substring(compressed.indexOf('|')+1);

        this.description = compressed.substring(0, compressed.indexOf('|'));
        compressed = compressed.substring(compressed.indexOf('|')+1);

        String coursename = compressed.substring(0, compressed.indexOf('|'));
        compressed = compressed.substring(compressed.indexOf('|')+1);

        this.deadline = compressed.substring(0, compressed.indexOf('|'));
        compressed = compressed.substring(compressed.indexOf('|')+1);

        this.added = compressed.substring(0, compressed.indexOf('|'));
        compressed = compressed.substring(compressed.indexOf('|')+1);

        this.finished = Boolean.parseBoolean(compressed);

        int counter = 0;
        SharedPreferences sp_courses = context.getSharedPreferences("courses", Context.MODE_PRIVATE);
        while (!sp_courses.getString(""+counter, "").equals("")) {

            String coursecompressed = sp_courses.getString(""+counter, "");
            String coursenormal = coursecompressed;
            for (int i = 0; i < 3; i++) {

                coursecompressed = coursecompressed.substring(coursecompressed.indexOf('|')+1);

            }

            coursecompressed = coursecompressed.substring(0, coursecompressed.indexOf('|'));

            if (coursecompressed.equals(coursename)) {

                this.course = new Course(coursenormal);
                break;

            }
            counter++;

        }

    }

    public String getTitle() {

        return title;

    }

    public String getDescription() {

        return description;

    }

    public String getDeadline() {

        return deadline;

    }

    public Course getCourse() {

        return course;

    }

    public boolean isFinished() {

        return finished;

    }

    public void setFinished(boolean val) {

        this.finished = val;

    }

    public String getCompressedString(){

        return String.format("%s|%s|%s|%s|%s|%s", this.title, this.description, (course != null) ? this.course.getCourseName() : "NAN", this.deadline, this.added, this.finished);

    }

    @Override
    public int compareTo(@NonNull Object o) {

        SharedPreferences sp_settings = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        Task t = (Task) o;
        String compare = sp_settings.getString("compareTasksTo", "Title");

        if (compare.equals(context.getResources().getString(R.string.title))) {

            if (!t.getTitle().equals(this.getTitle())) {

                int charindex = 0;
                int shortestlength = Math.min(t.getTitle().length(), this.getTitle().length());
                while ((t.getTitle().charAt(charindex) == this.getTitle().charAt(charindex)) && charindex < shortestlength-1) charindex++;

                if (t.getTitle().charAt(charindex) < this.getTitle().charAt(charindex)) return 1;
                else return -1;

            }

        } else if (compare.equals(Manager.getInstance().getContext().getResources().getString(R.string.deadline))) {

            String tDeadline = t.getDeadline();
            String deadline = this.getDeadline();

            if (tDeadline.equals(deadline)) return 0;

            if (tDeadline.equals("NAN")) return 1;
            if (deadline.equals("NAN")) return -1;

            String[] temp = tDeadline.split("\\.");
            int[] td = {Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2])};

            temp = deadline.split("\\.");
            int[] d = {Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2])};

            for (int i = 0; i < 3; i++) {

                if (td[i] != d[i]) return (td[i]< d[i]) ? (1) : (-1);

            }

        }

        return 0;

    }

}

