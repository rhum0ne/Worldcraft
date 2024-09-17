package fr.rhumun.game.worldcraftopengl.graphic;

import fr.rhumun.game.worldcraftopengl.Vector;

public enum Models {

    BLOCK(new Model()),
    HEXAGON(new Model());

    Model model;
    Models(Model model){
        this.model = model;
    }

    public static void init(){
        initHexagonModel();

        // Face supérieure
        BLOCK.model.addQuad(
                0, 0, 0,
                1, 0, 0,
                1, 0, 1,
                0, 0, 1,
                new Vector(0, 1, 0),
                0, 0,   // UV Point 1
                1, 0,   // UV Point 2
                1, 1,   // UV Point 3
                0, 1   // UV Point 4
        );
        // Face inférieure
        BLOCK.model.addQuad(
                0, -1, 0,
                1, -1, 0,
                1, -1, 1,
                0, -1, 1,
                new Vector(0, -1, 0),
                0, 0,   // UV Point 1
                1, 0,   // UV Point 2
                1, 1,   // UV Point 3
                0, 1   // UV Point 4
        );
        // Face avant
        BLOCK.model.addQuad(
                0, -1, 0, // Point 1 (bas-gauche)
                1, -1, 0, // Point 2 (bas-droit)
                1, 0, 0, // Point 3 (haut-droit)
                0, 0, 0, // Point 4 (haut-gauche)
                new Vector(0, 0, -1), // Normale
                0, 0,    // UV Point 1
                1, 0,    // UV Point 2
                1, 1,    // UV Point 3
                0, 1    // UV Point 4
        );
        // Face droite
        BLOCK.model.addQuad(
                1, 0, 0,
                1, 0, 1,
                1, -1, 1,
                1, -1, 0,
                new Vector(1, 0, 0),
                0, 0,   // UV Point 1
                1, 0,   // UV Point 2
                1, 1,   // UV Point 3
                0, 1   // UV Point 4
        );
        // Face arrière
        BLOCK.model.addQuad(
                0, 0, 1,
                1, 0, 1,
                1, -1, 1,
                0, -1, 1,
                new Vector(0, 0, 1),
                0, 0,   // UV Point 1
                1, 0,   // UV Point 2
                1, 1,   // UV Point 3
                0, 1   // UV Point 4
        );
        // Face gauche
        BLOCK.model.addQuad(
                0, 0, 0,
                0, 0, 1,
                0, -1, 1,
                0, -1, 0,
                new Vector(-1, 0, 0),
                0, 0,   // UV Point 1
                1, 0,   // UV Point 2
                1, 1,   // UV Point 3
                0, 1   // UV Point 4
        );
    }

    private static void initHexagonModel() {
        float startX = 0.0f;
        float startY = 0.0f;
        float startZ = 0.0f;
        float endX = 1.0f;
        float endY = -1.0f;
        float endZ = 1.0f;

        float radius = 0.5f; // Rayon de l'hexagone
        float halfHeight = 0.5f; // Hauteur de l'hexagone

        Vector normalTop = new Vector(0, 1, 0);   // Normale vers le haut
        Vector normalBottom = new Vector(0, -1, 0); // Normale vers le bas

        // Calculer les facteurs de mise à l'échelle et le centre
        float scaleX = endX - startX;
        float scaleY = endY - startY;
        float scaleZ = endZ - startZ;
        float centerX = (endX + startX) / 2;
        float centerY = (endY + startY) / 2;
        float centerZ = (endZ + startZ) / 2;

        // Ajouter les faces latérales (pour chaque côté de l'hexagone)
        for (int i = 0; i < 6; i++) {
            float angle1 = (float) (i * Math.PI / 3); // Calculer l'angle pour le sommet 1
            float angle2 = (float) ((i + 1) * Math.PI / 3); // Calculer l'angle pour le sommet 2

            float x1 = (float) Math.cos(angle1) * radius + centerX;
            float z1 = (float) Math.sin(angle1) * radius + centerZ;
            float x2 = (float) Math.cos(angle2) * radius + centerX;
            float z2 = (float) Math.sin(angle2) * radius + centerZ;

            // Ajouter la face latérale (deux triangles formant un quadrilatère)
            HEXAGON.model.addQuad(
                    x1, halfHeight + centerY, z1,  // Point 1 en haut
                    x2, halfHeight + centerY, z2,  // Point 2 en haut
                    x2, -halfHeight + centerY, z2, // Point 3 en bas
                    x1, -halfHeight + centerY, z1, // Point 4 en bas
                    new Vector(0, 0, 0), // Normale (la normale peut varier en fonction de la face)
                    0, 0,  // UV Point 1
                    1, 0,  // UV Point 2
                    1, 1,  // UV Point 3
                    0, 1   // UV Point 4
            );
        }

        // Ajouter la face supérieure (un fan de triangles)
        for (int i = 0; i < 6; i++) {
            float angle1 = (float) (i * Math.PI / 3);
            float angle2 = (float) ((i + 1) * Math.PI / 3);

            float x1 = (float) Math.cos(angle1) * radius + centerX;
            float z1 = (float) Math.sin(angle1) * radius + centerZ;
            float x2 = (float) Math.cos(angle2) * radius + centerX;
            float z2 = (float) Math.sin(angle2) * radius + centerZ;

            HEXAGON.model.addQuad(
                    centerX, halfHeight + centerY, centerZ, // Centre du haut
                    x1, halfHeight + centerY, z1,  // Sommet 1
                    x2, halfHeight + centerY, z2,  // Sommet 2
                    centerX, halfHeight + centerY, centerZ,  // Répétition pour le quatrième coin (Triangle, donc répétition correcte)
                    normalTop,  // Normale vers le haut
                    0.5f, 0.5f,  // UV central
                    0, 0,  // UV sommet 1
                    1, 0,  // UV sommet 2
                    0, 1   // Répétition UV pour le quatrième coin
            );
        }

        // Ajouter la face inférieure (similaire à la supérieure)
        for (int i = 0; i < 6; i++) {
            float angle1 = (float) (i * Math.PI / 3);
            float angle2 = (float) ((i + 1) * Math.PI / 3);

            float x1 = (float) Math.cos(angle1) * radius + centerX;
            float z1 = (float) Math.sin(angle1) * radius + centerZ;
            float x2 = (float) Math.cos(angle2) * radius + centerX;
            float z2 = (float) Math.sin(angle2) * radius + centerZ;

            HEXAGON.model.addQuad(
                    centerX, -halfHeight + centerY, centerZ, // Centre du bas
                    x2, -halfHeight + centerY, z2,  // Sommet 2
                    x1, -halfHeight + centerY, z1,  // Sommet 1
                    centerX, -halfHeight + centerY, centerZ,  // Répétition pour le quatrième coin (Triangle, donc répétition correcte)
                    normalBottom,  // Normale vers le bas
                    0.5f, 0.5f,  // UV central
                    1, 0,  // UV sommet 2
                    0, 0,  // UV sommet 1
                    1, 1   // Répétition UV pour le quatrième coin
            );
        }
    }





    public Model get(){ return model; }
}
