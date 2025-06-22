package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components;

import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;

public class ClickableSlot extends Slot {
    public ClickableSlot(int x, int y, int size, int id, Gui gui) {
        super(x, y, size, id, gui);
    }

    public ClickableSlot(int x, int y, int id, Gui gui){
        this(x, y, DEFAULT_SIZE, id, gui);
    }

    @Override
    public void onClick(Player player){
        ItemStack clickedItem = this.getItem();
        ItemStack selectedItem = getGuiModule().getSelectedItem();

        if(selectedItem == null || clickedItem == null || !clickedItem.isSame(selectedItem)){
            this.setItem(selectedItem);
            getGuiModule().setSelectedItem(clickedItem);
            return;
        }

        if(clickedItem.isSame(selectedItem)){
            int canAdd = clickedItem.getMaxStackSize() - clickedItem.getQuantity();
            if(canAdd <= 0) return;

            int toAdd = Math.min(canAdd, selectedItem.getQuantity());
            int rest = selectedItem.getQuantity() - toAdd;

            clickedItem.add(toAdd);
            if(rest == 0) getGuiModule().setSelectedItem(null);
            else selectedItem.setQuantity(rest);
        }
    }
}
