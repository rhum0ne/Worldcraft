package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.content.items.Item;
import fr.rhumun.game.worldcraftopengl.content.items.ItemContainer;
import lombok.Getter;

@Getter
public class Inventory implements ItemContainer {

    /**
     * Player inventory slots. The first 9 slots represent the hotbar
     * shown at the bottom of the screen while the remaining slots
     * store the rest of the player's items.
     */
    Item[] items = new Item[36];
    Player player;

    public Inventory(Player player) {
        this.player = player;
    }

    public Item getItem(int slot){
        return items[slot];
    }

    public void setFreeSlot(Item item) {
        for(int i=0; i< items.length; i++){
            if(items[i] == null){
                items[i] = item;
                return;
            }
        }
    }

    @Override
    public void setItem(int slot, Item item) {
        this.items[slot] = item;
    }
}
