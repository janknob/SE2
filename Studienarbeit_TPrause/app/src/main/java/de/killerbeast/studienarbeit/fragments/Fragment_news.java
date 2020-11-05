package de.killerbeast.studienarbeit.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import java.util.Locale;
import java.util.Map;

import de.killerbeast.studienarbeit.activities.Activity_showNews;
import de.killerbeast.studienarbeit.Manager;
import de.killerbeast.studienarbeit.News;
import de.killerbeast.studienarbeit.Parser;
import de.killerbeast.studienarbeit.R;
import de.killerbeast.studienarbeit.interfaces.Interface_Fragmenthandler;
import de.killerbeast.studienarbeit.interfaces.Interface_Parser;

public class Fragment_news extends Fragment implements Interface_Parser {

    private View view;
    private Interface_Fragmenthandler parent;
    private AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    public static Fragment_news newInstance(Interface_Fragmenthandler parent) {

        Fragment_news fragment = new Fragment_news();
        fragment.setParent(parent);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    private void setParent(Interface_Fragmenthandler parent) {

        this.parent = parent;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view,savedInstance);

        this.view = view;

        SharedPreferences sp_news = Manager.getInstance().getSharedPreferences("news");
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

        Parser parser = new Parser(this);
        parser.parse("news");

    }

    public void parsed(Map<String, String> values, String mode) {

        if (mode.equals("news")) {

            SharedPreferences.Editor editor = Manager.getInstance().getSharedPreferences("news").edit();

            for (String s : values.keySet()) editor.putString(s, values.get(s));

            editor.apply();
            alertDialog.dismiss();

            parent.updateFragment(Manager.STRING_FRAGMENT_NEWS);

        }

    }

    private void showNews(){

        LinearLayout ll_news = view.findViewById(R.id.ll_news);

        SharedPreferences sp_news = Manager.getInstance().getSharedPreferences("news");

        Map<String, ?> news = sp_news.getAll();

        boolean notfirst = false;

        for (Object s : news.values()) {

            News n = new News((String)s);

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
            container.setOnClickListener((v)-> {

                Intent inten = new Intent();
                inten.setClass(Manager.getInstance().getContext(), Activity_showNews.class);
                inten.putExtra("newsCompressed", n.getCompressed());
                startActivity(inten);


            });

            container.setElevation(20);

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

            Manager.getInstance().getSharedPreferences("news").edit().clear().apply();
            parent.updateFragment(Manager.STRING_FRAGMENT_NEWS);
            srl.setRefreshing(false);

        });

    }

    public void updateProgress(int progress){

        ((TextView)alertDialog.findViewById(R.id.tv_dialogdownload_percent)).setText(String.format(Locale.GERMAN, "%d %%", progress));

    }

}
