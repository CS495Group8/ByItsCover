package com.example.byitscover.scrapers;

import com.example.byitscover.helpers.CurrentBook;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.io.IOException;

public class GoodreadsScraper {
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

        String baseUrl = "https://www.goodreads.com";
        String searchingUrl = baseUrl + "/search?query=" + toAppendUrl;
        Document document = Jsoup.connect(searchingUrl).get();
        Element link = document.select("a.bookTitle").first();
        String bookUrl = link.attr("abs:href");
        Document bookDocument = Jsoup.connect(bookUrl).get();
        Element ratingValue = bookDocument.selectFirst("[itemprop=ratingValue]");
        final String ratingValueString = ((TextNode) ratingValue.childNode(0)).getWholeText();

        return Double.parseDouble(ratingValueString);
    }
}


