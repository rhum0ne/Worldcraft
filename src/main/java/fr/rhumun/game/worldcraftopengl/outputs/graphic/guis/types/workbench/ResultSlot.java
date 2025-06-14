package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench;

import fr.rhumun.game.worldcraftopengl.content.items.Item;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Slot;

/**
 * Slot displaying a crafting result. Clicking it triggers a crafting action.
 */
public class ResultSlot extends Slot {

    private final Item result;
    private final int amount;
    private final Workbench workbench;

    public ResultSlot(int x, int y, int size, Item result, int amount, Workbench workbench) {
        super(x, y, size, workbench.nextSlotId(), workbench);
        this.result = result;
        this.amount = amount;
        this.workbench = workbench;
        workbench.registerSlot(this);
    }

    @Override
    public Item getItem() {
        return result;
    }

    @Override
    public void setItem(Item item) {
        // result slots are read-only
    }

    @Override
    public void onClick(Player player) {
        workbench.craft(player, result, amount);
    }
}
