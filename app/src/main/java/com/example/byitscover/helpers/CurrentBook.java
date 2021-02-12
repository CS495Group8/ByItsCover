package com.example.byitscover.helpers;

import java.util.Map;

//Singleton for global params
public class CurrentBook {
    private String title;
    private String author;
    private Map<String, Double> reviewValues;

    private static CurrentBook instance = new CurrentBook();

    private CurrentBook() {
        //private to prevent anyone else from instantiating
    }

    public static CurrentBook getInstance() {
        return instance;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Map<String, Double> getReviewValues() {
        return reviewValues;
    }

    public void setTitle(String titleIn) {
        title = titleIn;
    }

    public void setAuthor(String authorIn) {
        author = authorIn;
    }

    public void setReviewValues(Map<String, Double> reviewValuesIn) {
        reviewValues = reviewValuesIn;
    }
}
