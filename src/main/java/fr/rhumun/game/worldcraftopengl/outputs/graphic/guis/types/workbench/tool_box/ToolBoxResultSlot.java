package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench.tool_box;

import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Slot;

/**
 * Slot displaying a crafting result in the toolbox.
 * Clicking it crafts the selected item.
 */
public class ToolBoxResultSlot extends Slot {

    private ItemStack result;
    private final ToolBox toolBox;

    public ToolBoxResultSlot(int x, int y, int size, ToolBox toolBox) {
        super(x, y, size, toolBox.nextSlotId(), toolBox);
        this.toolBox = toolBox;
        toolBox.registerSlot(this);
    }

    @Override
    public ItemStack getItem() {
        return result;
    }

    public void setItem(ItemStack result) {
        this.result = result;
    }

    @Override
    public void onClick(Player player) {
        toolBox.craft(player, this.getItem());
    }
}
