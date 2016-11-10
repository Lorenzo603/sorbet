package it.lf.sorbet.crawlers.impl;


import it.lf.sorbet.crawlers.Crawler;
import it.lf.sorbet.models.Quote;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SnaiCrawler extends AbstractCrawler {

    private static Logger LOG = LogManager.getLogger(BwinCrawler.class);

    @Override
    public String getBookmakerId() {
        return "Snai";
    }

    public List<Quote> crawl() {
        final List<Quote> quotes = new ArrayList<>();

        WebDriver driver = null;

        try {
            String url = getCrawlerConfig().getString("url");
            System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\GeckoDriver\\geckodriver.exe");
            driver = new FirefoxDriver();

            driver.get(url);
            Thread.sleep(2000);
            (new WebDriverWait(driver, 20))
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".ccc-close"))).click();
            Thread.sleep(2000);
            (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[aria-controls='CALCIO_0']"))).click();
            Thread.sleep(1000);
            (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[aria-controls='CALCIO_ITALIA_2']"))).click();
            Thread.sleep(1000);
            (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#CALCIO_ITALIA_2 .list-group a"))).click();
            Thread.sleep(5000);

            Document doc = Jsoup.parse(driver.getPageSource());
            Elements matches = doc.select("tr.ng-scope");

            matches.forEach(element -> {
                Elements match = element.select("td");
                Quote quote = new Quote();
                quote.setQ1(Double.valueOf(match.get(1).text().replace(',', '.')));
                quote.setD(Double.valueOf(match.get(2).text().replace(',', '.')));
                quote.setQ2(Double.valueOf(match.get(3).text().replace(',', '.')));

                quote.setAliasTeam1(match.get(0).select("a").text().split("-")[0].trim());
                quote.setAliasTeam2(match.get(0).select("a").text().split("-")[1].trim());

                quotes.add(quote);
            });

        } catch (Exception e) {
            LOG.error("Connection exception", e);
            return Collections.EMPTY_LIST;
        } finally {
            driver.quit();
        }
        return quotes;
    }
}
