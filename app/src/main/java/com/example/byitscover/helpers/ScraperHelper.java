package com.example.byitscover.helpers;

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
}
