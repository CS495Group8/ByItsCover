package com.example.byitscover.helpers;

import com.example.byitscover.BuildConfig;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

import org.jsoup.nodes.Document;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;

/**
 * This class contains useful methods for all scrapers
 *
 * @author Marc
 * @version 1.0
 */
public class ScraperHelper {

    private static final int HTTP_REQUEST_TIMEOUT = 3 * 600000;
    private static final String SEARCH_ENGINE_ID = "055daec8cea1afc9a";

    public static List<Result> googleAPISearch(String site){
        String toSearch = getGoogleQuery(site);

        Customsearch customsearch= null;

        try {
            customsearch = new Customsearch(new NetHttpTransport(),new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest httpRequest) {
                    try {
                        // set connect and read timeouts
                        httpRequest.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
                        httpRequest.setReadTimeout(HTTP_REQUEST_TIMEOUT);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Result> resultList=null;
        try {
            Customsearch.Cse.List list=customsearch.cse().list(toSearch);
            list.setKey(BuildConfig.API_KEY);
            list.setCx(SEARCH_ENGINE_ID);
            Search results=list.execute();
            resultList=results.getItems();
        }
        catch (  Exception e) {
            e.printStackTrace();
        }

        return resultList;

    }

    public static String getTopResultUrl(List<Result> results) {
        return results.get(0).getFormattedUrl();
    }

    private static String getGoogleQuery(String site) {
        CurrentBook instance = CurrentBook.getInstance();
        String[] titleWords = instance.getTitle().split(" ");
        String[] authorWords = instance.getAuthor().split(" ");

        String toSearch = new String();
        for (String word : titleWords) {
            toSearch = toSearch + word + " ";
        }
        for (String word : authorWords) {
            if (word == authorWords[authorWords.length - 1]) {
                toSearch = toSearch + word;
            } else {
                toSearch = toSearch + word + " ";
            }
        }

        return toSearch + " " + site;
    }

    public static String getGoogleUrlNoAPI(String site) {
        CurrentBook instance = CurrentBook.getInstance();
        String[] titleWords = instance.getTitle().split(" ");
        String[] authorWords = instance.getAuthor().split(" ");
        String[] siteWords = site.split(" ");

        String toAppendUrl = new String();
        for (String word : titleWords) {
            toAppendUrl = toAppendUrl + word + "+";
        }
        for (String word : authorWords) {
                toAppendUrl = toAppendUrl + word + "+";
        }
        if (siteWords.length > 1) {
            for (String word : siteWords) {
                if (word == siteWords[siteWords.length - 1]) {
                    toAppendUrl = toAppendUrl + word;
                } else {
                    toAppendUrl = toAppendUrl + word + "+";
                }
            }
        }

        //search goodreads for book
        String searchingUrl =  "https://www.google.com/search?q=" + toAppendUrl;

        return searchingUrl;
    }

}
