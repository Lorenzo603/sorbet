package it.lf.sorbet.services.impl;

import it.lf.sorbet.services.actions.NormalizationAction;
import org.springframework.stereotype.Service;

@Service
public class InvertNameSurname implements NormalizationAction {

    @Override
    public String execute(String input) {
        String[] splitted = input.split(" ");
        if (splitted.length == 2) {
            return splitted[1] + " " + splitted[0];
        }
        return input;
    }

}
