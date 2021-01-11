package de.niceguys.studisapp.Model;

public class UniversityNews {

    // initialize
    private final String title;
    private final String normalText;
    private final String descriptiontext;
    private final String imageUrl;

    // constructor
    public UniversityNews(String title, String normalText, String descriptiontext, String imageUrl) {

        this.title = title;
        this.normalText = normalText;
        this.descriptiontext = descriptiontext;
        this.imageUrl = imageUrl;

    }

    public UniversityNews(String compressed) {

        this.title = compressed.substring(0, compressed.indexOf('|'));
        compressed = compressed.substring(compressed.indexOf('|')+1);

        this.normalText = compressed.substring(0, compressed.indexOf('|'));
        compressed = compressed.substring(compressed.indexOf('|')+1);

        this.descriptiontext = compressed.substring(0, compressed.indexOf('|'));
        compressed = compressed.substring(compressed.indexOf('|')+1);

        this.imageUrl = compressed;

    }

    // getter and setter
    public String getTitle() {

        return title;

    }

    public String getNormalText() {

        return normalText;

    }

    public String getDescriptiontext() {

        return descriptiontext;

    }

    public String getImageUrl() {

        return imageUrl;

    }

    public String getCompressed(){

        return String.format("%s|%s|%s|%s", getTitle(), getNormalText(), getDescriptiontext(), getImageUrl());

    }

}
