package it.lf.sorbet.crawlers.impl;


import it.lf.sorbet.crawlers.Crawler;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;

public abstract class AbstractCrawler implements Crawler {

    private Logger LOG = LogManager.getLogger(AbstractCrawler.class);

    private Configuration configuration;

    protected Configuration getCrawlerConfig()  {
        String bookmakerId = getBookmakerId();
        Configurations configs = new Configurations();
        try {
            if (configuration == null) {
                this.configuration = configs.properties(new File("resources/config/crawlers/" + bookmakerId + ".properties"));
            }
            return configuration;
        } catch (ConfigurationException ce) {
            LOG.error("Error loading Configuration properties for crawler of Bookmaker: " + bookmakerId, ce);
        }
        return null;
    }

    protected String parseOdd(String odd) {
        if (StringUtils.isNotBlank(odd)) {
            return  odd.replace(',', '.');
        }
        return StringUtils.EMPTY;
    }

}
