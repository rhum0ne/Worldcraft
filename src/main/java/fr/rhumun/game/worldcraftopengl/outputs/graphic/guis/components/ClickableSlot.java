package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components;

import fr.rhumun.game.worldcraftopengl.content.items.Item;
import fr.rhumun.game.worldcraftopengl.entities.Player;

public class ClickableSlot extends Slot {
    public ClickableSlot(int x, int y, int size, int id, Gui gui) {
        super(x, y, size, id, gui);
    }

    public ClickableSlot(int x, int y, int id, Gui gui){
        this(x, y, DEFAULT_SIZE, id, gui);
    }

    @Override
    public void onClick(Player player){
        Item clickedItem = this.getItem();
        this.setItem(getGuiModule().getSelectedItem());
        getGuiModule().setSelectedItem(clickedItem);
    }
}
