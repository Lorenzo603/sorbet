package it.lf.sorbet.crawlers.impl;


import it.lf.sorbet.models.Quote;
import it.lf.sorbet.services.ValueNormalizer;
import it.lf.sorbet.services.impl.SnaiValueNormalizer;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SnaiCrawler extends AbstractCrawler {

    private static Logger LOG = LogManager.getLogger(SnaiCrawler.class);

    @Autowired
    private SnaiValueNormalizer snaiValueNormalizer;

    @Override
    public String getBookmakerId() {
        return "Snai";
    }

    public List<Quote> crawl(String sport) {
        final List<Quote> quotes = new ArrayList<>();

        try {
            String url;
            if ("soccer".equals(sport)) {
                url = "https://www.snai.it/sport";
            } else if ("tennis".equals(sport)){
                url = "https://www.snai.it/sport";
            } else {
                throw new IllegalStateException("Target sport not set");
            }

            WebDriver driver = getWebDriver();

            driver.get(url);
            Thread.sleep(2000);
            (new WebDriverWait(driver, 20))
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".ccc-close"))).click();
            Thread.sleep(2000);
            if ("soccer".equals(sport)) {
                (new WebDriverWait(driver, 10))
                        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[aria-controls='CALCIO_0']"))).click();

                Thread.sleep(1000);
                (new WebDriverWait(driver, 10))
                        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".btn-group-justified")));
                driver.findElement(By.cssSelector(".btn-group-justified")).findElements(By.tagName("a")).get(2).click();
                Thread.sleep(5000);

            }

            Elements matches = null;
            Document doc = Jsoup.parse(driver.getPageSource());
            if ("soccer".equals(sport)) {
                matches = doc.select("#piuGiocate_CALCIO tr.ng-scope");
            } else if ("tennis".equals(sport)) {
                matches = doc.select("#piuGiocate_TENNIS tr.ng-scope");
            }

            matches.forEach(element -> {
                Elements match = element.select("td");
                Quote quote = new Quote();
                quote.addValue(Double.valueOf(match.get(1).text().replace(',', '.')));
                quote.addValue(Double.valueOf(match.get(2).text().replace(',', '.')));
                if ("soccer".equals(sport)) {
                    quote.addValue(Double.valueOf(match.get(3).text().replace(',', '.')));
                }

                String[] teamsText = match.get(0).select("a").get(0).text().split("-");
                String alias1 = teamsText[0].trim().replace(",", " ");
                String alias2 = teamsText[1].trim().replace(",", " ");
                quote.setAlias1(alias1);
                quote.setAlias2(alias2);
                quote.setNormalizedAlias1(snaiValueNormalizer.normalizeAlias(alias1));
                quote.setNormalizedAlias2(snaiValueNormalizer.normalizeAlias(alias2));

                quotes.add(quote);
            });

        } catch (Exception e) {
            LOG.error("Connection exception", e);
            return Collections.EMPTY_LIST;
        } finally {
            getWebDriver().quit();
        }
        return quotes;
    }
}
