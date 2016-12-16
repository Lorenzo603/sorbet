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

        double[] bestValues = new double[sportsMatch.getQuotes().get(0).getValues().size()];
        for (int i = 0; i < bestValues.length; i++) {
            bestValues[i] = Double.MIN_VALUE;
        }

        Bookmaker[] bookmakers = new Bookmaker[bestValues.length];

        for (Quote quote : sportsMatch.getQuotes()) {
            for (int i = 0; i < quote.getValues().size(); i ++) {
                if (quote.getValues().get(i) > bestValues[i]) {
                    bestValues[i] = quote.getValues().get(i);
                    bookmakers[i] = quote.getBookmaker();
                }
            }
        }


        double surebetCoefficient = 0;
        for (int i = 0; i < bestValues.length; i++) {
            surebetCoefficient += 1/bestValues[i];
        }

        LOG.info("Match " + sportsMatch.getAlias1() + " - " + sportsMatch.getAlias2() + " analyzed. Coefficient: " + surebetCoefficient);
        if (surebetCoefficient < 1.0) {
            double targetAmount = 100.00;

            SureBet sureBet = new SureBet();
            sureBet.setSureBetCoefficient(surebetCoefficient);
            sureBet.setReturnPercentage(1/surebetCoefficient);

            double bets[] = new double[bestValues.length];
            double totalBet = 0;
            for (int i = 0; i< bestValues.length; i++) {
                bets[i] = targetAmount/bestValues[i];
                sureBet.addBet(bets[i]);
                totalBet += bets[i];
            }
            sureBet.setTotalBet(totalBet);

            for (int i = 0; i < bookmakers.length; i++) {
                sureBet.addBookmaker(bookmakers[i]);
            }

            sureBet.setSportsMatch(sportsMatch);

            return sureBet;
        }

        return null;
    }
}
