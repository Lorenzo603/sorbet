package it.lf.sorbet.services;

import it.lf.sorbet.models.SportsMatch;
import it.lf.sorbet.models.SureBet;

public interface SureBetService {

    SureBet getSureBet(SportsMatch sportsMatch);

}
