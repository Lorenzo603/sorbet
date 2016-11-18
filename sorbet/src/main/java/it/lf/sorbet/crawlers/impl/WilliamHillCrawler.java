package it.lf.sorbet.crawlers.impl;

import it.lf.sorbet.models.Quote;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class WilliamHillCrawler extends AbstractCrawler {

    private static Logger LOG = LogManager.getLogger(WilliamHillCrawler.class);

    @Override
    public String getBookmakerId() {
        return "WilliamHill";
    }

    public List<Quote> crawl() {
        final List<Quote> quotes = new ArrayList<>();

        WebDriver driver = null;


        try {
            String url = getCrawlerConfig().getString("url");
            System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\GeckoDriver\\geckodriver.exe");
            driver = new FirefoxDriver();

            driver.get(url);
            List<String> subcategoriesLinks = new ArrayList<>();
            List<WebElement> subcategories = driver.findElements(By.cssSelector(".listContainer a"));
            for (WebElement subcategory : subcategories) {
                subcategoriesLinks.add(subcategory.getAttribute("href"));
            }

            for (String link : subcategoriesLinks) {
                try {
                    driver.get(link);
                    Document doc = Jsoup.parse(driver.getPageSource());
                    Elements matches = doc.select("tr.rowOdd");

                    matches.forEach(element -> {
                        Elements match = element.select("td[scope='col']");
                        Quote quote = new Quote();
                        quote.setQ1(Double.valueOf(match.get(4).select(".eventprice").text()));
                        quote.setD(Double.valueOf(match.get(5).select(".eventprice").text()));
                        quote.setQ2(Double.valueOf(match.get(6).select(".eventprice").text()));

                        String[] teamText = match.get(2).select("span").text().split("-");
                        quote.setAliasTeam1(teamText[0].trim());
                        quote.setAliasTeam2(teamText[1].trim());

                        quotes.add(quote);
                    });
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                }

            }


        } catch (Exception e) {
            LOG.error("Connection exception", e);
            return Collections.EMPTY_LIST;
        } finally {
            driver.quit();
        }

        return quotes;
    }
}
