package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils;

public class BlockUtil {

    public static float[] verticesWithAttributes = new float[]{
            // Position       Normale        Texture (u, v)
            // Face avant
            0, 0, 0,          0, 0, -1,      0, 0,
            1, 0, 0,          0, 0, -1,      1, 0,
            1, 1, 0,          0, 0, -1,      1, 1,
            0, 1, 0,          0, 0, -1,      0, 1,

            // Face arrière
            0, 1, 1,          0, 0, 1,       0, 1,
            1, 1, 1,          0, 0, 1,       1, 1,
            1, 0, 1,          0, 0, 1,       1, 0,
            0, 0, 1,          0, 0, 1,       0, 0,

            // Face gauche
            0, 0, 0,         -1, 0, 0,       0, 0,
            0, 1, 0,         -1, 0, 0,       0, 1,
            0, 1, 1,         -1, 0, 0,       1, 1,
            0, 0, 1,         -1, 0, 0,       1, 0,

            // Face droite
            1, 0, 0,          1, 0, 0,       0, 0,
            1, 1, 0,          1, 0, 0,       0, 1,
            1, 1, 1,          1, 0, 0,       1, 1,
            1, 0, 1,          1, 0, 0,       1, 0,

            // Face supérieure
            0, 1, 0,          0, 1, 0,       0, 0,
            1, 1, 0,          0, 1, 0,       1, 0,
            1, 1, 1,          0, 1, 0,       1, 1,
            0, 1, 1,          0, 1, 0,       0, 1,

            // Face inférieure
            0, 0, 0,          0, -1, 0,      0, 0,
            1, 0, 0,          0, -1, 0,      1, 0,
            1, 0, 1,          0, -1, 0,      1, 1,
            0, 0, 1,          0, -1, 0,      0, 1
    };


    public static int[] indices = new int[]{
            // Face avant
            0, 2, 1,
            0, 3, 2,

            // Face arrière
            4, 6, 5,
            4, 7, 6,

            // Face gauche
            8, 10, 9,
            8, 11, 10,

            // Face droite
            12, 13, 14,
            12, 14, 15,

            // Face supérieure
            16, 18, 17,
            16, 19, 18,

            // Face inférieure
            20, 21, 22,
            20, 22, 23
    };

}
