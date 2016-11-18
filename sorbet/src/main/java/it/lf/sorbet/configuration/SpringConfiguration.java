package it.lf.sorbet.configuration;

import it.lf.sorbet.crawlers.Crawler;
import it.lf.sorbet.crawlers.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SpringConfiguration {

    @Autowired
    private BetFairCrawler betFairCrawler;
    @Autowired
    private BwinCrawler bwinCrawler;
    @Autowired
    private EuroBetCrawler euroBetCrawler;
    @Autowired
    private GazzaBetCrawler gazzaBetCrawler;
    @Autowired
    private SnaiCrawler snaiCrawler;
    @Autowired
    private WilliamHillCrawler williamHillCrawler;

    @Bean
    public List<Crawler> crawlers(){
        return Arrays.asList(
                //betFairCrawler
                //bwinCrawler
                //euroBetCrawler
                //snaiCrawler
                gazzaBetCrawler
        );
    }



}
