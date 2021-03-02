package com.example.byitscover.scrapers;

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

public class BarnesAndNobleScraper {
    public static Map<String, String> getInfo() throws IOException {
        List<Result> results = ScraperHelper.googleAPISearch(ScraperConstants.BARNES_AND_NOBLE);
        String searchingUrl = getActualBookResult(results);
        Document document = Jsoup.connect(searchingUrl).get();
        System.out.println(searchingUrl);

        Map<String, String> toReturn = new HashMap<String, String>();

        //get rating by parsing json js var
        String htmlString = document.html();
        String subString = htmlString.substring(htmlString.indexOf(",\"rating\":")+10,
                htmlString.indexOf(",\"rating\":")+13);
        toReturn.put(ScraperConstants.BAN_RATING_KEY, subString);

        //get review
        Element reviewValue = (Element) document.getElementById("overviewSection").childNode(1).childNode(1)
                .childNode(3).childNode(1).childNode(1).childNode(1);

        toReturn.put(ScraperConstants.BAN_REVIEW_KEY ,
                Jsoup.clean(reviewValue.toString(), Whitelist.none()));

        return toReturn;
    }

    private static String getActualBookResult(List<Result> results) {
        String toReturn = "";
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getFormattedUrl().contains("/w/")) {
                toReturn = results.get(i).getFormattedUrl();
                break;
            }
        }
        return toReturn;
    }
}
