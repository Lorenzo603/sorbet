package it.lf.sorbet.services.impl;

import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.Levenshtein;
import it.lf.sorbet.services.StringSimilarityService;
import org.springframework.stereotype.Service;

@Service
public class StringSimilarityServiceImpl implements StringSimilarityService {

    Levenshtein levenshtein = new Levenshtein();
    JaroWinkler jaroWinkler = new JaroWinkler();

    @Override
    public double getSimilarityCoefficient(String s1, String s2) {
        // return levenshtein.distance(s1, s2);
        return jaroWinkler.similarity(s1, s2);
    }

    @Override
    public boolean areSimilar(String s1, String s2) {
        return getSimilarityCoefficient(s1, s2) > 0.74;
    }
}
