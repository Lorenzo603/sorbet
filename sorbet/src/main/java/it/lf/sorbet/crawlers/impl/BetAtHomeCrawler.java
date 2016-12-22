package it.lf.sorbet.crawlers.impl;

import it.lf.sorbet.models.Quote;
import it.lf.sorbet.services.impl.BetAtHomeValueNormalizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BetAtHomeCrawler extends AbstractCrawler {

    private static Logger LOG = LogManager.getLogger(BetAtHomeCrawler.class);

    @Autowired
    private BetAtHomeValueNormalizer betAtHomeValueNormalizer;

    @Override
    public String getBookmakerId() {
        return "BetAtHome";
    }

    @Override
    public List crawl(String sport) {
        final List<Quote> quotes = new ArrayList<>();

        try {
            String url;
            if ("soccer".equals(sport)) {
                url = "https://it.bet-at-home.com/it/sport";
            } else if ("tennis".equals(sport)) {
                url = "https://it.bet-at-home.com/it/sport";
            } else {
                throw new IllegalStateException("Target sport not set");
            }

            WebDriver driver = getWebDriver();

            driver.get(url);

            (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li[data-sport='2']")));

            Actions actions = new Actions(driver);
            actions.moveToElement(driver.findElement(By.cssSelector("li[data-sport='2']"))).moveToElement(driver.findElement(By.cssSelector("li[data-sport='2'] .i-checkBlue15x14"))).click().build().perform();

            Thread.sleep(1000);

            if ("soccer".equals(sport)) {

            } else if ("tennis".equals(sport)) {

                Document doc = Jsoup.parse(driver.getPageSource());
                Elements matches = doc.select(".quotes tr[itemtype='http://schema.org/SportsEvent']");
                matches.forEach(match -> {
                    Elements odds = match.select(".ods-odd-link");

                    Quote quote = new Quote();
                    quote.addValue(Double.valueOf(odds.get(0).text()));
                    quote.addValue(Double.valueOf(odds.get(1).text()));

                    String[] names = match.select("span[itemprop=name]").text().split(" - ");
                    String alias1 = names[0];
                    String alias2 = names[1];
                    quote.setAlias1(alias1);
                    quote.setAlias2(alias2);
                    quote.setNormalizedAlias1(betAtHomeValueNormalizer.normalizeAlias(alias1));
                    quote.setNormalizedAlias2(betAtHomeValueNormalizer.normalizeAlias(alias2));

                    quotes.add(quote);

                });
            }

        } catch (Exception e) {
            LOG.error("Connection exception", e);
            return Collections.EMPTY_LIST;
        } finally {
            getWebDriver().quit();
        }

        return quotes;
    }
}
