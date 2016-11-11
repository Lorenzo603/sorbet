package it.lf.sorbet.services.impl;


import org.junit.Test;

public class StringSimilarityServiceImplUnitTest {

    private StringSimilarityServiceImpl stringSimilarityService = new StringSimilarityServiceImpl();

    @Test
    public void testSimilarity() {

        System.out.println(stringSimilarityService.getSimilarityCoefficient("Milan", "Inter"));
        System.out.println(stringSimilarityService.getSimilarityCoefficient("Milan", "AC Milan"));
        System.out.println(stringSimilarityService.getSimilarityCoefficient("Inter", "FC Internazionale"));
        System.out.println(stringSimilarityService.getSimilarityCoefficient("Inter", "Inter FC"));

        System.out.println(stringSimilarityService.getSimilarityCoefficient("Italy", "Italy (U18)"));
        System.out.println(stringSimilarityService.getSimilarityCoefficient("Svezia U19", "Serbia U19"));

        System.out.println(stringSimilarityService.getSimilarityCoefficient("KS Gornik Zabrze U19", "Gornik Zabrze (U19)"));
        System.out.println(stringSimilarityService.getSimilarityCoefficient("WKS Slask Wroclaw U19", "Slask Wroclaw (U19)"));
        System.out.println(stringSimilarityService.getSimilarityCoefficient("KS Gornik Zabrze U19WKS Slask Wroclaw U19", "Gornik Zabrze (U19)Slask Wroclaw (U19)"));

        System.out.println(stringSimilarityService.getSimilarityCoefficient("Hapoel Nazareth IllitHapoel Akko", "Hapoel Nazareth EliteHapoel Akko"));

        System.out.println(stringSimilarityService.getSimilarityCoefficient("Hapoel Kfar ShalemASN Jerusalem", "Hapoel Petach TikvaHapoel Jerusalem"));

        System.out.println(stringSimilarityService.getSimilarityCoefficient("Yokohama FCKanazawa", "Yokohama FCNiigata"));

    }

}
