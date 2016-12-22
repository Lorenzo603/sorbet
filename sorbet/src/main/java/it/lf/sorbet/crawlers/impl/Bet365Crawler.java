package it.lf.sorbet.crawlers.impl;

import it.lf.sorbet.crawlers.Crawler;
import it.lf.sorbet.models.Quote;
import it.lf.sorbet.services.impl.Bet365ValueNormalizer;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class Bet365Crawler extends AbstractCrawler {

    private static Logger LOG = LogManager.getLogger(WilliamHillCrawler.class);

    @Autowired
    private Bet365ValueNormalizer bet365ValueNormalizer;

    @Override
    public String getBookmakerId() {
        return "Bet365";
    }

    @Override
    public List crawl(String sport) {
        final List<Quote> quotes = new ArrayList<>();

        try {
            String url;
            if ("soccer".equals(sport)) {
                url = "http://www.bet365.it/?lng=6&cb=10326422110#/AC/B13/C1/D50/E2/F163/";
            } else if ("tennis".equals(sport)) {
                url = "http://www.bet365.it/?lng=6&cb=10326422110#/AC/B13/C1/D50/E2/F163/";
            } else {
                throw new IllegalStateException("Target sport not set");
            }

            WebDriver driver = getWebDriver();

            driver.get(url);

            (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector(".lpdgl"))).click();

            Thread.sleep(2000);

            driver.get(url);

            Thread.sleep(2000);

            Document doc = Jsoup.parse(driver.getPageSource());

            if ("soccer".equals(sport)) {

            } else if ("tennis".equals(sport)) {
                Elements matchGroups = doc.select(".gl-MarketGroupContainer");
                matchGroups.forEach(matchGroup -> {

                    List<Quote> groupQuotes = new ArrayList<>();

                    Elements matches = matchGroup.select(".sl-CouponParticipantWithBookCloses");
                    matches.forEach(match -> {
                        Quote quote = new Quote();

                        String[] names = match.select(".sl-CouponParticipantWithBookCloses_Name").text().split(" v ");
                        String alias1 = names[0];
                        String alias2 = names[1];

                        quote.setAlias1(alias1);
                        quote.setAlias2(alias2);
                        quote.setNormalizedAlias1(bet365ValueNormalizer.normalizeAlias(alias1));
                        quote.setNormalizedAlias2(bet365ValueNormalizer.normalizeAlias(alias2));

                        groupQuotes.add(quote);
                    });

                    Elements odds = matchGroup.select(".gl-ParticipantOddsOnlyDarker");
                    IntStream.range(0, groupQuotes.size()).forEach(index -> {
                        Quote currentQuote = groupQuotes.get(index);
                        String oddValue1 = odds.select(".gl-ParticipantOddsOnly_Odds").get(index).text();
                        String oddValue2 = odds.select(".gl-ParticipantOddsOnly_Odds").get(index + groupQuotes.size()).text();
                        if (StringUtils.isNotEmpty(oddValue1) && StringUtils.isNotEmpty(oddValue2)) {
                            currentQuote.addValue(Double.valueOf(oddValue1));
                            currentQuote.addValue(Double.valueOf(oddValue2));
                        }
                    });

                    quotes.addAll(groupQuotes);

                });
            }

        } catch (Exception e) {
            LOG.error("Connection exception", e);
            return Collections.EMPTY_LIST;
        } finally {
            getWebDriver().quit();
        }

        return quotes.stream().filter(q -> !q.getValues().isEmpty()).collect(Collectors.toList());
    }
}
