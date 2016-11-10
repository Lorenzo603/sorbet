package it.lf.sorbet;

import it.lf.sorbet.runners.QuoteAnalyzer;
import it.lf.sorbet.runners.QuoteCrawler;
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
        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }

        if (ArrayUtils.contains(args, "crawl")) {
            ((QuoteCrawler) ctx.getBean("quoteCrawler")).run();
        } else if (ArrayUtils.contains(args, "analyze")) {
            ((QuoteAnalyzer) ctx.getBean("quoteAnalyzer")).run();
        } else {
            LOG.error("Wrong starting parameter. Specify either 'crawl' or 'analyze'");
        }
    }

}
