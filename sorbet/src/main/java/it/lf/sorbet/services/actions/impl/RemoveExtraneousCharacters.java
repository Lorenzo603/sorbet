package it.lf.sorbet.services.actions.impl;


import it.lf.sorbet.services.actions.NormalizationAction;
import org.springframework.stereotype.Service;

@Service
public class RemoveExtraneousCharacters implements NormalizationAction {

    @Override
    public String execute(String input) {
        return input.replace(".", " ").replace(",", "");
    }
}
