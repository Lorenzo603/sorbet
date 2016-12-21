package it.lf.sorbet.models;

import java.util.ArrayList;
import java.util.List;

public class Quote {

    private Bookmaker bookmaker;
    private SportsMatch sportsMatch;
    private String alias1;
    private String alias2;
    private String normalizedAlias1;
    private String normalizedAlias2;

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

    public String getNormalizedAlias1() {
        return normalizedAlias1;
    }

    public void setNormalizedAlias1(String normalizedAlias1) {
        this.normalizedAlias1 = normalizedAlias1;
    }

    public String getNormalizedAlias2() {
        return normalizedAlias2;
    }

    public void setNormalizedAlias2(String normalizedAlias2) {
        this.normalizedAlias2 = normalizedAlias2;
    }
}
