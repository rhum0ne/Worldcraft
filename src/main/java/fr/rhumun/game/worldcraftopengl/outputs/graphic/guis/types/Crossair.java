package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types;

import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.Gui;

import static fr.rhumun.game.worldcraftopengl.Game.GUI_ZOOM;

public class Crossair extends CenteredGUI {
    private static int WIDTH = 30;
    private static int HEIGHT = 30;
    public Crossair() {
        // Position centrale avec une taille fixe
        //super(960 - (GUI_ZOOM*WIDTH)/2, 540 - (GUI_ZOOM*HEIGHT)/2, 960 + (GUI_ZOOM*WIDTH)/2, 540 + (GUI_ZOOM*HEIGHT)/2, Texture.CROSSHAIR);
        super(WIDTH, HEIGHT, Texture.CROSSHAIR);
    }
}

