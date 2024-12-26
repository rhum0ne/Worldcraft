package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types;

import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.Gui;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class HotBarGui extends Gui {
    public HotBarGui() {
        //super(-0.5f,  -0.45f, 0.5f, -0.57f, Texture.HOTBAR);
        super(-0.5f,  -0.82f, 0.5f, -1f, Texture.HOTBAR);
        this.setItemContainer(GAME.getPlayer().getInventory());

        for(int i=0; i<9; i++)
            this.createSlot(i*0.12f+0.05f, -0.12f);
    }
}
