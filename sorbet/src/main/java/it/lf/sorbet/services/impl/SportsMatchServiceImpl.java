package it.lf.sorbet.services.impl;

import info.debatty.java.stringsimilarity.Levenshtein;
import it.lf.sorbet.models.Quote;
import it.lf.sorbet.models.SportsMatch;
import it.lf.sorbet.services.SportsMatchService;
import it.lf.sorbet.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class SportsMatchServiceImpl implements SportsMatchService {

    @Autowired
    private TeamService teamService;

    private HashMap<String, SportsMatch> sportsMatchHashMap;

    private Levenshtein levenshtein = new Levenshtein();

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
            retrievedSportsMatch.getQuotes().add(quote);
        } else {
            SportsMatch similarSportsMatch = findSportsMatchWithSimilarKey(key);
            if (similarSportsMatch != null) {
                quote.setSportsMatch(similarSportsMatch);
                similarSportsMatch.getQuotes().add(quote);
            } else {
                SportsMatch sportsMatch = new SportsMatch();
                sportsMatch.setAliasTeam1(quote.getAliasTeam1());
                sportsMatch.setAliasTeam2(quote.getAliasTeam2());
                quote.setSportsMatch(sportsMatch);
                sportsMatch.setQuotes(new ArrayList<Quote>(Arrays.asList(quote)));
                sportsMatchHashMap.put(key, sportsMatch);
            }
        }
    }

    private SportsMatch findSportsMatchWithSimilarKey(String key) {
        for (String existingKey : sportsMatchHashMap.keySet()) {
            if (areSimilar(existingKey, key)) {
                return sportsMatchHashMap.get(existingKey);
            }
        }
        return null;
    }

    private boolean areSimilar(String existingKey, String key) {
        return levenshtein.distance(existingKey, key) > 0.95;
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
