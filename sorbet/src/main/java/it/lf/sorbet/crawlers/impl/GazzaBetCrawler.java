package it.lf.sorbet.crawlers.impl;

import it.lf.sorbet.crawlers.Crawler;
import it.lf.sorbet.models.Quote;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GazzaBetCrawler implements Crawler {
    private static Logger LOG = LogManager.getLogger(GazzaBetCrawler.class);

    public List<Quote> crawl() {
        final List<Quote> quotes = new ArrayList<Quote>();

        String url = "http://sports.gazzabet.it/it/t/19159/Italia-Serie-A";

        try {

            Document doc = Jsoup.connect(url).get();
            Elements matches = doc.select(".mkt_content");

            matches.forEach(new Consumer<Element>() {
                public void accept(Element element) {
                    Elements prices = element.select("td");
                    Quote quote = new Quote();
                    quote.setQ1(Double.valueOf(prices.get(3).select(".price").get(1).text()));
                    quote.setD(Double.valueOf(prices.get(4).select(".price").get(1).text()));
                    quote.setQ2(Double.valueOf(prices.get(5).select(".price").get(1).text()));

                    Elements teams = element.select(".mb-option-button__option-name");
                    quote.setAliasTeam1(prices.get(3).select(".seln-name").text());
                    quote.setAliasTeam2(prices.get(5).select(".seln-name").text());

                    quotes.add(quote);
                }
            });

        } catch (IOException e) {
            LOG.error("Connection exception", e);
        }

        return quotes;
    }
}
