package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.creative_inventory;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Slot;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.items_containers.CreativeItems;
import lombok.Getter;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.Game.GUI_ZOOM;
import static fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.IntegrablePlayerInventory.*;

/**
 * Combined inventory GUI used in creative mode.
 * It displays the creative give menu next to the full player inventory.
 */
@Getter
public class CreativePlayerInventoryGui extends CenteredGUI {

    public static final float ratio = 1f;
    public static final int CREATIVE_WIDTH = 528;
    private static final int GAP = 10;

    private final CreativeGui creative;

    public CreativePlayerInventoryGui() {
        super(CREATIVE_WIDTH + GAP + INVENTORY_WIDTH, CREATIVE_WIDTH, null);

        // Creative menu on the left
        creative = new CreativeGui(this);
        this.addComponent(creative);

        // Player inventory on the right
        int invX = (CREATIVE_WIDTH + GAP) * GUI_ZOOM;
        int invY = CREATIVE_WIDTH * GUI_ZOOM / 2;
        Gui inventory = new Gui(invX, invY, INVENTORY_WIDTH, INVENTORY_HEIGHT, Texture.INVENTORY, this);
        inventory.setItemContainer(GAME.getPlayer().getInventory());

        addInventoryComponentsTo( inventory);

        this.addComponent(inventory);
    }
}
