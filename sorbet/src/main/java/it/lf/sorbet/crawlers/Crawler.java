package it.lf.sorbet.crawlers;

import it.lf.sorbet.models.Quote;

import java.util.List;

public interface Crawler {

    String getBookmakerId();

    List<Quote> crawl();


}
