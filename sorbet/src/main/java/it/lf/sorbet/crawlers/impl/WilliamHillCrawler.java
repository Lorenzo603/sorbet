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
public class WilliamHillCrawler implements Crawler{

    private static Logger LOG = LogManager.getLogger(WilliamHillCrawler.class);

    @Override
    public String getBookmakerId() {
        return "WilliamHill";
    }

    public List<Quote> crawl() {
        final List<Quote> quotes = new ArrayList<Quote>();

        String url = "http://sports.williamhill.it/bet_ita/it/betting/t/321/Serie+A.html";

        try {

            Document doc = Jsoup.connect(url).get();
            Elements matches = doc.select("tr.rowOdd");

            matches.forEach(new Consumer<Element>() {
                public void accept(Element element) {
                    Elements match = element.select("td[scope='col']");
                    Quote quote = new Quote();
                    quote.setQ1(Double.valueOf(match.get(4).select(".eventprice").text()));
                    quote.setD(Double.valueOf(match.get(5).select(".eventprice").text()));
                    quote.setQ2(Double.valueOf(match.get(6).select(".eventprice").text()));

                    Elements teams = match.get(3).select("span");
                    quote.setAliasTeam1(teams.text().split("-")[0].trim());
                    quote.setAliasTeam2(teams.text().split("-")[1].trim());

                    quotes.add(quote);
                }
            });

        } catch (IOException e) {
            LOG.error("Connection exception", e);
        }

        return quotes;
    }
}
