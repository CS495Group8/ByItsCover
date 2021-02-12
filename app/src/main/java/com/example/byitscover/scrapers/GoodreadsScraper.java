package com.example.byitscover.scrapers;

import com.example.byitscover.CurrentBook;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class GoodreadsScraper {
    static double avgReviewScore;

    public static double getAverageReviewScore() throws IOException {
        CurrentBook instance = CurrentBook.getInstance();
        String[] titleWords = instance.getTitle().split(" ");
        String[] authorWords = instance.getAuthor().split(" ");

        String toAppendUrl = new String();
        for (String word : titleWords) {
            toAppendUrl = toAppendUrl + word + "+";
        }
        for (String word : authorWords) {
            if (word == authorWords[authorWords.length - 1]) {
                toAppendUrl = toAppendUrl + word;
            } else {
                toAppendUrl = toAppendUrl + word + "+";
            }
        }

        String searchingUrl = "https://www.goodreads.com/search?query=" + toAppendUrl;
        //TODO: Fix this
        Document document = Jsoup.connect(searchingUrl).get();
        String titleWed = document.title();
        System.out.println(titleWed);

        return 0.20;
    }
}


