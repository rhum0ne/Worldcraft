package fr.rhumun.game.worldcraftopengl;

import lombok.Getter;

@Getter
public class Inventory {

    Item[] items = new Item[9];
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
}
