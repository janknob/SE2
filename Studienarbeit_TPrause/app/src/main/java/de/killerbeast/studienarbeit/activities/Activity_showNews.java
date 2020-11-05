package de.killerbeast.studienarbeit.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;

import com.koushikdutta.ion.Ion;
import java.util.Objects;

import de.killerbeast.studienarbeit.News;
import de.killerbeast.studienarbeit.R;

public class Activity_showNews extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        switch (getSharedPreferences("settings", MODE_PRIVATE).getString("appTheme", "System")) {

            case "Hell":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "Dunkel":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
        setContentView(R.layout.activity_shownews);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String c = Objects.requireNonNull(getIntent().getExtras()).getString("newsCompressed");
        assert c != null;
        News news = new News(c);

        TextView tv_shoNews_title = findViewById(R.id.tv_showNews_title);
        tv_shoNews_title.setText(news.getTitle());

        TextView tv_shoNews_normal = findViewById(R.id.tv_showNews_normal);

        if (news.getNormalText().contains("<a href=")) {

            tv_shoNews_normal.setLinksClickable(true);
            tv_shoNews_normal.setMovementMethod(LinkMovementMethod.getInstance());
            tv_shoNews_normal.setText( Html.fromHtml( news.getNormalText() ) );


        } else  tv_shoNews_normal.setText(news.getNormalText());

        TextView tv_shoNews_description = findViewById(R.id.tv_showNews_description);

        if (news.getDescriptiontext().contains("<a href="))     {

            tv_shoNews_description.setLinksClickable(true);
            tv_shoNews_description.setMovementMethod(LinkMovementMethod.getInstance());
            tv_shoNews_description.setText( Html.fromHtml(news.getDescriptiontext()));

        } else if (news.getDescriptiontext().equals("")) {

            TextView dividor = findViewById(R.id.tv_showNews_dividor);
            dividor.setVisibility(View.GONE);
            TextView description = findViewById(R.id.tv_showNews_descritoption_title);
            description.setVisibility(View.GONE);

        }
        tv_shoNews_description.setText(news.getDescriptiontext());


        ImageView iv_showNews = findViewById(R.id.iv_showNews);

        Ion.with(iv_showNews)
                //.placeholder(R.drawable.placeholder_image)
                //.error(R.drawable.error_image)
                .load(news.getImageUrl());

    }

    @Override
    public void onBackPressed(){

        this.finish();

    }

}
