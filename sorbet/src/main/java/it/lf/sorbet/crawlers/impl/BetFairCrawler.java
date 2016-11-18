package it.lf.sorbet.crawlers.impl;

import it.lf.sorbet.models.Quote;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BetFairCrawler extends AbstractCrawler {

    private static Logger LOG = LogManager.getLogger(BetFairCrawler.class);

    @Override
    public String getBookmakerId() {
        return "BetFair";
    }

    public List<Quote> crawl() {
        final List<Quote> quotes = new ArrayList<>();

        try {
            String url = getCrawlerConfig().getString("url");
            System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\GeckoDriver\\geckodriver.exe");
            WebDriver driver = getWebDriver();
            driver.get(url);
            (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[title='Sfoglia Tutti']"))).click();
            sleep(200);
            List<String> subcategoriesLinks = new ArrayList<>();
            List<WebElement> categories = driver.findElements(By.cssSelector(".browseall-country-selector-item a"));
            for (WebElement category : categories) {
                category.click();
                sleep(100);
                List<WebElement> subcategories = driver.findElements(By.cssSelector(".browseall-events-selector li a"));
                for (WebElement subcategory : subcategories) {
                    subcategoriesLinks.add(subcategory.getAttribute("href"));
                }
            }

            for (String link : subcategoriesLinks) {
                driver.get(link);
                sleep(100);
                Document doc = Jsoup.parse(driver.getPageSource());
                Elements matches = doc.select(".event-information");
                matches.forEach(element -> {

                    Elements prices = element.select("li");
                    Quote quote = new Quote();
                    String q1 = parseOdd(prices.get(0).select("span").text());
                    try {
                        quote.setQ1(Double.valueOf(q1));
                    } catch (Exception e) {
                        return;
                    }

                    String d = parseOdd(prices.get(1).select("span").text());
                    try {
                        quote.setD(Double.valueOf(d));
                    } catch (Exception e) {
                        return;
                    }

                    String q2 = parseOdd(prices.get(2).select("span").text());
                    try {
                        quote.setQ2(Double.valueOf(q2));
                    } catch (Exception e) {
                        return;
                    }

                    quote.setAliasTeam1(element.select(".home-team-name").text());
                    quote.setAliasTeam2(element.select(".away-team-name").text());

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
