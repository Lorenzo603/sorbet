package it.lf.sorbet.models;

public class Quote {

    private Bookmaker bookmaker;
    private SportsMatch sportsMatch;
    private String aliasTeam1;
    private String aliasTeam2;

    private double q1;
    private double q2;
    private double d;

    public Bookmaker getBookmaker() {
        return bookmaker;
    }

    public void setBookmaker(Bookmaker bookmaker) {
        this.bookmaker = bookmaker;
    }


    public double getQ1() {
        return q1;
    }

    public void setQ1(double q1) {
        this.q1 = q1;
    }

    public double getQ2() {
        return q2;
    }

    public void setQ2(double q2) {
        this.q2 = q2;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }


    public SportsMatch getSportsMatch() {
        return sportsMatch;
    }

    public void setSportsMatch(SportsMatch sportsMatch) {
        this.sportsMatch = sportsMatch;
    }

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
}
