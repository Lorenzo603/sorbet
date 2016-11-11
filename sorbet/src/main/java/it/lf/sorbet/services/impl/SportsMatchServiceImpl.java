package it.lf.sorbet.services.impl;

import info.debatty.java.stringsimilarity.Levenshtein;
import it.lf.sorbet.models.Quote;
import it.lf.sorbet.models.SportsMatch;
import it.lf.sorbet.services.SportsMatchService;
import it.lf.sorbet.services.StringSimilarityService;
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
    private StringSimilarityService stringSimilarityService;
    @Autowired
    private TeamService teamService;

    private HashMap<String, SportsMatch> sportsMatchHashMap;

    public List<SportsMatch> findAndAssignAllSportMatchesAndQuotes(List<Quote> quotes) {
        sportsMatchHashMap = new HashMap<String, SportsMatch>();
        for (Quote quote : quotes) {
            assignSportsMatchForQuote(quote);
        }

        List<SportsMatch> sportsMatches = new ArrayList<SportsMatch>();
        sportsMatches.addAll(sportsMatchHashMap.values());
        return sportsMatches;
    }

    private void assignSportsMatchForQuote(Quote quote) {
        String key = generateSportsMatchMapKeyFromQuote(quote);
        SportsMatch retrievedSportsMatch = sportsMatchHashMap.get(key);
        if (retrievedSportsMatch != null) {
            quote.setSportsMatch(retrievedSportsMatch);
            retrievedSportsMatch.getQuotes().add(quote);
        } else {
            SportsMatch similarSportsMatch = findSimilarSportsMatch(quote, key);
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

    private SportsMatch findSimilarSportsMatch(Quote quote, String key) {
        if (stringSimilarityService.areSimilar(quote.getAliasTeam1(), key.split("\\|\\|\\|")[0])
            && stringSimilarityService.areSimilar(quote.getAliasTeam2(), key.split("\\|\\|\\|")[1])) {
            for (String existingKey : sportsMatchHashMap.keySet()) {
                if (stringSimilarityService.areSimilar(existingKey, key)) {
                    return sportsMatchHashMap.get(existingKey);
                }
            }
        }

        return null;
    }

    private String generateSportsMatchMapKeyFromQuote(Quote quote) {
        return generateSportsMatchMapKey(quote.getAliasTeam1(), quote.getAliasTeam2());
    }

    private String generateSportsMatchMapKey(String aliasTeam1, String aliasTeam2) {
        String team1 = teamService.getTeamIdByAlias(aliasTeam1);
        if (team1 == null) {
            team1 = aliasTeam1;
        }
        String team2 = teamService.getTeamIdByAlias(aliasTeam2);
        if (team2 == null) {
            team2 = aliasTeam2;
        }
        return team1 + "|||" + team2;
    }

}
