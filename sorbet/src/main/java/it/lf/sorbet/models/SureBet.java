package it.lf.sorbet.models;

import java.util.ArrayList;
import java.util.List;

public class SureBet {

    private List<Bookmaker> bookmakers = new ArrayList<>();

    private SportsMatch sportsMatch;

    private double sureBetCoefficient;
    private double returnPercentage;

    private List<Double> bets = new ArrayList<>();
    private double totalBet;

    public void addBookmaker(Bookmaker bookmaker) {
        this.bookmakers.add(bookmaker);
    }

    public List<Bookmaker> getBookmakers() {
        return this.bookmakers;
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

    public void addBet(Double bet) {
        this.bets.add(bet);
    }

    public List<Double> getBets() {
        return bets;
    }

    public double getTotalBet() {
        return totalBet;
    }

    public void setTotalBet(double totalBet) {
        this.totalBet = totalBet;
    }



}
