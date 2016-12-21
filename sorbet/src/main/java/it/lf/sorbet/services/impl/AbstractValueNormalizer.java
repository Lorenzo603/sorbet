package it.lf.sorbet.services.impl;


import it.lf.sorbet.services.ValueNormalizer;
import it.lf.sorbet.services.actions.NormalizationAction;

import java.util.List;

public abstract class AbstractValueNormalizer implements ValueNormalizer {


    @Override
    public String normalizeAlias(String original) {
        String partialResult = original;
        for (NormalizationAction action : getNormalizedActions()) {
            partialResult = action.execute(partialResult);
        }
        return partialResult;
    }

    protected List<NormalizationAction> getNormalizedActions() {
        return null;
    }
}
