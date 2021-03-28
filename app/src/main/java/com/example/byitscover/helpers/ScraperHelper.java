package com.example.byitscover.helpers;

import com.example.byitscover.BuildConfig;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

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

    /**
     * This function calls the Google Custom Search API to search for the book
     *
     * @param site This is the site that is being searched, such as Goodreads
     * @param query This is the object that contains information about the book to be searched
     * @return a list of search results returned from the API
     */
    public static List<Result> googleAPISearch(String site, Query query){
        String toSearch = getGoogleQuery(site, query);

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

    /**
     * This method just returns the Url of the top search result returned by the API
     * @param results this is the list of results
     * @return String of the Url of the first result
     */
    public static String getTopResultUrl(List<Result> results) {
        return results.get(0).getFormattedUrl();
    }

    /**
     * This method is used to generate what query is used to search Google based on the title and
     * author coming from the Query object and the site being searched
     *
     * @param site This is the site that is being searched, such as Goodreads
     * @param query This is the object that contains information about the book to be searched
     * @return String of the query to be searched: "<title> + <author> + <site>"
     */
    private static String getGoogleQuery(String site, Query query) {
        String[] titleWords = new String[0];
        if (query.getTitle() != null) {
            titleWords = query.getTitle().split(" ");
        }
        String[] authorWords = new String[0];
        if (query.getAuthor() != null) {
            authorWords = query.getAuthor().split(" ");
        }

        String toSearch = new String();
        for (String word : titleWords) {
            toSearch = toSearch + word + " ";
        }
        for (String word : authorWords) {
            toSearch = toSearch + word + " ";
        }

        return toSearch + " " + site;
    }

    /**
     * Scrape Google and access it directly to search rather than using the API. Sometimes
     * produces better results for certain websites
     *
     * @param site the site being searched (ScraperConstants.x)
     * @return the url of the top result
     */
    public static String getGoogleUrlNoAPI(String site, Query query) {
        String[] titleWords = query.getTitle().split(" ");
        String[] authorWords = query.getAuthor().split(" ");
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
        else {
            toAppendUrl = toAppendUrl + site;
        }

        //search goodreads for book
        String searchingUrl =  "https://www.google.com/search?q=" + toAppendUrl;
        return searchingUrl;
    }

}