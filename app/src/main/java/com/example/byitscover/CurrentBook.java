package com.example.byitscover;

//Singleton for global params
public class CurrentBook {
    private String title;
    private String author;
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

    public void setTitle(String titleIn) {
        title = titleIn;
    }

    public void setAuthor(String authorIn) {
        author = authorIn;
    }
}
