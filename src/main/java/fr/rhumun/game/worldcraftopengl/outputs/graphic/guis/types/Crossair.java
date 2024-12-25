package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types;

import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.Gui;

public class Crossair extends Gui {
    public Crossair() {
        super(-0.05f, 0.05f, 0.05f, -0.05f, Texture.CROSSHAIR);
    }
}
