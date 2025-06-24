package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;

public class IntegrablePlayerInventory{

    public static final int INVENTORY_WIDTH = 356;
    public static final int INVENTORY_HEIGHT = 166;
    private static final float ratio = 1f;

    public static void addInventoryComponentsTo(Gui inventory) {
        addInventoryComponentsTo(inventory, 0, 0);
    }

    public static void addInventoryComponentsTo(Gui inventory, int offsetX, int offsetY){
        for (int x = 0; x < 9; x++) {
            inventory.createClickableSlot(getInvX(x) + offsetX, getInvY(3) + 11 + offsetY,
                    (int) Math.ceil(ratio * Slot.DEFAULT_SIZE));
        }

        for (int y = 2; y >= 0; y--) {
            for (int x = 0; x < 9; x++) {
                inventory.createClickableSlot(getInvX(x) + offsetX, getInvY(y) + offsetY,
                        (int) Math.ceil(ratio * Slot.DEFAULT_SIZE));
            }
        }
        inventory.addText(offsetX, -20 + offsetY, "Inventory");
    }



    private static int getInvX(int slot) {
        return 3 + (int) Math.ceil(slot * 40 * ratio);
    }

    private static int getInvY(int row) {
        return 5 + (int) Math.ceil(row * 39 * ratio);
    }

}
