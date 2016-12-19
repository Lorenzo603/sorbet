package it.lf.sorbet.runners;

import it.lf.sorbet.models.Bookmaker;
import it.lf.sorbet.models.Quote;
import it.lf.sorbet.models.SportsMatch;
import it.lf.sorbet.models.SureBet;
import it.lf.sorbet.services.SportsMatchService;
import it.lf.sorbet.services.SureBetService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuoteAnalyzer
{
    private static final Logger LOG = LogManager.getLogger(QuoteAnalyzer.class);

    @Autowired
    private SureBetService sureBetService;
    @Autowired
    private SportsMatchService sportsMatchService;

    public void run() {
        List<Quote> quotes = loadQuotes();
        if (quotes != null) {
            for (SportsMatch sportsMatch : sportsMatchService.findAndAssignAllSportMatchesAndQuotes(quotes)) {
                SureBet surebet = sureBetService.getSureBet(sportsMatch);
                if (surebet != null && sportsMatch.getQuotes().size() <= 3) {
                    LOG.info("----------  SureBet found ----------");
                    LOG.info("sports Match: " + surebet.getSportsMatch().getAlias1() + " - " + surebet.getSportsMatch().getAlias2());
                    LOG.info("Coefficient: " + surebet.getSureBetCoefficient());
                    LOG.info("Return percentage: " + surebet.getReturnPercentage());
                    for (Double bet : surebet.getBets()) {
                        LOG.info("Bets: " + bet);
                    }
                    for (Bookmaker bookmaker : surebet.getBookmakers()) {
                        LOG.info("Bookmakers: " + bookmaker.getId());
                    }

                    LOG.info("SureBet Quotes ---");
                    for (Quote quote : surebet.getSportsMatch().getQuotes()) {
                        LOG.info(quote.getAlias1() + "$$$" + quote.getAlias2());
                    }

                }
            }
        }
    }

    private static List<Quote> loadQuotes() {
        List<Quote> quotes = new ArrayList<Quote>();

        try {
            File quoteDir = new File("C:\\p");
            for (File file : Arrays.stream(quoteDir.listFiles()).filter(f -> f.getName().endsWith(".csv")).collect(Collectors.toList())) {
                Reader in = new FileReader(file);
                Iterable<CSVRecord> records = null;
                records = CSVFormat.EXCEL.parse(in);
                for (CSVRecord record : records) {
                    Quote quote = new Quote();
                    Bookmaker bookmaker = new Bookmaker();
                    bookmaker.setId(record.get(0));
                    quote.setBookmaker(bookmaker);
                    quote.setAlias1(record.get(1));
                    quote.setAlias2(record.get(2));
                    for (int i = 3; i <  record.size() ;i++) {
                        quote.addValue(Double.valueOf(record.get(i)));
                    }
                    quotes.add(quote);
                }
                return quotes;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
