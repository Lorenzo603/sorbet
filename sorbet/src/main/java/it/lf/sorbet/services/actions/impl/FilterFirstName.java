package it.lf.sorbet.services.actions.impl;

import it.lf.sorbet.services.actions.NormalizationAction;
import org.springframework.stereotype.Service;

@Service
public class FilterFirstName implements NormalizationAction {
    @Override
    public String execute(String input) {
        // take only first letter of first name
        if (!input.contains("/")) {
            String[] separateNames = input.split(" ");
            if (separateNames.length > 1) {
                String normFirstName = separateNames[0].charAt(0) + " " + separateNames[1];
                return normFirstName;
            }
        }
        return input;
    }
}
