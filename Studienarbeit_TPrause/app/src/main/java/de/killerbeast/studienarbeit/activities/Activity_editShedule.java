package de.killerbeast.studienarbeit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import de.killerbeast.studienarbeit.Course;
import de.killerbeast.studienarbeit.Manager;
import de.killerbeast.studienarbeit.R;

public class Activity_editShedule extends AppCompatActivity {

    private boolean sheduleScrolling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editshedule);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar tb = findViewById(R.id.tb_editShedule);
        setSupportActionBar(tb);

        showCourses();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void showCourses(){

        int deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        HorizontalScrollView sv = findViewById(R.id.sv_editShedule);
        sv.setSmoothScrollingEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sv.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

                int width = sv.getChildAt(0).getWidth();

                int days = width / deviceWidth;

                if (!sheduleScrolling) {

                    //Log.wtf("Scrolled to ", scrollX + ", " + scrollY);
                    // Log.wtf("DeviceWidth", deviceWidth * 0.5 + "");



                    for (int i = 0; i < days; i++) {

                        int under = (int)((deviceWidth*(i-1)) + (deviceWidth*0.5));
                        int above = (int)((deviceWidth*i) + (deviceWidth*0.5));

                        if (above > scrollX && under < scrollX) {

                            if (getSharedPreferences("settings", MODE_PRIVATE).getBoolean("animations", true)) {

                                ObjectAnimator animator = ObjectAnimator.ofInt(sv, "scrollX", deviceWidth * i);

                                animator.setDuration(500);
                                animator.start();
                                animator.addUpdateListener(animation -> {

                                    int val = (int) animation.getAnimatedValue();
                                    sheduleScrolling = true;
                                    runOnUiThread(() -> sv.setScrollX(val));
                                    sheduleScrolling = false;

                                });

                            } else {

                                sheduleScrolling = true;
                                int finalI = i;
                                runOnUiThread(() -> sv.setScrollX(deviceWidth * finalI));
                                sheduleScrolling = false;

                            }

                            //Log.wtf("scroll to", i + "");
                            break;
                        }

                    }

                }

            });
        }

        sv.setOnTouchListener((v, event) -> {

            String e = event.toString();

            if (e.contains("ACTION_UP")) {

                sheduleScrolling = false;
                sv.scrollBy(+1,0);

            } else sheduleScrolling = true;

            return false;

        });

        LinearLayout ll_editShedule = findViewById(R.id.ll_editShedule);
        ll_editShedule.removeAllViews();

        ArrayList<Course> courses = createCourses();

        Map<String, LinearLayout> days = new LinkedHashMap<>();

        for (Course c : courses) {

            if (!days.containsKey(c.getDay())) {

                LinearLayout ll = new LinearLayout(Manager.getInstance().getContext());

                TextView tv = new TextView(Manager.getInstance().getContext());
                tv.setText(c.getDay());
                tv.setTextColor(ContextCompat.getColor(Manager.getInstance().getContext(), R.color.normalText));
                tv.setTextSize(26);
                tv.setPadding(0,0,0,20);
                ll.setGravity(Gravity.CENTER_HORIZONTAL);
                ll.addView(tv);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setLayoutParams(new LinearLayout.LayoutParams(deviceWidth, ViewGroup.LayoutParams.MATCH_PARENT));
                days.put(c.getDay(), ll);

            }

        }

        for (Course c : courses) {

            CardView cv = new CardView(Manager.getInstance().getContext());
            CardView.inflate(Manager.getInstance().getContext(), R.layout.cardview_shedule, cv);
            cv.setLayoutParams(new CardView.LayoutParams( (int)(deviceWidth - (deviceWidth*0.1)), CardView.LayoutParams.WRAP_CONTENT));
            ((TextView)cv.findViewById(R.id.tv_cardview_shedule_name)).setText(c.getCourseName());
            ((TextView)cv.findViewById(R.id.tv_cardview_shedule_time)).setText(c.getTimeFormatted());
            ((TextView)cv.findViewById(R.id.tv_cardview_shedule_prof)).setText(String.format("%s %s", Manager.getInstance().getContext().getResources().getString(R.string.with),c.getProfessor()));
            ((TextView)cv.findViewById(R.id.tv_cardview_shedule_room_kind)).setText(String.format("%s | %s", c.getRoomNumber(), c.getKind()));

            if (!c.isShown()) {

                ((TextView)cv.findViewById(R.id.tv_cardview_shedule_name)).setPaintFlags(((TextView)cv.findViewById(R.id.tv_cardview_shedule_name)).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ((TextView)cv.findViewById(R.id.tv_cardview_shedule_time)).setPaintFlags(((TextView)cv.findViewById(R.id.tv_cardview_shedule_time)).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ((TextView)cv.findViewById(R.id.tv_cardview_shedule_prof)).setPaintFlags(((TextView)cv.findViewById(R.id.tv_cardview_shedule_prof)).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ((TextView)cv.findViewById(R.id.tv_cardview_shedule_room_kind)).setPaintFlags(((TextView)cv.findViewById(R.id.tv_cardview_shedule_room_kind)).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            }

            cv.setRight(40);
            cv.setCardElevation(20);
            cv.setRadius(20);

            cv.setOnClickListener(v -> {

                String key = "0";

                Map<String, ?> map = new LinkedHashMap<>(getSharedPreferences("courses", MODE_PRIVATE).getAll());

                for (String s : map.keySet()) if (Objects.requireNonNull(map.get(s)).toString().equals(c.saveCourse())) key = s;

                c.setShown(!c.isShown());

                SharedPreferences.Editor editor = getSharedPreferences("courses", MODE_PRIVATE).edit();

                editor.putString(key, c.saveCourse());

                editor.apply();
                showCourses();

            });

            Space sp = new Space(Manager.getInstance().getContext());
            sp.setMinimumHeight(60);
            Objects.requireNonNull(days.get(c.getDay())).addView(cv);
            Objects.requireNonNull(days.get(c.getDay())).addView(sp);

        }

        for (LinearLayout ll : days.values()) {

            if (ll.getChildCount() > 1)
                ll_editShedule.addView(ll);

        }


    }

    private ArrayList<Course> createCourses() {

        ArrayList<Course> courses = new ArrayList<>();

        int counter = 0;

        while (!getSharedPreferences("courses", MODE_PRIVATE).getString("" + counter, "").equals("")) courses.add(new Course(getSharedPreferences("courses", MODE_PRIVATE).getString(""+ counter++, "")));

        return courses;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu2, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Context cw = new ContextThemeWrapper(this, R.style.dialogMenu);
        AlertDialog.Builder help = new AlertDialog.Builder(cw);
        help.setTitle(getResources().getString(R.string.editShedule));
        help.setMessage(getResources().getString(R.string.helpHowTo));
        help.setPositiveButton(getResources().getString(R.string.warning_ok), null);
        AlertDialog dialog = help.create();
        dialog.show();
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        setResult(Manager.REQUEST_UPDATE_SHEDULE_FRAGMENT);
        this.finish();

    }
}