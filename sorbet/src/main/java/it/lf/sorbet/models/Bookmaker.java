package it.lf.sorbet.models;


import it.lf.sorbet.crawlers.Crawler;

public class Bookmaker {

    private String id;
    private Crawler crawler;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Crawler getCrawler() {
        return crawler;
    }

    public void setCrawler(Crawler crawler) {
        this.crawler = crawler;
    }
}
