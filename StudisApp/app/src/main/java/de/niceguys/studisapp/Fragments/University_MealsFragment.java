package de.niceguys.studisapp.Fragments;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import de.niceguys.studisapp.HtmlParser;
import de.niceguys.studisapp.Interfaces.Interface_Parser;
import de.niceguys.studisapp.Manager;
import de.niceguys.studisapp.R;

public class University_MealsFragment extends Fragment implements Interface_Parser {

    private AlertDialog alertDialog;

    private HorizontalScrollView hs_main;
    private LinearLayout ll_main_container;

    private boolean sheduleScrolling = false;
    private int deviceWidth = 0;
    private int counter = 0;

    public static University_MealsFragment newInstance() {

        University_MealsFragment fragment = new University_MealsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_universitymeals, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ll_main_container = view.findViewById(R.id.ll_university_meals_main);
        ll_main_container.removeAllViews();
        for (int i = 0; i < 7; i++) ll_main_container.addView(new View(requireContext()));
        hs_main = view.findViewById(R.id.sv_university_meals_main);
        deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        hs_main.setSmoothScrollingEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hs_main.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

                int width = hs_main.getChildAt(0).getWidth();

                int days = width / deviceWidth;
                //Log.wtf("scroll ",   "o");

                if (!sheduleScrolling) {

                    //Log.wtf("Scrolled to ", scrollX + ", " + scrollY);
                    // Log.wtf("DeviceWidth", deviceWidth * 0.5 + "");

                    for (int i = 0; i < days; i++) {

                        int under = (int)((deviceWidth*(i-1)) + (deviceWidth*0.5));
                        int above = (int)((deviceWidth*i) + (deviceWidth*0.5));

                        if (above > scrollX && under < scrollX) {

                            if (Manager.getInstance().getData("settings").getBoolean("animations", true)) {

                                ObjectAnimator animator = ObjectAnimator.ofInt(hs_main, "scrollX", deviceWidth * i);

                                animator.setDuration(500);
                                animator.start();
                                animator.addUpdateListener(animation -> {

                                    int val = (int) animation.getAnimatedValue();
                                    sheduleScrolling = true;
                                    try {

                                        requireActivity().runOnUiThread(() -> hs_main.setScrollX(val));

                                    } catch (Exception e) {
                                        //Activity was closed while Animator was working
                                        animator.end();
                                        e.printStackTrace();

                                    }
                                    sheduleScrolling = false;

                                });

                            } else {

                                sheduleScrolling = true;
                                int finalI = i;
                                try {
                                    requireActivity().runOnUiThread(() -> hs_main.setScrollX(deviceWidth * finalI));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                sheduleScrolling = false;

                            }

                            //Log.wtf("scroll to", i + "");
                            break;
                        }

                    }

                }

            });
        } // magnetic scrolling
        hs_main.setOnTouchListener((v, event) -> {

            String e = event.toString();

            if (e.contains("ACTION_UP")) {

                sheduleScrolling = false;
                hs_main.scrollBy(+1,0);

            }
            else sheduleScrolling = true;

            return false;

        }); //helper for magnetic scrolling

        AlertDialog.Builder downloading = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        downloading.setView(inflater.inflate(R.layout.dialog_downloading, null));
        alertDialog = downloading.create();
        try {
            alertDialog.show();
        } catch (Exception e) {
            Log.w("Meals", "Downloadingalert Error: " + e.getCause());
        }

        downloadMeals();

    }

    @SuppressLint("InflateParams")
    private void downloadMeals() {

        Context cw = new ContextThemeWrapper(Manager.getInstance().getContext(), R.style.dialogMenu);
        AlertDialog.Builder downloading = new AlertDialog.Builder(cw);
        LayoutInflater inflater = getLayoutInflater();
        downloading.setView(inflater.inflate(R.layout.dialog_downloading, null));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        Date date = calendar.getTime();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN);
        String firstDayOfWeek = f.format(date);
        HtmlParser parser = new HtmlParser(this);
        parser.parse(Manager.Parser.meals, firstDayOfWeek);

    }

    public void parsed(Map<String, ?> values, String mode) {

        Map<String, Map<String, String>> value = (Map<String, Map<String, String>>) values;

        if (mode.equals("meals")) {

            showMeals(value);

        }

    }

    public void showMeals(Map<String, Map<String, String>> values) {

        String day = "";

        Map<String, String> main = null;
        Map<String, String> extra = values.get("extra");
        Map<String, String> dessert = values.get("desert");
        Map<String, String> salad = values.get("salads");

        for (String s : values.keySet()) {

            if (!s.equals("extra") && !s.equals("desert") && !s.equals("salads")) {

                day = s;
                main = values.get(s);
                break;

            }

        }

        LinearLayout dayContainer = new LinearLayout(requireContext());
        dayContainer.setLayoutParams(new LinearLayout.LayoutParams(deviceWidth, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout.inflate(requireContext(), R.layout.linearlayout_meals, dayContainer);

        TextView dayName = dayContainer.findViewById(R.id.tv_meals_day);
        dayName.setText(day);

        LinearLayout ll_main = dayContainer.findViewById(R.id.ll_meals_main);
        LinearLayout ll_deserts = dayContainer.findViewById(R.id.ll_meals_deserts);
        LinearLayout ll_extras = dayContainer.findViewById(R.id.ll_meals_extras);
        LinearLayout ll_snacks = dayContainer.findViewById(R.id.ll_meals_snacks_salads);


        for (String key : main.keySet()) {

            LinearLayout ll = new LinearLayout(requireContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);

            TextView description = new TextView(requireContext());
            TextView costs = new TextView(requireContext());

            description.setText(key);
            costs.setText(main.get(key));
            costs.setPadding(10,0,0,0);

            costs.setTextColor(ContextCompat.getColor(requireContext(), R.color.normalText));
            description.setTextColor(ContextCompat.getColor(requireContext(), R.color.normalText));

            costs.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 0));
            description.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));

            ll.addView(description);
            ll.addView(costs);

            ll_main.addView(ll);

        }

        for (String key : dessert.keySet()) {

            LinearLayout ll = new LinearLayout(requireContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);

            TextView description = new TextView(requireContext());
            TextView costs = new TextView(requireContext());

            description.setText(key);
            costs.setText(dessert.get(key));
            costs.setPadding(10,0,0,0);

            costs.setTextColor(ContextCompat.getColor(requireContext(), R.color.normalText));
            description.setTextColor(ContextCompat.getColor(requireContext(), R.color.normalText));

            costs.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 0));
            description.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));

            ll.addView(description);
            ll.addView(costs);

            ll_deserts.addView(ll);

        }

        for (String key : extra.keySet()) {

            LinearLayout ll = new LinearLayout(requireContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);

            TextView description = new TextView(requireContext());
            TextView costs = new TextView(requireContext());

            description.setText(key);
            costs.setText(extra.get(key));
            costs.setPadding(10,0,0,0);

            costs.setTextColor(ContextCompat.getColor(requireContext(), R.color.normalText));
            description.setTextColor(ContextCompat.getColor(requireContext(), R.color.normalText));

            costs.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 0));
            description.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));

            ll.addView(description);
            ll.addView(costs);

            ll_extras.addView(ll);

        }

        for (String key : salad.keySet()) {

            LinearLayout ll = new LinearLayout(requireContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);

            TextView description = new TextView(requireContext());
            TextView costs = new TextView(requireContext());

            description.setText(key);
            costs.setText(salad.get(key));
            costs.setPadding(10, 0, 0, 0);

            costs.setTextColor(ContextCompat.getColor(requireContext(), R.color.normalText));
            description.setTextColor(ContextCompat.getColor(requireContext(), R.color.normalText));

            costs.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 0));
            description.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));

            ll.addView(description);
            ll.addView(costs);

            ll_snacks.addView(ll);

        }

        switch (day.substring(0, day.indexOf(","))) {

                case "Montag":
                    ll_main_container.addView(dayContainer,0);
                    break;
                case "Dienstag":
                    ll_main_container.addView(dayContainer,1);
                    break;
                case "Mittwoch":
                    ll_main_container.addView(dayContainer,2);
                    break;
                case "Donnerstag":
                    ll_main_container.addView(dayContainer,3);
                    break;
                case "Freitag":
                    ll_main_container.addView(dayContainer,4);
                    break;
                case "Samstag":
                    ll_main_container.addView(dayContainer,5);
                    break;

            }

        counter++;

        sortLayouts();

    }

    private void sortLayouts() {


        for (int i = 0; i < ll_main_container.getChildCount(); i++) {

            View v = ll_main_container.getChildAt(i);

            if (v instanceof LinearLayout) {

                LinearLayout bigDayContainer = (LinearLayout) v;
                LinearLayout neededContainer = (LinearLayout) bigDayContainer.getChildAt(0);
                TextView tv = (TextView) neededContainer.getChildAt(0);
                String day = tv.getText().toString();
                ll_main_container.removeView(v);
                switch (day.substring(0,day.indexOf(","))) {

                    case "Montag":
                        ll_main_container.addView(v,0);
                        break;

                    case "Dienstag":
                        ll_main_container.addView(v,1);
                        break;

                    case "Mittwoch":
                        ll_main_container.addView(v,2);
                        break;

                    case "Donnerstag":
                        ll_main_container.addView(v,3);
                        break;

                    case "Freitag":
                        ll_main_container.addView(v,4);
                        break;

                    case "Samstag":
                        ll_main_container.addView(v,5);
                        break;

                }

            }

        }

        if (counter > 5) {

            alertDialog.dismiss();

        }

    }

}
