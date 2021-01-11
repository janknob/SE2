package de.niceguys.studisapp.Model;

import android.util.Log;

import com.koushikdutta.ion.Ion;

import de.niceguys.studisapp.Interfaces.Interface_Downloader;

public class HtmlDownloader {

    private final Interface_Downloader caller;
    private final String mode;

    HtmlDownloader(Interface_Downloader caller, String mode) {

        this.caller = caller;
        this.mode = mode;

    }

    public void download(String url) {

        Log.w("DownloaderUrl", url);
        Ion.with(Manager.getInstance().getContext()).load(url).noCache()
                .progress((downloaded, total) -> {

                    int progress = (int) (downloaded / total);
                    caller.updateProgress(progress);

                }).asString()
                .setCallback((e, result) -> {

                    if (result != null)
                    caller.finishedDownload(result, mode);

                });

    }

    public void downloadWithParameter(String url, String name) {

        Ion.with(Manager.getInstance().getContext())
                .load(url)
                .progress(((downloaded, total) -> caller.updateProgress((int)(downloaded/total))))
                .setBodyParameter("tx_personenverzeichnis_pi1[__referrer][@extension]", "Personenverzeichnis")
                .setBodyParameter("tx_personenverzeichnis_pi1[__referrer][@vendor]", "Bitzinger")
                .setBodyParameter("tx_personenverzeichnis_pi1[__referrer][@controller]", "Personenverzeichnis")
                .setBodyParameter("tx_personenverzeichnis_pi1[__referrer][@action]", "suche")
                .setBodyParameter("tx_personenverzeichnis_pi1[__referrer][arguments]", "YTozOntzOjY6ImFjdGlvbiI7czo1OiJzdWNoZSI7czoxMDoiY29udHJvbGxlciI7czoxOToiUGVyc29uZW52ZXJ6ZWljaG5pcyI7czoxMzoiZnJlaXRleHRzdWNoZSI7czoyNToiUHJvZi4gRHIuIENsYXVzIEF0emVuYmVjayI7fQ==0b11fb50f3015c61c32b416332d47472e39c99e5")
                .setBodyParameter("tx_personenverzeichnis_pi1[__referrer][@request]", "a:4:{s:10:\"@extension\";s:19:\"Personenverzeichnis\";s:11:\"@controller\";s:19:\"Personenverzeichnis\";s:7:\"@action\";s:5:\"suche\";s:7:\"@vendor\";s:9:\"Bitzinger\";}b1146e44d088f8057cfa1f75a8b511fed305a9f8")
                .setBodyParameter("tx_personenverzeichnis_pi1[__trustedProperties]", "a:0:{}79451a37ebba4eb449c60a11d4aa9cdab659c37e")
                .setBodyParameter("tx_personenverzeichnis_pi1[freitextsuche]", name)
                .asString().setCallback((e, result) -> caller.finishedDownload(result, mode));

    }

}
