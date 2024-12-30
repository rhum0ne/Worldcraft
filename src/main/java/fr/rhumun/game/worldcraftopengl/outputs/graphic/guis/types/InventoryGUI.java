package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types;

import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.Slot;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.Game.GUI_ZOOM;

public class InventoryGUI extends Gui {

    private final float ratio;
    public InventoryGUI(int x, int y) {
        this(x, y, 1f, null);
    }

    public InventoryGUI(int x, int y, float ratio, Gui container) {
        super(x, y, (int) Math.ceil(ratio*356), (int) Math.ceil(ratio*36), Texture.INVENTORY, container);

        this.setItemContainer(GAME.getPlayer().getInventory());
        this.ratio = ratio;

        //LE GUI ZOOM*11 est hasardeux et ne fonctionne que pour une taille de GUI de 2
        for(int i=0; i<9; i++)
            this.createClickableSlot(getXForSlot(i), 3, (int) Math.ceil(ratio* Slot.DEFAULT_SIZE));
    }

    private int getXForSlot(int slot){
        return 3 + (int) Math.ceil(slot * 40 * ratio);
    }

}
