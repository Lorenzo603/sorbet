package it.lf.sorbet.crawlers.impl;

import it.lf.sorbet.models.Quote;
import it.lf.sorbet.services.impl.WilliamHillValueNormalizer;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class WilliamHillCrawler extends AbstractCrawler {

    private static Logger LOG = LogManager.getLogger(WilliamHillCrawler.class);

    @Autowired
    private WilliamHillValueNormalizer williamHillValueNormalizer;

    @Override
    public String getBookmakerId() {
        return "WilliamHill";
    }

    public List<Quote> crawl(String sport) {
        final List<Quote> quotes = new ArrayList<>();

        try {
            String url;
            if ("soccer".equals(sport)) {
                url = "http://sports.williamhill.it/bet_ita/it/betting/y/5/Calcio.html";
            } else if ("tennis".equals(sport)){
                url = "http://sports.williamhill.it/bet_ita/it/betting/y/17/mh/Tennis.html";
            } else {
                throw new IllegalStateException("Target sport not set");
            }

            WebDriver driver = getWebDriver();

            driver.get(url);
            (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id("popupClose"))).click();

            if ("soccer".equals(sport)) {
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
                            quote.addValue(Double.valueOf(match.get(4).select(".eventprice").text()));
                            quote.addValue(Double.valueOf(match.get(5).select(".eventprice").text()));
                            quote.addValue(Double.valueOf(match.get(6).select(".eventprice").text()));

                            String[] teamText = StringEscapeUtils.unescapeHtml4(match.get(2).select("span").text()).split("-");
                            quote.setAlias1(teamText[0].trim());
                            quote.setAlias2(teamText[1].trim());

                            quotes.add(quote);
                        });
                    } catch (Exception e) {
                        LOG.error(e.getMessage());
                    }

                }
            } else if ("tennis".equals(sport)) {

                Document doc = Jsoup.parse(driver.getPageSource());
                Elements matches = doc.select("tr.rowOdd");

                matches.forEach(element -> {
                    Elements match = element.select("td[scope='col']");
                    Quote quote = new Quote();
                    quote.addValue(Double.valueOf(match.get(2).select(".eventprice").text()));
                    quote.addValue(Double.valueOf(match.get(4).select(".eventprice").text()));

                    String[] teamText = StringEscapeUtils.unescapeHtml4(match.get(3).select("span").text()).split(" - ");

                    String alias1 = teamText[0];
                    String alias2 = teamText[1];
                    quote.setAlias1(alias1);
                    quote.setAlias2(alias2);
                    quote.setNormalizedAlias1(williamHillValueNormalizer.normalizeAlias(alias1));
                    quote.setNormalizedAlias2(williamHillValueNormalizer.normalizeAlias(alias2));

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
