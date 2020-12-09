package de.niceguys.studisapp.Interfaces;

import java.util.Map;

public interface Interface_Parser extends de.niceguys.studisapp.Interfaces.Interface_Downloader {

    default void parsed(Map<String, ?> values, String mode) {}

}
