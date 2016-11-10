package it.lf.sorbet.services;

import it.lf.sorbet.models.Quote;
import it.lf.sorbet.models.SportsMatch;

import java.util.List;

public interface SportsMatchService {

    List<SportsMatch> findAndAssignAllSportMatchesAndQuotes(List<Quote> quotes);

}
