package de.niceguys.studisapp.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.koushikdutta.ion.Ion;

import java.util.Map;

import de.niceguys.studisapp.HtmlParser;
import de.niceguys.studisapp.Interfaces.Interface_Parser;
import de.niceguys.studisapp.Manager;
import de.niceguys.studisapp.R;
import de.niceguys.studisapp.UniversityNews;

public class University_NewsFragment extends Fragment implements Interface_Parser {

    private View view;
    private AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_universitynews, container, false);
    }

    public static University_NewsFragment newInstance() {

        University_NewsFragment fragment = new University_NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view,savedInstance);

        this.view = view;

        SharedPreferences sp_news = Manager.getInstance().getData("news");
        if (sp_news.getAll().size() == 0) downloadNews();
        else showNews();

    }

    @SuppressLint("InflateParams")
    private void downloadNews(){

        Context cw = new ContextThemeWrapper(Manager.getInstance().getContext(), R.style.dialogMenu);
        AlertDialog.Builder downloading = new AlertDialog.Builder(cw);
        LayoutInflater inflater = getLayoutInflater();
        downloading.setView(inflater.inflate(R.layout.dialog_downloading, null));
        alertDialog = downloading.create();
        alertDialog.show();

        HtmlParser parser = new HtmlParser(this);
        parser.parse(Manager.Parser.news);

    }


    public void parsed(Map<String, ?> values, String mode) {

        Map<String, String> value = (Map<String, String>) values;

        if (mode.equals("news")) {

            SharedPreferences.Editor editor = Manager.getInstance().getData("news").edit();

            for (String s : value.keySet()) editor.putString(s, value.get(s));

            editor.apply();
            alertDialog.dismiss();

            showNews();

        }

    }

    private void showNews(){

        LinearLayout ll_news = view.findViewById(R.id.ll_news);
        requireActivity().runOnUiThread(ll_news::removeAllViews);

        SharedPreferences sp_news = Manager.getInstance().getData("news");

        Map<String, ?> news = sp_news.getAll();

        boolean notfirst = false;

        for (Object s : news.values()) {

            UniversityNews n = new UniversityNews((String)s);

            LinearLayout container = new LinearLayout(view.getContext());

            LinearLayout splitter = new LinearLayout(view.getContext());
            splitter.setOrientation(LinearLayout.HORIZONTAL);

            ImageView iv = new ImageView(view.getContext());
            Ion.with(iv)
                    //.placeholder(R.drawable.placeholder_image)
                    //.error(R.drawable.error_image)
                    .load(n.getImageUrl());

            iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

            TextView tv = new TextView(view.getContext());
            tv.setText(n.getTitle());
            tv.setTextColor(ContextCompat.getColor(Manager.getInstance().getContext(), R.color.normalText));
            tv.setTextSize(18);
            tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            tv.setGravity(Gravity.CENTER_VERTICAL);
            Space sp = new Space(view.getContext());
            sp.setMinimumWidth(10);
            splitter.addView(iv);
            splitter.addView(sp);
            splitter.addView(tv);

            container.addView(splitter);
            //container.setOnClickListener((v)-> {
//
            //    Intent inten = new Intent();
            //    inten.setClass(Manager.getInstance().getContext(), Activity_showNews.class);
            //    inten.putExtra("newsCompressed", n.getCompressed());
            //    startActivity(inten);
//
//
            //});

            //TODO: handle onclicklistener above

            Space sp2 = new Space(view.getContext());
            sp2.setMinimumHeight(25);
            Space sp3 = new Space(view.getContext());
            sp3.setMinimumHeight(25);
            View v = new View(view.getContext());

            v.setBackgroundColor(ContextCompat.getColor(Manager.getInstance().getContext(), R.color.colorPrimary));

            if (notfirst) {

                ll_news.addView(sp2);
                ll_news.addView(v);
                v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                ll_news.addView(sp3);

            } else notfirst = true;

            ll_news.addView(container);

        }

        SwipeRefreshLayout srl = view.findViewById(R.id.news_swipeRefresh);
        srl.setOnRefreshListener(()-> {

            Manager.getInstance().getData("news").edit().clear().apply();
            downloadNews();
            srl.setRefreshing(false);

        });
    }
}
