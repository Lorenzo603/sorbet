package it.lf.sorbet.services.impl;

import it.lf.sorbet.services.actions.NormalizationAction;
import it.lf.sorbet.services.actions.impl.FilterFirstName;
import it.lf.sorbet.services.actions.impl.RemoveExtraneousCharacters;
import it.lf.sorbet.services.actions.impl.RemoveNationality;
import it.lf.sorbet.services.actions.impl.Trimming;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BetFairValueNormalizer extends AbstractValueNormalizer {

    @Autowired
    private Trimming trimming;
    @Autowired
    private RemoveExtraneousCharacters removeExtraneousCharacters;
    @Autowired
    private RemoveNationality removeNationality;
    @Autowired
    private FilterFirstName filterFirstName;

    @Bean
    @Override
    protected List<NormalizationAction> getNormalizedActions() {
        List<NormalizationAction> aList = new ArrayList<>();
        aList.add(trimming);
        aList.add(removeExtraneousCharacters);
        aList.add(removeNationality);
        aList.add(filterFirstName);
        return aList;
    }

}
