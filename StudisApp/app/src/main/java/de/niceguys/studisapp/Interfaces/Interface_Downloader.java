package de.niceguys.studisapp.Interfaces;

public interface Interface_Downloader {

    default void updateProgress(int progress) {

        System.out.println("Downloadprogress: " + progress);

    }

    default void finishedDownload(String html, String mode){}

}
