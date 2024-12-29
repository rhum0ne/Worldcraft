package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types;

import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;

import static fr.rhumun.game.worldcraftopengl.Game.GUI_ZOOM;

public class CreativeInventoryGui extends CenteredGUI {

    private final InventoryGUI inventory;

    private final int width = 528;
    private final int height = 528;
    private final float ratio = (float) 450 /528;
    public CreativeInventoryGui() {
        super(450, 450, Texture.CREATIVE_INVENTORY);

        this.inventory = this.addInventory((int) (74*ratio), (int) (478*ratio), ratio);
    }
}
