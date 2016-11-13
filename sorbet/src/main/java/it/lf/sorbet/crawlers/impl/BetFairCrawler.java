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
            Map<String, List<String>> allCategories = new HashMap<>();
            List<WebElement> categories = driver.findElements(By.cssSelector(".browseall-country-selector-item a"));
            for (WebElement category : categories) {
                allCategories.put(category.findElement(By.tagName("span")).getText(), new ArrayList<>());
            }
            for (String categoryId : allCategories.keySet()) {
                driver.findElement(By.cssSelector(".browseall-country-selector-item a:contains('"+categoryId+"')")).click();
                List<WebElement> subcategories = driver.findElements(By.cssSelector(".browseall-events-selector li a"));
                for (WebElement subcategory : subcategories) {
                    allCategories.get(categoryId).add(subcategory.getText());
                }
            }

            for (WebElement category : categories) {
                category.click();
                List<WebElement> subcategories = driver.findElements(By.cssSelector(".browseall-events-selector li a"));
                for (WebElement subcategory : subcategories) {
                    String subcategoryName = subcategory.getText();
                    category.click();
                    subcategory.click();
                    sleep(100);
                    Document doc = Jsoup.parse(driver.getPageSource());
                    Elements matches = doc.select(".event-information");
                    matches.forEach(element -> {
                        try {

                        } catch (Exception e) {
                            LOG.error("Connection exception", e);
                        }
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

                    driver.findElement(By.cssSelector("a[title='" + subcategoryName + "']")).click();

                }

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
