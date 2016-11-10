package it.lf.sorbet.crawlers.impl;

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
public class BwinCrawler extends AbstractCrawler {

    private static Logger LOG = LogManager.getLogger(BwinCrawler.class);

    @Override
    public String getBookmakerId() {
        return "BWin";
    }

    public List<Quote> crawl() {

        final List<Quote> quotes = new ArrayList<>();

        WebDriver driver = null;
        try {
            String url = getCrawlerConfig().getString("url");
            System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\GeckoDriver\\geckodriver.exe");
            driver = new FirefoxDriver();

            driver.get(url);
            (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#nav-sport-4"))).click();
            Thread.sleep(2000);

            Document doc = Jsoup.parse(driver.getPageSource());
            Elements matches = doc.select(".marketboard-event-group__item--event");

            LOG.debug(doc.toString());

            matches.forEach(element -> {
                Elements match = element.select(".mb-option-button__option-odds");
                Quote quote = new Quote();
                quote.setQ1(Double.valueOf(match.get(0).text()));
                quote.setD(Double.valueOf(match.get(1).text()));
                quote.setQ2(Double.valueOf(match.get(2).text()));

                Elements teams = element.select(".mb-option-button__option-name");
                quote.setAliasTeam1(teams.get(0).text());
                quote.setAliasTeam2(teams.get(2).text());

                quotes.add(quote);
            });

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Collections.EMPTY_LIST;
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }

        return quotes;
    }

}
