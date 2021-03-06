package it.lf.sorbet.crawlers.impl;

import it.lf.sorbet.models.Quote;
import it.lf.sorbet.services.ValueNormalizer;
import it.lf.sorbet.services.impl.GazzabetValueNormalizer;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class GazzaBetCrawler extends AbstractCrawler {
    private static Logger LOG = LogManager.getLogger(GazzaBetCrawler.class);

    @Autowired
    private GazzabetValueNormalizer gazzabetValueNormalizer;

    @Override
    public String getBookmakerId() {
        return "GazzaBet";
    }

    public List<Quote> crawl(String sport) {
        final List<Quote> quotes = new ArrayList<>();

        try {
            String url;
            if ("soccer".equals(sport)) {
                url = "http://sports.gazzabet.it/it/s/FOOT/Calcio";
            } else if ("tennis".equals(sport)){
                url = "http://sports.gazzabet.it/it/s/TENN/Tennis";
            } else {
                throw new IllegalStateException("Target sport not set");
            }

            WebDriver driver = getWebDriver();

            driver.get(url);

            List<String> links = new ArrayList<>();
            List<WebElement> subcategories = new ArrayList<>();
            if ("soccer".equals(sport)) {
                subcategories = driver.findElements(By.cssSelector("#nav-area .sport-FOOT .expander-content > .expander a"));
            } else if ("tennis".equals(sport)) {
                subcategories = driver.findElements(By.cssSelector("#nav-area .sport-TENN .expander-content > .expander a"));
            }

            for (WebElement subcategory : subcategories) {
                links.add(subcategory.getAttribute("href"));
            }

            for (String subcategoryUrl : links) {
                driver.get(subcategoryUrl);
                sleep(200);
                Document doc = Jsoup.parse(driver.getPageSource());
                Elements matches = doc.select(".coupon .mkt_content");

                matches.forEach(element -> {
                    try {
                        Elements prices = element.select("td");
                        Quote quote = new Quote();
                        quote.addValue(Double.valueOf(prices.get(3).select(".price .dec").text()));
                        quote.addValue(Double.valueOf(prices.get(4).select(".price .dec").text()));
                        if ("soccer".equals(sport)) {
                            quote.addValue(Double.valueOf(prices.get(5).select(".price .dec").text()));
                        }

                        String alias1 = prices.get(3).select(".seln-name").text();
                        quote.setAlias1(alias1);
                        quote.setNormalizedAlias1(gazzabetValueNormalizer.normalizeAlias(alias1));
                        String alias2 = StringUtils.EMPTY;
                        if ("soccer".equals(sport)) {
                            alias2 = prices.get(5).select(".seln-name").text();
                        } else if ("tennis".equals(sport)) {
                            alias2 = prices.get(4).select(".seln-name").text();
                        }
                        quote.setAlias2(alias2);
                        quote.setNormalizedAlias2(gazzabetValueNormalizer.normalizeAlias(alias2));

                        quotes.add(quote);
                    } catch (Exception e) {
                        LOG.error(e.getMessage());
                    }

                });
            }


        } catch (Exception e) {
            LOG.error("Connection exception", e);
        } finally {
            getWebDriver().quit();
        }

        return quotes;
    }
}
