package de.killerbeast.studienarbeit.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Locale;
import java.util.Objects;
import de.killerbeast.studienarbeit.Course;
import de.killerbeast.studienarbeit.Manager;
import de.killerbeast.studienarbeit.R;

public class Fragment_showCourse_room extends Fragment {

    private Course course;
    private String buildingName, buildingCoordinates, level;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_showcourse_room, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    public static Fragment_showCourse_room newInstance(Course course) {

        Fragment_showCourse_room fragment = new Fragment_showCourse_room();
        Bundle args = new Bundle();
        args.putString("course", course.saveCourse());
        fragment.setArguments(args);
        return fragment;

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);

        if (requireArguments().containsKey("course"))
            course = new Course(Objects.requireNonNull(requireArguments().getString("course")));

        WebView wv = view.findViewById(R.id.wv_locateroom_coordinates);
        TextView tv_coordinates = view.findViewById(R.id.tv_locateroom_coordinates);
        TextView tv_description = view.findViewById(R.id.tv_locateroom_description);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        double height1 = displayMetrics.heightPixels * 0.7;
        int height = (int) height1;
        int width = displayMetrics.widthPixels;

        ViewGroup.LayoutParams params = wv.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        params.height = height;
        params.width = width;
        wv.setLayoutParams(params);
        String newUA = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        wv.getSettings().setUserAgentString(newUA);

        TextView tv_top = view.findViewById(R.id.tv_locateroom_top);
        tv_top.setText(String.format(Manager.getInstance().getContext().getResources().getString(R.string.whereistheroom), course.getRoomNumber()));

        if (course.getRoomNumber().contains("virt_")) { //no physical room -> no coordinates;

            wv.setVisibility(View.GONE);
            tv_coordinates.setVisibility(View.GONE);
            tv_description.setText(getResources().getString(R.string.virtualRoom));


        } else {

            Log.wtf("Height:", height + "");
            wv.setWebViewClient(new WebViewClient());
            wv.getSettings().setMinimumFontSize(12);
            wv.getSettings().setJavaScriptEnabled(true);
            wv.getSettings().setLoadWithOverviewMode(true);
            wv.getSettings().setUseWideViewPort(true);
            wv.getSettings().setGeolocationEnabled(true);
            wv.setSoundEffectsEnabled(true);
            wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            wv.getSettings().setUseWideViewPort(true);


            String room_raw = course.getRoomNumber(); //FA209;
            if (room_raw.contains(" ")) room_raw = room_raw.replace(" ", "");
            if (room_raw.contains("F")) room_raw = room_raw.substring(1);
            char building = room_raw.charAt(0);
            int room = Integer.parseInt(room_raw.substring(1));
            switch (building) {

                case 'A':

                    buildingName = getResources().getString(R.string.buildingA);
                    buildingCoordinates = getResources().getString(R.string.buildingACoordinates);

                    break;

                case 'B':

                    buildingName = getResources().getString(R.string.buildingB);
                    buildingCoordinates = getResources().getString(R.string.buildingBCoordinates);

                    break;
                case 'C':

                    buildingName = getResources().getString(R.string.buildingC);
                    buildingCoordinates = getResources().getString(R.string.buildingCCoordinates);

                    break;

                case 'D':

                    buildingName = getResources().getString(R.string.buildingD);
                    buildingCoordinates = getResources().getString(R.string.buildingDCoordinates);

                    break;

                case 'G':

                    buildingName = getResources().getString(R.string.buildingG);
                    buildingCoordinates = getResources().getString(R.string.buildingGCoordinates);

                    break;

            }

            switch (room / 100) {

                case 0:

                    level = getResources().getString(R.string.groundFloor);

                    break;

                case 1:

                    level = getResources().getString(R.string.firstFloor);

                    break;

                case 2:

                    level = getResources().getString(R.string.secondFloor);

                    break;

            }

            String[] coords = buildingCoordinates.split(",");

            String url = String.format(getResources().getString(R.string.url_maps_template), coords[0], coords[1]);

            wv.loadUrl(url);

            String temp1 = buildingCoordinates.substring(0, buildingCoordinates.indexOf(".") + 2);
            String temp2 = buildingCoordinates.substring(buildingCoordinates.indexOf(",") + 1, buildingCoordinates.indexOf(".", buildingCoordinates.indexOf(".") + 1) + 2);

            String coordss = temp1 + "," + temp2;


            tv_coordinates.setText(String.format("Gebäudekoordinaten: %s", coordss));

            tv_description.setText(String.format(Locale.GERMAN, "Raum %s befindet sich in %s im %s", course.getRoomNumber(), buildingName, level));


        }

    }


}
