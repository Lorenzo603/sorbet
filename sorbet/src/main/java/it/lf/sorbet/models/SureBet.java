package it.lf.sorbet.models;

public class SureBet {

    private Bookmaker bookmakerQ1;
    private Bookmaker bookmakerD;
    private Bookmaker bookmakerQ2;

    private SportsMatch sportsMatch;

    private double sureBetCoefficient;
    private double returnPercentage;

    private double betQ1;
    private double betD;
    private double betQ2;
    private double totalBet;

    public Bookmaker getBookmakerQ1() {
        return bookmakerQ1;
    }

    public void setBookmakerQ1(Bookmaker bookmakerQ1) {
        this.bookmakerQ1 = bookmakerQ1;
    }

    public Bookmaker getBookmakerD() {
        return bookmakerD;
    }

    public void setBookmakerD(Bookmaker bookmakerD) {
        this.bookmakerD = bookmakerD;
    }

    public Bookmaker getBookmakerQ2() {
        return bookmakerQ2;
    }

    public void setBookmakerQ2(Bookmaker bookmakerQ2) {
        this.bookmakerQ2 = bookmakerQ2;
    }

    public SportsMatch getSportsMatch() {
        return sportsMatch;
    }

    public void setSportsMatch(SportsMatch sportsMatch) {
        this.sportsMatch = sportsMatch;
    }

    public double getSureBetCoefficient() {
        return sureBetCoefficient;
    }

    public void setSureBetCoefficient(double sureBetCoefficient) {
        this.sureBetCoefficient = sureBetCoefficient;
    }

    public double getReturnPercentage() {
        return returnPercentage;
    }

    public void setReturnPercentage(double returnPercentage) {
        this.returnPercentage = returnPercentage;
    }

    public double getBetQ1() {
        return betQ1;
    }

    public void setBetQ1(double betQ1) {
        this.betQ1 = betQ1;
    }

    public double getBetD() {
        return betD;
    }

    public void setBetD(double betD) {
        this.betD = betD;
    }

    public double getBetQ2() {
        return betQ2;
    }

    public void setBetQ2(double betQ2) {
        this.betQ2 = betQ2;
    }

    public double getTotalBet() {
        return totalBet;
    }

    public void setTotalBet(double totalBet) {
        this.totalBet = totalBet;
    }
}
