package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.player_inventory;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Slot;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.InventoryGUI;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.IntegrablePlayerInventory.addInventoryComponentsTo;

/**
 * Graphical interface showing the player's inventory.
 * It exposes three rows of storage slots above the existing hotbar.
 */
public class PlayerInventoryGui extends CenteredGUI {

    private static final float ratio = 1f;

    public PlayerInventoryGui() {
        super(376, 200, Texture.PLAYER_INVENTORY);

        this.setItemContainer(GAME.getPlayer().getInventory());

        addInventoryComponentsTo(this,10,23);
    }

    private int getXForSlot(int slot) {
        return 3 + (int) Math.ceil(slot * 40 * ratio);
    }

    private int getYForSlot(int row) {
        return 3 + (int) Math.ceil(row * 40 * ratio);
    }
}
