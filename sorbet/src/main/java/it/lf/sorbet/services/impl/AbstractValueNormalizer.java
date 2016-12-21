package it.lf.sorbet.services.impl;


import it.lf.sorbet.services.ValueNormalizer;
import it.lf.sorbet.services.actions.NormalizationAction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class AbstractValueNormalizer implements ValueNormalizer {

    protected List<NormalizationAction> normalizationActions;

    @Override
    public String normalizeAlias(String original) {

        String partialResult = original;
        for (NormalizationAction action : normalizationActions) {
            partialResult = action.execute(partialResult);
        }
        return partialResult;
    }
}
