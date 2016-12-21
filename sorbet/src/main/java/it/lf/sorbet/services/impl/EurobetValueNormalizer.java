package it.lf.sorbet.services.impl;

import it.lf.sorbet.services.actions.NormalizationAction;
import it.lf.sorbet.services.actions.impl.FilterFirstName;
import it.lf.sorbet.services.actions.impl.RemoveExtraneousCharacters;
import it.lf.sorbet.services.actions.impl.RemoveNationality;
import it.lf.sorbet.services.actions.impl.Trimming;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EurobetValueNormalizer extends AbstractValueNormalizer {

    @Autowired
    private Trimming trimming;
    @Autowired
    private RemoveExtraneousCharacters removeExtraneousCharacters;
    @Autowired
    private RemoveNationality removeNationality;
    @Autowired
    private FilterFirstName filterFirstName;

    @Bean
    protected List<NormalizationAction> normalizationActions() {
        List<NormalizationAction> aList = new ArrayList<>();
        aList.add(trimming);
        aList.add(removeExtraneousCharacters);
        aList.add(removeNationality);
        aList.add(filterFirstName);
        this.normalizationActions = aList;
        return aList;
    }

}
