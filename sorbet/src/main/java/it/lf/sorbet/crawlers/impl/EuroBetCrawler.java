package it.lf.sorbet.crawlers.impl;

import it.lf.sorbet.models.Quote;
import it.lf.sorbet.services.ValueNormalizer;
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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EuroBetCrawler extends AbstractCrawler {

    private static Logger LOG = LogManager.getLogger(EuroBetCrawler.class);

    @Resource(name = "eurobetValueNormalizer")
    private ValueNormalizer eurobetValueNormalizer;

    @Override
    public String getBookmakerId() {
        return "EuroBet";
    }

    public List<Quote> crawl(String sport) {
        final List<Quote> quotes = new ArrayList<Quote>();

        try {
            String url;
            if ("soccer".equals(sport)) {
                url = "http://web.eurobet.it/webeb/scommesse-sportive";
            } else if ("tennis".equals(sport)){
                url = "http://web.eurobet.it/webeb/scommesse-sportive";
            } else {
                throw new IllegalStateException("Target sport not set");
            }

            if ("soccer".equals(sport)) {
                WebDriver driver = getWebDriver();
                driver.get(url);
                driver.get(url);
                (new WebDriverWait(driver, 10))
                        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".btn_cookie"))).click();
                (new WebDriverWait(driver, 10))
                        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[title='HomePage']"))).click();

                (new WebDriverWait(driver, 10))
                        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[title='Serie A']")));

                Document doc = Jsoup.parse(driver.getPageSource());
                Elements categories = doc.select("#targetSport .left_menu_tipoSport_box_competizione_nome");

                categories.forEach(category -> {
                    (new WebDriverWait(driver, 10))
                            .until(ExpectedConditions.presenceOfElementLocated(By.id(category.id()))).click();

                    Document categoryDoc = Jsoup.parse(driver.getPageSource());

                    Elements subcategories = categoryDoc.select(".left_menu_tipoSport_box_competizione_categoria_nome");
                    subcategories.forEach(subcategory -> {
                        WebElement subcategoryElement = driver.findElement(By.id(subcategory.id()));
                        if (subcategoryElement.isDisplayed()) {
                            subcategoryElement.click();
                            sleep(200);

                            Document subcategoryDoc = Jsoup.parse(driver.getPageSource());

                            Elements matches = subcategoryDoc.select(".box_container_scommesse_evento");
                            matches.forEach(element -> {
                                try {
                                    Elements match = element.select(".box_container_scommesse_quoteType");
                                    Quote quote = new Quote();
                                    quote.addValue(Double.valueOf(match.get(0).text())); // 1
                                    quote.addValue(Double.valueOf(match.get(1).text())); // X
                                    quote.addValue(Double.valueOf(match.get(2).text())); // 2

                                    String teams = element.select(".box_container_scommesse_nomeEvento").select("a").text();
                                    String alias1 = teams.split("-")[0];
                                    String alias2 = teams.split("-")[1];
                                    quote.setAlias1(alias1);
                                    quote.setAlias2(alias2);
                                    quote.setNormalizedAlias1(eurobetValueNormalizer.normalizeAlias(alias1));
                                    quote.setNormalizedAlias2(eurobetValueNormalizer.normalizeAlias(alias2));

                                    quotes.add(quote);
                                } catch (Exception e) {
                                    LOG.error(e);
                                }

                            });
                        }


                    });

                });

            } else if ("tennis".equals(sport)) {
                WebDriver driver = getWebDriver();
                driver.get(url);
                driver.get(url);
                (new WebDriverWait(driver, 10))
                        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".btn_cookie"))).click();
                (new WebDriverWait(driver, 10))
                        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[title='HomePage']"))).click();

                Thread.sleep(5000);

                (new WebDriverWait(driver, 10))
                        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[title^='Tennis']"))).click();

                Thread.sleep(5000);

                (new WebDriverWait(driver, 10))
                        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#buttonToday"))).click();

                Thread.sleep(5000);

                Document subcategoryDoc = Jsoup.parse(driver.getPageSource());

                Elements matches = subcategoryDoc.select(".box_container_scommesse_evento");
                matches.forEach(element -> {
                    try {
                        Elements match = element.select(".box_container_scommesse_quoteType_2");
                        Quote quote = new Quote();
                        quote.addValue(Double.valueOf(match.get(0).text())); // 1
                        quote.addValue(Double.valueOf(match.get(1).text())); // 2

                        String teams = element.select(".box_container_scommesse_nomeEvento").select("a").text();
                        String alias1 = teams.split("-")[0];
                        String alias2 = teams.split("-")[1];
                        quote.setAlias1(alias1);
                        quote.setAlias2(alias2);
                        quote.setNormalizedAlias1(eurobetValueNormalizer.normalizeAlias(alias1));
                        quote.setNormalizedAlias2(eurobetValueNormalizer.normalizeAlias(alias2));

                        quotes.add(quote);
                    } catch (Exception e) {
                        LOG.error(e.getMessage(), e);
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
