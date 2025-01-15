package fr.rhumun.game.worldcraftopengl.worlds.generators.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

@Setter @Getter
public class Graph {

    private final double[] abscisses;
    private final int[] result;


    public Graph(double[] abscisses, int[] result) {
        this.abscisses = abscisses;
        this.result = result;
    }

    public int getResult(double y){
        try {
            int i = 0;
            while (i < abscisses.length - 1 && y > abscisses[i + 1]) {
                i++;
            }

            // Calculer les coefficients de la droite (y = ax + b)
            double xA = abscisses[i];
            double xB = abscisses[i + 1];
            int yA = result[i];
            int yB = result[i + 1];

            double a = (yB - yA) / (xB - xA); // Pente
            double b = yA - a * xA;          // Ordonnée à l'origine

            // Calculer la valeur interpolée
            int interpolatedResult = (int) (a * y + b);

            // Debug (optionnel)
//        System.out.println("Segment: [" + xA + ", " + xB + "]");
//        System.out.println("Équation: " + a + "x + " + b + " = " + interpolatedResult);

            return interpolatedResult;
        } catch (Exception e) {
            GAME.errorLog("Error during calculation of Graph : " + Arrays.toString(result));
            throw new RuntimeException(e);
        }
    }
}
