package it.lf.sorbet.crawlers.impl;

import it.lf.sorbet.models.Quote;
import it.lf.sorbet.services.ValueNormalizer;
import it.lf.sorbet.services.impl.BwinValueNormalizer;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BWinCrawler extends AbstractCrawler {

    private static Logger LOG = LogManager.getLogger(BWinCrawler.class);

    @Autowired
    private BwinValueNormalizer bwinValueNormalizer;

    @Override
    public String getBookmakerId() {
        return "BWin";
    }

    public List<Quote> crawl(String sport) {

        final List<Quote> quotes = new ArrayList<>();

        try {
            String url;
            if ("soccer".equals(sport)) {
                url = "https://sports.bwin.com/en/sports#sportId=4";
            } else if ("tennis".equals(sport)){
                url = "https://sports.bwin.it/it/sports#sportId=5";
            } else {
                throw new IllegalStateException("Target sport not set");
            }

            WebDriver driver = getWebDriver();

            driver.get(url);
            sleep(400);
            Document doc = Jsoup.parse(driver.getPageSource());
            Elements matches = doc.select(".marketboard-event-group__item--event");

            matches.forEach(element -> {
                Elements match = element.select(".mb-option-button__option-odds");
                Quote quote = new Quote();
                quote.addValue(Double.valueOf(match.get(0).text())); // 1
                quote.addValue(Double.valueOf(match.get(1).text())); // X
                if ("soccer".equals(sport)) {
                    quote.addValue(Double.valueOf(match.get(2).text())); // 2
                }

                Elements teams = element.select(".mb-option-button__option-name");
                String alias1 = teams.get(0).text();
                quote.setAlias1(alias1);
                quote.setNormalizedAlias1(bwinValueNormalizer.normalizeAlias(alias1));
                String alias2 = StringUtils.EMPTY;
                if ("tennis".equals(sport)) {
                    alias2 = teams.get(1).text();
                } else if ("soccer".equals(sport)) {
                    alias2 = teams.get(2).text();
                }
                quote.setAlias2(alias2);
                quote.setNormalizedAlias2(bwinValueNormalizer.normalizeAlias(alias2));

                quotes.add(quote);
            });

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Collections.EMPTY_LIST;
        } finally {
            getWebDriver().quit();
        }

        return quotes;
    }

}
