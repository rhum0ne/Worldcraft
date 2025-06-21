package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.player_inventory;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Slot;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.InventoryGUI;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

/**
 * Graphical interface showing the player's inventory.
 * It exposes three rows of storage slots above the existing hotbar.
 */
public class PlayerInventoryGui extends CenteredGUI {

    private static final float ratio = 1f;

    public PlayerInventoryGui() {
        super(356, 166, Texture.INVENTORY);

        this.setItemContainer(GAME.getPlayer().getInventory());

        // Upper inventory (27 slots)
        for (int y = 3; y >= 0; y--) {
            for (int x = 0; x < 9; x++) {
                this.createClickableSlot(getXForSlot(x), getYForSlot(y),
                        (int) Math.ceil(ratio * Slot.DEFAULT_SIZE));
            }
        }

        this.addText(0, -20, "Inventory");
    }

    private int getXForSlot(int slot) {
        return 3 + (int) Math.ceil(slot * 40 * ratio);
    }

    private int getYForSlot(int row) {
        return 3 + (int) Math.ceil(row * 40 * ratio);
    }
}
