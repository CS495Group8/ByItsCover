package com.example.byitscover.scrapers;

import com.example.byitscover.helpers.CurrentBook;
import com.example.byitscover.helpers.ScraperConstants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GoodreadsScraper {
    public static Map<String, String> getInfo() throws IOException {
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
        String baseUrl = "https://www.goodreads.com";
        String searchingUrl = baseUrl + "/search?query=" + toAppendUrl;
        Document document = Jsoup.connect(searchingUrl).get();

        //go to first search result link
        Element link = document.select("a.bookTitle").first();
        String bookUrl = link.attr("abs:href");
        Document bookDocument = Jsoup.connect(bookUrl).get();

        Map<String, String> toReturn = new HashMap<String, String>();

        //get rating value
        Element ratingValue = bookDocument.selectFirst("[itemprop=ratingValue]");
        toReturn.put(ScraperConstants.GOODREADS_RATING_KEY ,
                ((TextNode) ratingValue.childNode(0)).getWholeText());

        //get review text
        Element reviewValue = bookDocument.selectFirst("div#description").selectFirst("span");
        toReturn.put(ScraperConstants.GOODREADS_REVIEW_KEY,
                Jsoup.clean(reviewValue.childNode(0).toString(), Whitelist.none()));
        System.out.println(toReturn.get(ScraperConstants.GOODREADS_REVIEW_KEY));

        return toReturn;
    }
}


