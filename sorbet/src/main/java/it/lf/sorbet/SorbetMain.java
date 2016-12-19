package it.lf.sorbet;

import it.lf.sorbet.runners.QuoteAnalyzer;
import it.lf.sorbet.runners.QuoteCrawler;
import it.lf.sorbet.runners.TeamMapGenerator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class SorbetMain {

    private static final Logger LOG = LogManager.getLogger(SorbetMain.class);

    public static void main( String[] args ) {
        ApplicationContext ctx = SpringApplication.run(SorbetMain.class, args);

        if (ArrayUtils.contains(args, "crawl")) {
            ((QuoteCrawler) ctx.getBean("quoteCrawler")).run(args[1]);
        } else if (ArrayUtils.contains(args, "analyze")) {
            ((QuoteAnalyzer) ctx.getBean("quoteAnalyzer")).run(args[1]);
        } else if (ArrayUtils.contains(args, "generateTeamMap")) {
            ((TeamMapGenerator) ctx.getBean("teamMapGenerator")).run();
        } else {
            LOG.error("Wrong starting parameter. Specify either 'crawl' or 'analyze'. When specifying crawl you have to also specify the sport, such as 'soccer'.");
        }

    }

}
