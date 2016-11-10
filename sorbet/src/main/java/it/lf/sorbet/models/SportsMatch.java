package it.lf.sorbet.models;


import java.util.List;

public class SportsMatch {

    private String aliasTeam1;
    private String aliasTeam2;

    private List<Quote> quotes;

    public String getAliasTeam1() {
        return aliasTeam1;
    }

    public void setAliasTeam1(String aliasTeam1) {
        this.aliasTeam1 = aliasTeam1;
    }

    public String getAliasTeam2() {
        return aliasTeam2;
    }

    public void setAliasTeam2(String aliasTeam2) {
        this.aliasTeam2 = aliasTeam2;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }
}
