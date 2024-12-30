package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis;

import fr.rhumun.game.worldcraftopengl.Item;
import fr.rhumun.game.worldcraftopengl.Player;

public class CreativeSlot extends Slot{
    public CreativeSlot(int x, int y, int size, int id, Gui gui) {
        super(x, y, size, id, gui);
    }


    @Override
    public void onClick(Player player){
        getGuiModule().setSelectedItem(this.getItem());
    }
}
