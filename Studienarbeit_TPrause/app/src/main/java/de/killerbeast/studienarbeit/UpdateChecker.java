package de.killerbeast.studienarbeit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import com.koushikdutta.ion.Ion;
import java.io.File;
import java.util.ArrayList;
import de.killerbeast.studienarbeit.interfaces.Interface_Downloader;
import de.killerbeast.studienarbeit.interfaces.Interface_UpdateChecker;

public class UpdateChecker extends AsyncTask<String, Void, Void> implements Interface_Downloader {


    private final ArrayList<Integer> currentAppVersion = new ArrayList<>();
    private final boolean showNotification;
    private final Interface_UpdateChecker caller;


    public UpdateChecker(Interface_UpdateChecker caller, boolean showNotification){

        this.caller = caller;
        this.showNotification = showNotification;
        String versionName = BuildConfig.VERSION_NAME;
        String[] splitted = versionName.split("\\.");

        for (String s : splitted)
            this.currentAppVersion.add(Integer.parseInt(s));

    }

    @Override
    public void finishedDownload(String download, String mode) {

        if (mode.equals("checkForUpdate")) parseHtml(download);

    }

    private void parseHtml(String download) {

        download = download.substring(download.indexOf("<appVersion>"));

        String downloadedAppVersion_s = download.substring(download.indexOf(">")+1, download.indexOf("</appVersion>"));

        ArrayList<Integer> appVersionOnline = new ArrayList<>();

        String[] temp = downloadedAppVersion_s.split("\\.");
        for (String s : temp) appVersionOnline.add(Integer.parseInt(s));

        StringBuilder changelogs_b = new StringBuilder();

        while (download.contains("<p>")) {

            changelogs_b.append(download.substring(download.indexOf("<p>") + 3, download.indexOf("</p>"))).append("\n");
            download = download.substring(download.indexOf("</p>")+4);

        }

        String changelogs = changelogs_b.toString();
        SharedPreferences sp_settings = Manager.getInstance().getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp_settings.edit();
        editor.putString("changelogs", changelogs);
        editor.apply();

        StringBuilder current = new StringBuilder();

        for (int i : currentAppVersion) current.append(i).append(".");
        int counter = 0;
        try {

            while (appVersionOnline.get(counter).equals(currentAppVersion.get(counter)) && counter < currentAppVersion.size() -1) counter++;
            if (currentAppVersion.get(counter) < appVersionOnline.get(counter)) updateAvaiable(downloadedAppVersion_s, changelogs);
            else showUpToDate();
        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    private void showUpToDate() {

        if (showNotification) {

            caller.showUpToDate();

        }

    }

    private void updateAvaiable(String onlineVersopn, String logs) {

        logs = logs.substring(0, logs.indexOf("Changelogs from"));
        //Update
        AlertDialog.Builder warning = new AlertDialog.Builder(Manager.getInstance().getContext());
        warning.setTitle(String.format("Version %s %s",onlineVersopn, Manager.getInstance().getContext().getResources().getString(R.string.warning_newAppVersion_title)));
        warning.setMessage(Manager.getInstance().getContext().getResources().getString(R.string.warning_newAppVersion_message));
        warning.setPositiveButton(Manager.getInstance().getContext().getResources().getString(R.string.warning_newAppVersion_yes), (dialog, which) -> downloadNewApk());
        warning.setNegativeButton(Manager.getInstance().getContext().getResources().getString(R.string.warning_newAppVersion_no), (dialog, which) -> {});
        TextView l = new TextView(Manager.getInstance().getContext());
        l.setText(logs);
        l.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        ScrollView sv = new ScrollView(Manager.getInstance().getContext());
        sv.addView(l);
        warning.setView(sv);

        AlertDialog warn = warning.create();
        warn.show();

    }

    private void downloadNewApk(){

        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = "StundenplanHof.apk";
        destination += fileName;

        String url = Manager.getInstance().getContext().getString(R.string.url_newApp);
        com.koushikdutta.async.Util.SUPRESS_DEBUG_EXCEPTIONS = true;
        Ion.with(Manager.getInstance().getContext())
                .load(url)
                .noCache()
                .progress((downloaded, total) -> System.out.println("" + downloaded + " / " + total))
                .write(new File(destination))
                .setCallback((e, file1) -> {
                    if (e != null) Log.wtf("updateDownload", e.getLocalizedMessage());

                    Log.wtf("f", file1.exists() + ", " + file1.getAbsolutePath());


                    Uri apkUri = FileProvider.getUriForFile(Manager.getInstance().getContext(), BuildConfig.APPLICATION_ID + ".fileprovider", file1);

                    Log.wtf("APKURI", apkUri.getPath());

                    Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    intent.setData(apkUri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Manager.getInstance().getContext().getApplicationContext().startActivity(intent);

                    Toast.makeText(Manager.getInstance().getContext().getApplicationContext(), "App Installing", Toast.LENGTH_LONG).show();

                });

    }

    @Override
    protected Void doInBackground(String... strings) {

        boolean isInternet = Manager.getInstance().isInternetAvaiable();

        if (isInternet) {

            Downloader d = new Downloader(this, "checkForUpdate");
            Log.wtf("check now for updates", "");
            d.download(strings[0]);

        } else {
            Log.wtf("dont check now for updates", "");
        }

        return null;

    }

}
