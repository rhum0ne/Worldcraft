package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench;

import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Slot;
import lombok.Getter;
import lombok.Setter;

/**
 * Slot displaying a crafting result. Clicking it triggers a crafting action.
 */
@Setter
@Getter
public class ResultSlot extends Slot {

    private ItemStack result;
    private final Workbench workbench;

    public ResultSlot(int x, int y, int size, Workbench workbench) {
        super(x, y, size, workbench.nextSlotId(), workbench);
        this.workbench = workbench;
        workbench.registerSlot(this);
    }

    @Override
    public ItemStack getItem(){
        return this.result;
    }

    public void setItem(ItemStack result){
        this.result = result;
    }

    @Override
    public void onClick(Player player) {
        workbench.craft(player, this.getItem());
    }
}
