package it.lf.sorbet;

import it.lf.sorbet.crawlers.Crawler;
import it.lf.sorbet.crawlers.impl.*;
import it.lf.sorbet.models.Bookmaker;
import it.lf.sorbet.models.Quote;
import it.lf.sorbet.models.SportsMatch;
import it.lf.sorbet.models.SureBet;
import it.lf.sorbet.services.SportsMatchService;
import it.lf.sorbet.services.SureBetService;
import it.lf.sorbet.services.impl.SportsMatchServiceImpl;
import it.lf.sorbet.services.impl.SureBetServiceImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


public class QuoteCrawler
{
    private static final Logger LOG = LogManager.getLogger(QuoteCrawler.class);

    private static SureBetService sureBetService = new SureBetServiceImpl();
    private static SportsMatchService sportsMatchService = new SportsMatchServiceImpl();

    public static void main( String[] args ) {

        List<Quote> quotes = new ArrayList<Quote>();

        Bookmaker bm1 = new Bookmaker();
        bm1.setId("BWin");
        bm1.setCrawler(new BWinCrawler());
        crawlPage(quotes, bm1);

        Bookmaker bm2 = new Bookmaker();
        bm2.setId("EuroBet");
        bm2.setCrawler(new EuroBetCrawler());
        crawlPage(quotes, bm2);

        Bookmaker bm3 = new Bookmaker();
        bm3.setId("WilliamHill");
        bm3.setCrawler(new WilliamHillCrawler());
        crawlPage(quotes, bm3);

        Bookmaker bm4 = new Bookmaker();
        bm4.setId("BetFair");
        bm4.setCrawler(new BetFairCrawler());
        crawlPage(quotes, bm4);

        Bookmaker bm5 = new Bookmaker();
        bm5.setId("Snai");
        bm5.setCrawler(new SnaiCrawler());
        crawlPage(quotes, bm5);

        Bookmaker bm6 = new Bookmaker();
        bm6.setId("GazzaBet");
        bm6.setCrawler(new GazzaBetCrawler());
        crawlPage(quotes, bm6);

        writeQuotes(quotes);
    }

    private static void crawlPage(List<Quote> quotes, Bookmaker bookmaker) {
        List<Quote> crawledQuotes = bookmaker.getCrawler().crawl();

        for (Quote quote : crawledQuotes) {
            quote.setBookmaker(bookmaker);
        }

        quotes.addAll(crawledQuotes);
    }

    private static void writeQuotes(List<Quote> quotes) {
        FileWriter fileWriter = null;
        CSVPrinter csvPrinter = null;
        try {
            fileWriter = new FileWriter("C:\\quotes.csv");
            csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);

            for (Quote quote : quotes) {
                List record = new ArrayList();
                record.add(quote.getBookmaker().getId());
                record.add(quote.getAliasTeam1());
                record.add(quote.getAliasTeam2());
                record.add(quote.getQ1());
                record.add(quote.getD());
                record.add(quote.getQ2());
                csvPrinter.printRecord(record);
            }
            LOG.info("Finished writing quotes file");
        } catch (IOException ioe) {
            LOG.error("Error writing quotes file. ", ioe);
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvPrinter.close();
            } catch (IOException ioe) {
                LOG.error("Error flushing/closing quotes file during writing" , ioe);
            }
        }

    }
}
