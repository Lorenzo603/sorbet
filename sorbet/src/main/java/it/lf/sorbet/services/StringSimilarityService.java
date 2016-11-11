package it.lf.sorbet.services;

public interface StringSimilarityService {

    double getSimilarityCoefficient(String s1, String s2);
    boolean areSimilar(String s1, String s2);

}
