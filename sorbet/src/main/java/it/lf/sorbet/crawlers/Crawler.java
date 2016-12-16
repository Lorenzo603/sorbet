package it.lf.sorbet.crawlers;

import java.util.List;

public interface Crawler {

    String getBookmakerId();

    List crawl(String sport);


}
