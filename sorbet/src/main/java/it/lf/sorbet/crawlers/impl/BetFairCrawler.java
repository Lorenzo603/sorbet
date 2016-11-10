package it.lf.sorbet.crawlers.impl;

import it.lf.sorbet.crawlers.Crawler;
import it.lf.sorbet.models.Quote;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BetFairCrawler implements Crawler {

    private static Logger LOG = LogManager.getLogger(BetFairCrawler.class);

    public List<Quote> crawl() {
        final List<Quote> quotes = new ArrayList<Quote>();

        String url = "https://www.betfair.it/sport/football";

        System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\GeckoDriver\\geckodriver.exe");
        WebDriver driver = new FirefoxDriver();

        try {
            driver.get(url);
            (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[title='Serie A']"))).click();
            Thread.sleep(2000);

            Document doc = Jsoup.parse(driver.getPageSource());
            Elements matches = doc.select(".event-information");

            matches.forEach(new Consumer<Element>() {
                public void accept(Element element) {
                    Elements prices = element.select("li");
                    Quote quote = new Quote();
                    quote.setQ1(Double.valueOf(prices.get(0).select("span").text()));
                    quote.setD(Double.valueOf(prices.get(1).select("span").text()));
                    quote.setQ2(Double.valueOf(prices.get(2).select("span").text()));

                    quote.setAliasTeam1(element.select(".home-team-name").text());
                    quote.setAliasTeam2(element.select(".away-team-name").text());

                    quotes.add(quote);
                }
            });

        } catch (Exception e) {
            LOG.error("Connection exception", e);
        } finally {
            driver.quit();
        }
        return quotes;
    }

}
