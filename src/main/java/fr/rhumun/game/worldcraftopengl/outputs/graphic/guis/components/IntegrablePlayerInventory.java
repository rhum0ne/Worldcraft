package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;

public class IntegrablePlayerInventory{

    public static final int INVENTORY_WIDTH = 356;
    public static final int INVENTORY_HEIGHT = 166;
    private static final float ratio = 1f;

    public static void addInventoryComponentsTo(Gui inventory){
        for (int x = 0; x < 9; x++) {
            inventory.createClickableSlot(getInvX(x), getInvY(3) + 11,
                    (int) Math.ceil(ratio * Slot.DEFAULT_SIZE));
        }

        for (int y = 2; y >= 0; y--) {
            for (int x = 0; x < 9; x++) {
                inventory.createClickableSlot(getInvX(x), getInvY(y),
                        (int) Math.ceil(ratio * Slot.DEFAULT_SIZE));
            }
        }
        inventory.addText(0, -20, "Inventory");
    }



    private static int getInvX(int slot) {
        return 3 + (int) Math.ceil(slot * 40 * ratio);
    }

    private static int getInvY(int row) {
        return 3 + (int) Math.ceil(row * 40 * ratio);
    }

}
