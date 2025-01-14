package fr.rhumun.game.worldcraftopengl.worlds.generators.utils;

import de.articdive.jnoise.pipeline.JNoise;

public class HeightCalculation {

    public static int calculateHeight(JNoise continentalness, JNoise erosion, JNoise peakAndValeys, double xH, double zH) {

        int baseHeight = calcContinentalness(continentalness, xH, zH);
        int erosionMalus = calcErosion(erosion, xH, zH);
        int peakAndValeysBonus = calcPeakAndValeys(peakAndValeys, xH, zH);

        float x = 0f;
        // Ajouter du détail local pour éviter l'uniformité
        int finalHeight = (int) (baseHeight + x*peakAndValeysBonus - erosionMalus);

        // Appliquer une hauteur minimale et maximale
        finalHeight = Math.max(0, Math.min(256, finalHeight));

        return finalHeight;
    }

    private static int calcPeakAndValeys(JNoise peakAndValeys, double xH, double zH) {
        float pavValue = (float) (peakAndValeys.evaluateNoise(xH * 5, zH * 5)+1)/2; // Bruit de plus haute fréquence

        int value;
        if(pavValue < 0.2) value = (int) (100*pavValue);
        else if(pavValue < 0.4) value = (int) (-(25)*Math.abs(pavValue-0.2f)+20);
        else if(pavValue < 0.6) value = (int) ((250/2)*(pavValue-0.4f)+15);
        else value = (int) (-(50/4) * (pavValue-0.6f) +40);


        return value;
    }

    private static int calcErosion(JNoise erosion, double xH, double zH) {
        float erosionValue = (float) (erosion.evaluateNoise(xH * 0.5, zH * 0.5)+1)/2; // Bruit de plus basse fréquence

        int value;
        if(erosionValue < 0.2) value = (int) (10*erosionValue);
        else if(erosionValue < 0.3) value = (int) ((160)*Math.abs(erosionValue-0.25f)+2);
        else if(erosionValue < 0.8) value = (int) ((60f/2.5)*(erosionValue-0.55f)+2);
        else value = (int) (-(110/2) * (erosionValue-0.8f) + 4);


        return value;
    }

    private static int calcContinentalness(JNoise continentalness, double xH, double zH) {
        float continentalnessValue = (float) continentalness.evaluateNoise(xH, zH);

        int baseHeight = (int) ( 800*(continentalnessValue) + 50);

        return baseHeight;
    }

}
