package it.lf;


import it.lf.sorbet.models.Bookmaker;
import it.lf.sorbet.models.Quote;
import it.lf.sorbet.models.SportsMatch;
import it.lf.sorbet.models.SureBet;
import it.lf.sorbet.services.impl.SureBetServiceImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static it.lf.utils.DataPreparationUtils.buildBookmaker;
import static org.junit.Assert.*;

public class SureBetServiceImplUnitTest
{

    private final static double DELTA = 0.0001;

    private SureBetServiceImpl surebetServiceImpl = new SureBetServiceImpl();

    @Test
    public void getSureBet_no()
    {
        SportsMatch sportsMatch = new SportsMatch();
        sportsMatch.setQuotes(generateQuotesWithoutSurebet());
        SureBet sureBet = surebetServiceImpl.getSureBet(sportsMatch);
        assertNull(sureBet);
    }

    @Test
    public void getSureBet()
    {
        SportsMatch sportsMatch = new SportsMatch();
        sportsMatch.setQuotes(generateQuotesWithSurebet());
        SureBet sureBet = surebetServiceImpl.getSureBet(sportsMatch);

        assertEquals(0.9453, sureBet.getSureBetCoefficient(), DELTA);
        assertEquals(1.0577, sureBet.getReturnPercentage(), DELTA);
        assertEquals(58.8235, sureBet.getBetQ1(), DELTA);
        assertEquals(0.00, sureBet.getBetD(), DELTA);
        assertEquals(35.7142, sureBet.getBetQ2(), DELTA);
        assertEquals(94.5378, sureBet.getTotalBet(), DELTA);
        assertEquals("bm1", sureBet.getBookmakerQ1().getId());
        assertEquals("bm1", sureBet.getBookmakerD().getId());
        assertEquals("bm2", sureBet.getBookmakerQ2().getId());
    }

    private List<Quote> generateQuotesWithoutSurebet() {
        ArrayList<Quote> quotes = new ArrayList<Quote>();
        quotes.add(createQuote(1.30, Double.MAX_VALUE, 2.20, buildBookmaker().withId("bm1").build()));
        quotes.add(createQuote(1.50, Double.MAX_VALUE, 2.50, buildBookmaker().withId("bm2").build()));
        quotes.add(createQuote(1.65, Double.MAX_VALUE, 2.30, buildBookmaker().withId("bm3").build()));
        return quotes;
    }

    private List<Quote> generateQuotesWithSurebet() {
        ArrayList<Quote> quotes = new ArrayList<Quote>();
        quotes.add(createQuote(1.70, Double.MAX_VALUE, 2.20, buildBookmaker().withId("bm1").build()));
        quotes.add(createQuote(1.50, Double.MAX_VALUE, 2.80, buildBookmaker().withId("bm2").build()));
        quotes.add(createQuote(1.65, Double.MAX_VALUE, 2.75, buildBookmaker().withId("bm3").build()));
        return quotes;
    }

    private Quote createQuote(double q1, double d, double q2, Bookmaker bookmaker) {
        Quote quote = new Quote();
        quote.setQ1(q1);
        quote.setD(d);
        quote.setQ2(q2);
        quote.setBookmaker(bookmaker);
        return quote;
    }
}
