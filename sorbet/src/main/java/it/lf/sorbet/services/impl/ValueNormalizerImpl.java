package it.lf.sorbet.services.impl;


import it.lf.sorbet.services.ValueNormalizer;
import org.springframework.stereotype.Service;

@Service
public class ValueNormalizerImpl implements ValueNormalizer {

    @Override
    public String normalizeAlias(String original) {
        String trimmed = original.trim();

        // remove extraneous characters
        trimmed = trimmed.replace(".", " ");

        // remove nationality
        if (trimmed.contains("(")) {
            trimmed = trimmed.substring(0, trimmed.indexOf("(")).trim();
        }

        // take only first letter of first name
        if (!trimmed.contains("/")) {
            String[] separateNames = trimmed.split(" ");
            if (separateNames.length > 1) {
                String normFirstName = separateNames[0].charAt(0) + " " + separateNames[1];
                return normFirstName;
            }
        }

        return trimmed;
    }
}
