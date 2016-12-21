package it.lf.sorbet.services.actions.impl;

import it.lf.sorbet.services.actions.NormalizationAction;
import org.springframework.stereotype.Service;

@Service
public class RemoveNationality implements NormalizationAction {
    @Override
    public String execute(String input) {
        if (input.contains("(")) {
            return input.substring(0, input.indexOf("(")).trim();
        }
        return input;
    }
}
