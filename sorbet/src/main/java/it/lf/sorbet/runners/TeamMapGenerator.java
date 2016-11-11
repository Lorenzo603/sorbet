package it.lf.sorbet.runners;

import it.lf.sorbet.models.Quote;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Component
public class TeamMapGenerator
{
    private static final Logger LOG = LogManager.getLogger(TeamMapGenerator.class);

    public void run() {
        List<Quote> quotes = loadQuotes();
        for (Quote quote : quotes) {
            System.out.println("teamMap.put(\"" + quote.getAliasTeam1() + "\", Arrays.asList(\"" + quote.getAliasTeam1() + "\"));");
            System.out.println("teamMap.put(\"" + quote.getAliasTeam2() + "\", Arrays.asList(\"" + quote.getAliasTeam2() + "\"));");
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
                quote.setAliasTeam1(record.get(1));
                quote.setAliasTeam2(record.get(2));
                quotes.add(quote);
            }
            return quotes;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
