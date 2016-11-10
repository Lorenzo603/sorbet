package it.lf.utils.builders;


import it.lf.sorbet.models.Bookmaker;

public class BookmakerBuilder {

    private String id;

    public Bookmaker build() {
        Bookmaker bookmaker = new Bookmaker();
        bookmaker.setId(id);
        return bookmaker;
    }

    public BookmakerBuilder withId(String id) {
        this.id = id;
        return this;
    }

}
