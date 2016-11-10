package it.lf.sorbet.runners;

import it.lf.sorbet.crawlers.Crawler;
import it.lf.sorbet.models.Bookmaker;
import it.lf.sorbet.models.Quote;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class QuoteCrawler
{
    private static final Logger LOG = LogManager.getLogger(QuoteCrawler.class);

    @Resource(name = "crawlers")
    private List<Crawler> crawlers;

    public void run() {

        List<Quote> quotes = new ArrayList<Quote>();

        for (Crawler crawler : crawlers) {
            Bookmaker bm = new Bookmaker();
            String bookmakerId = crawler.getBookmakerId();
            bm.setId(bookmakerId);
            bm.setCrawler(crawler);
            LOG.info("Started crawler: " + bookmakerId);
            try {
                crawlPage(quotes, bm);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }

            LOG.info("Finished crawler: " + bookmakerId);
        }

        writeQuotes(quotes);
    }

    private void crawlPage(List<Quote> quotes, Bookmaker bookmaker) {
        List<Quote> crawledQuotes = bookmaker.getCrawler().crawl();

        for (Quote quote : crawledQuotes) {
            quote.setBookmaker(bookmaker);
        }

        quotes.addAll(crawledQuotes);
    }

    private void writeQuotes(List<Quote> quotes) {
        LOG.info("Started writing quotes file");
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
