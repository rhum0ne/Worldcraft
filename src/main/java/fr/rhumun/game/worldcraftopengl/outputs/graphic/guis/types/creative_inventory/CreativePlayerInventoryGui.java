package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.creative_inventory;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Slot;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.items_containers.CreativeItems;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.Game.GUI_ZOOM;

/**
 * Combined inventory GUI used in creative mode.
 * It displays the creative give menu next to the full player inventory.
 */
public class CreativePlayerInventoryGui extends CenteredGUI {

    private static final float ratio = 1f;
    private static final int CREATIVE_WIDTH = 528;
    private static final int INVENTORY_WIDTH = 356;
    private static final int INVENTORY_HEIGHT = 166;
    private static final int GAP = 20;

    public CreativePlayerInventoryGui() {
        super(CREATIVE_WIDTH + GAP + INVENTORY_WIDTH, CREATIVE_WIDTH, null);

        // Creative menu on the left
        Gui creative = new Gui(0, 0, CREATIVE_WIDTH, CREATIVE_WIDTH, Texture.CREATIVE_INVENTORY, this);
        creative.setItemContainer(new CreativeItems());

        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 9; x++) {
                int id = 9 * y + x;
                creative.createCreativeSlot(getCreativeX(id), getCreativeY(id),
                        (int) Math.ceil(ratio * Slot.DEFAULT_SIZE));
            }
        }
        creative.addText(77, 10, "Blocks");
        creative.addButton(new SlabButton(0, 10, creative));
        this.addComponent(creative);

        // Player inventory on the right
        int invX = (CREATIVE_WIDTH + GAP) * GUI_ZOOM;
        int invY = (CREATIVE_WIDTH - INVENTORY_HEIGHT) * GUI_ZOOM;
        Gui inventory = new Gui(invX, invY, INVENTORY_WIDTH, INVENTORY_HEIGHT, Texture.INVENTORY, this);
        inventory.setItemContainer(GAME.getPlayer().getInventory());

        for (int y = 3; y >= 0; y--) {
            for (int x = 0; x < 9; x++) {
                inventory.createClickableSlot(getInvX(x), getInvY(y),
                        (int) Math.ceil(ratio * Slot.DEFAULT_SIZE));
            }
        }
        inventory.addText(0, -20, "Inventory");
        this.addComponent(inventory);
    }

    private int getCreativeX(int slot) {
        return (int) (77 * ratio + Math.ceil(slot % 9 * 40 * ratio));
    }

    private int getCreativeY(int slot) {
        return (int) (34 * ratio + Math.ceil(slot / 9 * 39 * ratio));
    }

    private int getInvX(int slot) {
        return 3 + (int) Math.ceil(slot * 40 * ratio);
    }

    private int getInvY(int row) {
        return 3 + (int) Math.ceil(row * 40 * ratio);
    }
}
