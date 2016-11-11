package it.lf.sorbet.services.impl;


import it.lf.sorbet.models.Bookmaker;
import it.lf.sorbet.models.Quote;
import it.lf.sorbet.models.SportsMatch;
import it.lf.sorbet.models.SureBet;
import it.lf.sorbet.services.SureBetService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SureBetServiceImpl implements SureBetService {

    private static Logger LOG = LogManager.getLogger(SureBetServiceImpl.class);

    public SureBet getSureBet(SportsMatch sportsMatch) {
        if (sportsMatch.getQuotes().size() > 2) {
            return null;
        }

        double bestQ1 = Double.MIN_VALUE;
        double bestD = Double.MIN_VALUE;
        double bestQ2 = Double.MIN_VALUE;

        Bookmaker bookmakerQ1 = null;
        Bookmaker bookmakerD = null;
        Bookmaker bookmakerQ2 = null;

        for (Quote quote : sportsMatch.getQuotes()) {
            if (quote.getQ1() > bestQ1) {
                bestQ1 = quote.getQ1();
                bookmakerQ1 = quote.getBookmaker();
            }
            if (quote.getD() > bestD) {
                bestD = quote.getD();
                bookmakerD = quote.getBookmaker();
            }
            if (quote.getQ2() > bestQ2) {
                bestQ2 = quote.getQ2();
                bookmakerQ2 = quote.getBookmaker();
            }
        }

        double surebetCoefficient = 1/bestQ1 + 1/bestD + 1/bestQ2;
        LOG.info("Match " + sportsMatch.getAliasTeam1() + " - " + sportsMatch.getAliasTeam2() + " analyzed. Coefficient: " + surebetCoefficient);
        if (surebetCoefficient < 1.0) {
            double targetAmount = 100.00;

            SureBet sureBet = new SureBet();
            sureBet.setSureBetCoefficient(surebetCoefficient);
            sureBet.setReturnPercentage(1/surebetCoefficient);

            double betQ1 = targetAmount/bestQ1;
            sureBet.setBetQ1(betQ1);
            double betD = targetAmount/bestD;
            sureBet.setBetD(betD);
            double betQ2 = targetAmount/bestQ2;
            sureBet.setBetQ2(betQ2);
            sureBet.setTotalBet(betQ1 + betD + betQ2);
            sureBet.setBookmakerQ1(bookmakerQ1);
            sureBet.setBookmakerD(bookmakerD);
            sureBet.setBookmakerQ2(bookmakerQ2);
            sureBet.setSportsMatch(sportsMatch);

            return sureBet;
        }

        return null;
    }
}
