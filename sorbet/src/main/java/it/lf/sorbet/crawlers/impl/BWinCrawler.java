package it.lf.sorbet.crawlers.impl;

import it.lf.sorbet.crawlers.Crawler;
import it.lf.sorbet.models.Quote;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
public class BwinCrawler implements Crawler  {

    private static Logger LOG = LogManager.getLogger(BwinCrawler.class);

    @Override
    public String getBookmakerId() {
        return "Bwin";
    }

    public List<Quote> crawl() {
        final List<Quote> quotes = new ArrayList<Quote>();

        String url = "https://sports.bwin.com/en/sports#sportId=4";

        try {

            Document doc = Jsoup.connect(url).get();
            Elements matches = doc.select(".marketboard-event-group__item--event");

            matches.forEach(new Consumer<Element>() {
                public void accept(Element element) {
                    Elements match = element.select(".mb-option-button__option-odds");
                    Quote quote = new Quote();
                    quote.setQ1(Double.valueOf(match.get(0).text()));
                    quote.setD(Double.valueOf(match.get(1).text()));
                    quote.setQ2(Double.valueOf(match.get(2).text()));

                    Elements teams = element.select(".mb-option-button__option-name");
                    quote.setAliasTeam1(teams.get(0).text());
                    quote.setAliasTeam2(teams.get(2).text());

                    quotes.add(quote);
                }
            });

        } catch (IOException e) {
            LOG.error("Connection exception", e);
        }

        return quotes;
    }

}
