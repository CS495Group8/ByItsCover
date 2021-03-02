package com.example.byitscover.scrapers;

import com.example.byitscover.helpers.ScraperConstants;
import com.example.byitscover.helpers.ScraperHelper;
import com.google.api.services.customsearch.model.Result;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoogleScraper {
    public static Map<String, String> getInfo() throws IOException {
        String searchingUrl = ScraperHelper.getGoogleUrlNoAPI(ScraperConstants.GOOGLE_BOOKS);
        searchingUrl.replaceAll("[\\n]", "");
        Document document = Jsoup.connect(searchingUrl).get();
        System.out.println(searchingUrl);

        //go to first search result link
        Element link = (Element) document.select("div.g").first().childNode(1)
                .childNode(0).childNode(0);
        String bookUrl = link.attr("abs:href");
        Document bookDocument = Jsoup.connect(bookUrl).get();

        Map<String, String> toReturn = new HashMap<String, String>();

        Element rating5stars = (Element) bookDocument.getElementById("histogram-container").childNode(1).childNode(0)
                .childNode(0).childNode(1).childNode(0).childNode(0).childNode(0).childNode(1);
        Element rating4stars = (Element) bookDocument.getElementById("histogram-container").childNode(1).childNode(0)
                .childNode(1).childNode(1).childNode(0).childNode(0).childNode(0).childNode(1);
        Element rating3stars = (Element) bookDocument.getElementById("histogram-container").childNode(1).childNode(0)
                .childNode(2).childNode(1).childNode(0).childNode(0).childNode(0).childNode(1);
        Element rating2stars = (Element) bookDocument.getElementById("histogram-container").childNode(1).childNode(0)
                .childNode(3).childNode(1).childNode(0).childNode(0).childNode(0).childNode(1);
        Element rating1stars = (Element) bookDocument.getElementById("histogram-container").childNode(1).childNode(0)
                .childNode(4).childNode(1).childNode(0).childNode(0).childNode(0).childNode(1);

        Double fiveStars = Double.valueOf(Jsoup.clean(rating5stars.toString(), Whitelist.none()));
        Double fourStars = Double.valueOf(Jsoup.clean(rating4stars.toString(), Whitelist.none()));
        Double threeStars = Double.valueOf(Jsoup.clean(rating3stars.toString(), Whitelist.none()));
        Double twoStars = Double.valueOf(Jsoup.clean(rating2stars.toString(), Whitelist.none()));
        Double oneStars = Double.valueOf(Jsoup.clean(rating1stars.toString(), Whitelist.none()));

        //calculate rating
        Double rating = (((fiveStars * 5) + (fourStars * 4) + (threeStars * 3) + (twoStars * 2) +
                (oneStars * 1)) / (fiveStars + fourStars + threeStars + twoStars + oneStars));
        DecimalFormat df = new DecimalFormat("#.##");
        toReturn.put(ScraperConstants.GOOGLE_RATING_KEY, df.format(rating));

        //get review value
        Element reviewValue = (Element) bookDocument.getElementById("synopsistext").childNode(0);
        toReturn.put(ScraperConstants.GOOGLE_REVIEW_KEY,
                Jsoup.clean(reviewValue.toString(), Whitelist.none()));

        return toReturn;
    }
}
