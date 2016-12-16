package it.lf.sorbet.models;

import java.util.ArrayList;
import java.util.List;

public class Quote {

    private Bookmaker bookmaker;
    private SportsMatch sportsMatch;
    private String alias1;
    private String alias2;

    private List<Double> values = new ArrayList<>();

    public Bookmaker getBookmaker() {
        return bookmaker;
    }

    public void setBookmaker(Bookmaker bookmaker) {
        this.bookmaker = bookmaker;
    }

    public void addValue(Double value) {
        this.values.add(value);
    }

    public List<Double> getValues() {
        return this.values;
    }

    public SportsMatch getSportsMatch() {
        return sportsMatch;
    }

    public void setSportsMatch(SportsMatch sportsMatch) {
        this.sportsMatch = sportsMatch;
    }

    public String getAlias1() {
        return alias1;
    }

    public void setAlias1(String alias1) {
        this.alias1 = alias1;
    }

    public String getAlias2() {
        return alias2;
    }

    public void setAlias2(String alias2) {
        this.alias2 = alias2;
    }
}
