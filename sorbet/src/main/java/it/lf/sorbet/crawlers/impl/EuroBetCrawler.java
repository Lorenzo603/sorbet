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
import java.util.List;

@Service
public class EuroBetCrawler extends AbstractCrawler {

    private static Logger LOG = LogManager.getLogger(EuroBetCrawler.class);

    @Override
    public String getBookmakerId() {
        return "EuroBet";
    }

    public List<Quote> crawl() {
        final List<Quote> quotes = new ArrayList<Quote>();

        try {
            String url = getCrawlerConfig().getString("url");
            System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\GeckoDriver\\geckodriver.exe");
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
            Elements categories = doc.select(".left_menu_tipoSport_box_competizione_nome");

            categories.forEach(category -> {
                (new WebDriverWait(driver, 10))
                        .until(ExpectedConditions.presenceOfElementLocated(By.id(category.id()))).click();

                sleep(200);

                Elements subcategories = category.select(".left_menu_tipoSport_box_competizione_categoria_nome");
                subcategories.forEach(subcategory -> {
                    (new WebDriverWait(driver, 10))
                            .until(ExpectedConditions.presenceOfElementLocated(By.id(subcategory.id()))).click();

                    sleep(200);

                    Elements matches = category.select(".box_container_scommesse_evento");
                    matches.forEach(element -> {
                        Elements match = element.select(".box_container_scommesse_quoteType");
                        Quote quote = new Quote();
                        quote.setQ1(Double.valueOf(match.get(0).text()));
                        quote.setD(Double.valueOf(match.get(1).text()));
                        quote.setQ2(Double.valueOf(match.get(2).text()));

                        String teams = element.select(".box_container_scommesse_nomeEvento").select("a").text();
                        quote.setAliasTeam1(teams.split("-")[0].trim());
                        quote.setAliasTeam2(teams.split("-")[1].trim());

                        quotes.add(quote);
                    });
                });



            });

        } catch (Exception e) {
            LOG.error("Connection exception", e);
        } finally {
            getWebDriver().quit();
        }
        return quotes;
    }
}
