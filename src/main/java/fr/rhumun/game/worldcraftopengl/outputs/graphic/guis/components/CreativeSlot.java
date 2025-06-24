package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;

public class CreativeSlot extends Slot {
    public CreativeSlot(int x, int y, int size, int id, Gui gui) {
        super(x, y, size, id, gui);
    }


    @Override
    public void onClick(Player player){
        if(this.getItem() == null) getGuiModule().setSelectedItem(null);

        if(getGuiModule().getSelectedItem() != null){
            if(getGuiModule().getSelectedItem().isSame(this.getItem()))
                getGuiModule().getSelectedItem().addQuantity(1);
            else getGuiModule().setSelectedItem(null);

            return;
        }

        getGuiModule().setSelectedItem(this.getItem().copy());
        getGuiModule().getSelectedItem().setQuantity(1);
    }
}
