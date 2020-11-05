package de.killerbeast.studienarbeit.interfaces;

import java.util.Map;

public interface Interface_Parser extends Interface_Downloader {

    default void parsed(Map<String, String> values, String mode) {}

}
