package it.lf.sorbet;

import it.lf.sorbet.models.Bookmaker;
import it.lf.sorbet.models.Quote;
import it.lf.sorbet.models.SportsMatch;
import it.lf.sorbet.models.SureBet;
import it.lf.sorbet.services.SportsMatchService;
import it.lf.sorbet.services.SureBetService;
import it.lf.sorbet.services.impl.SportsMatchServiceImpl;
import it.lf.sorbet.services.impl.SureBetServiceImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


public class QuoteAnalyzer
{
    private static final Logger LOG = LogManager.getLogger(QuoteAnalyzer.class);

    private static SureBetService sureBetService = new SureBetServiceImpl();
    private static SportsMatchService sportsMatchService = new SportsMatchServiceImpl();

    public static void main( String[] args ) {
        List<Quote> quotes = loadQuotes();
        for (SportsMatch sportsMatch : sportsMatchService.findAndAssignAllSportMatchesAndQuotes(quotes)) {
            SureBet surebet = sureBetService.getSureBet(sportsMatch);
            if (surebet != null) {
                LOG.info("----------  SureBet found ----------");
                LOG.info("sports Match: " + surebet.getSportsMatch().getAliasTeam1() + " - " + surebet.getSportsMatch().getAliasTeam2());
                LOG.info("Coefficient: " + surebet.getSureBetCoefficient());
                LOG.info("Return percentage: " + surebet.getReturnPercentage());
                LOG.info("Bet Q1: " + surebet.getBetQ1());
                LOG.info("Bet D: " + surebet.getBetD());
                LOG.info("Bet Q2: " + surebet.getBetQ2());
                LOG.info("Bookmaker Q1: " + surebet.getBookmakerQ1().getId());
                LOG.info("Bookmaker D: " + surebet.getBookmakerD().getId());
                LOG.info("Bookmaker Q2: " + surebet.getBookmakerQ2().getId());
            }
        }
    }

    private static List<Quote> loadQuotes() {
        List<Quote> quotes = new ArrayList<Quote>();

        try {
            Reader in = new FileReader("C:\\quotes.csv");
            Iterable<CSVRecord> records = null;
            records = CSVFormat.EXCEL.parse(in);
            for (CSVRecord record : records) {
                Quote quote = new Quote();
                Bookmaker bookmaker = new Bookmaker();
                bookmaker.setId(record.get(0));
                quote.setBookmaker(bookmaker);
                quote.setAliasTeam1(record.get(1));
                quote.setAliasTeam2(record.get(2));
                quote.setQ1(Double.valueOf(record.get(3)));
                quote.setD(Double.valueOf(record.get(4)));
                quote.setQ2(Double.valueOf(record.get(5)));
                quotes.add(quote);
            }
            return quotes;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
