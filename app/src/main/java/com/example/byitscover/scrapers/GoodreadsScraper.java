package com.example.byitscover.scrapers;

import com.example.byitscover.helpers.CurrentBook;
import com.example.byitscover.helpers.ScraperConstants;
import com.example.byitscover.helpers.ScraperHelper;
import com.google.api.services.customsearch.model.Result;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * As the name implies, this is the scraper to get information from goodreads.com. Consists of only
 * the one method that gets the info from the website
 *
 * @author Marc
 * @version 1.0
 * @see <a href="goodreads.com">goodreads.com</a>
 */
public class GoodreadsScraper {
    /**
     * This class is the main driver of the class. Starts by getting the author and title info
     * from the singleton and then forms the url that needs to be scraped. Once the title and author
     * are searched for, the scraper then gets the link to the top search result and then connects
     * to that link to get the rating and review information.
     *
     * @return the information from the website
     * @throws IOException if the text is not found to be returned from the site
     */
    public static Map<String, String> getInfo() throws IOException {
        //Get google url, make sure there are no newlines, and connect to it
        List<Result> results = ScraperHelper.googleAPISearch(ScraperConstants.GOODREADS);
        String searchingUrl = ScraperHelper.getTopResultUrl(results);
        searchingUrl.replaceAll("[\\n]", "");
        Document document = Jsoup.connect(searchingUrl).get();
        System.out.println(searchingUrl);

        Map<String, String> toReturn = new HashMap<String, String>();

        //get rating value
        Element ratingValue = document.selectFirst("[itemprop=ratingValue]");
        toReturn.put(ScraperConstants.GOODREADS_RATING_KEY ,
                ((TextNode) ratingValue.childNode(0)).getWholeText());

        //get review text
        Element reviewValue = document.selectFirst("div#description").selectFirst("span");
        toReturn.put(ScraperConstants.GOODREADS_REVIEW_KEY,
                Jsoup.clean(reviewValue.childNode(0).toString(), Whitelist.none()));

        CurrentBook instance = CurrentBook.getInstance();

        //get author of book from Goodreads
        Element authorValue = document.selectFirst("div#bookAuthors");
        instance.setAuthor(authorValue
                .childNode(3).childNode(1).childNode(1).childNode(0).childNode(0).toString());

        //get title of book from Goodreads
        Element titleValue = document.selectFirst("h1#bookTitle");
        instance.setTitle(titleValue.childNode(0).toString());

        //get picture url
        Element coverUrl = (Element) document.selectFirst("div.bookCoverPrimary")
                .childNode(1).childNode(0);
        instance.setBookCoverUrl(coverUrl.absUrl("src"));

        return toReturn;
    }
}


