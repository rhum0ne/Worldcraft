package fr.rhumun.game.worldcraftopengl.graphic;

import fr.rhumun.game.worldcraftopengl.Vector;

public enum Models {

    BLOCK(new Model());

    Model model;
    Models(Model model){
        this.model = model;
    }

    public static void init(){
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
    }


    public Model get(){ return model; }
}
