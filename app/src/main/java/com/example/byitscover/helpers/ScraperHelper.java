package com.example.byitscover.helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * This class contains useful methods for all scrapers
 *
 * @author Marc
 * @version 1.0
 */
public class ScraperHelper {
    /**
     * This function returns the Google url of the current book for the specified site
     * @param site The site being used
     * @return The google url
     */
    public static String getGoogleUrl(String site) {
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

        //search goodreads for book
        String searchingUrl =  "https://www.google.com/search?q=" + toAppendUrl + "+" + site;

        return searchingUrl;
    }

    public static Document getSite(String searchingUrl) throws IOException {
        searchingUrl.replaceAll("[\\n]", "");
        Document document = Jsoup.connect(searchingUrl).get();
        System.out.println(searchingUrl);

        //go to first search result link
        Element link = (Element) document.select("div.g").first()
                .childNode(1).childNode(0).childNode(0).childNode(0);
        String bookUrl = link.attr("abs:href");
        Document bookDocument = Jsoup.connect(bookUrl).get();
        return bookDocument;
    }
}
