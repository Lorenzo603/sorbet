package it.lf.sorbet.services.impl;

import it.lf.sorbet.models.Quote;
import it.lf.sorbet.models.SportsMatch;
import it.lf.sorbet.services.SportsMatchService;
import it.lf.sorbet.services.TeamService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class SportsMatchServiceImpl implements SportsMatchService {

    private HashMap<String, SportsMatch> sportsMatchHashMap;
    private TeamService teamService = new TeamServiceImpl();

    public List<SportsMatch> findAndAssignAllSportMatchesAndQuotes(List<Quote> quotes) {
        sportsMatchHashMap = new HashMap<String, SportsMatch>();
        for (Quote quote : quotes) {
            assignSportsMatchForQuote(quote);
        }

        List<SportsMatch> sportsMatches = new ArrayList<SportsMatch>();
        for (SportsMatch sm : sportsMatchHashMap.values()) {
            sportsMatches.add(sm);
        }
        return sportsMatches;
    }

    private void assignSportsMatchForQuote(Quote quote) {
        String key = generateSportsMatchMapKeyFromQuote(quote);
        SportsMatch retrievedSportsMatch = sportsMatchHashMap.get(key);
        if (retrievedSportsMatch != null) {
            quote.setSportsMatch(retrievedSportsMatch);
            List<Quote> previousQuotes = retrievedSportsMatch.getQuotes();
            if (previousQuotes == null) {
                previousQuotes = new ArrayList<Quote>();
            }
            previousQuotes.add(quote);
            retrievedSportsMatch.setQuotes(previousQuotes);
        } else {
            SportsMatch sportsMatch = new SportsMatch();
            sportsMatch.setAliasTeam1(quote.getAliasTeam1());
            sportsMatch.setAliasTeam2(quote.getAliasTeam2());
            quote.setSportsMatch(sportsMatch);
            sportsMatch.setQuotes(new ArrayList<Quote>(Arrays.asList(quote)));
            sportsMatchHashMap.put(key, sportsMatch);
        }
    }

    private String generateSportsMatchMapKeyFromQuote(Quote quote) {
        return generateSportsMatchMapKey(quote.getAliasTeam1(), quote.getAliasTeam2());
    }

    private String generateSportsMatchMapKey(String aliasTeam1, String aliasTeam2) {
        String team1 = teamService.getTeamIdByAlias(aliasTeam1);
        if (team1 == null) {
            throw new RuntimeException("Team with alias " + aliasTeam1 + " not found.");
        }
        String team2 = teamService.getTeamIdByAlias(aliasTeam2);
        if (team2 == null) {
            throw new RuntimeException("Team with alias " + aliasTeam2 + " not found.");
        }
        return team1 + team2;
    }

}
