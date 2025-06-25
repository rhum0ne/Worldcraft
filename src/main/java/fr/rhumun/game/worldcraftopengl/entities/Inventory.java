package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.content.items.ItemContainer;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import lombok.Getter;

@Getter
public class Inventory implements ItemContainer {

    /**
     * Player inventory slots. The first 9 slots represent the hotbar
     * shown at the bottom of the screen while the remaining slots
     * store the rest of the player's items.
     */
    ItemStack[] items = new ItemStack[36];
    Player player;

    public Inventory(Player player) {
        this.player = player;
    }

    public ItemStack getItem(int slot){
        return items[slot];
    }

    public void setFreeSlot(ItemStack item) {
        for(int i=0; i< items.length; i++){
            if(items[i] == null){
                items[i] = item;
                return;
            }
        }
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        this.items[slot] = item;
    }

    public void addItem(ItemStack result) {
        this.setFreeSlot(result);
    }

    public void removeItem(Material itemMaterial){ removeItem(itemMaterial, 1); }

    public void removeItem(Material itemMaterial, int amout) {
        for(int i=0; i< items.length; i++){
            if(items[i] != null && items[i].getMaterial() == itemMaterial){
                if(items[i].getQuantity() > amout){
                    items[i].setQuantity(items[i].getQuantity() - amout);
                    return;
                }
                else if(items[i].getQuantity() == amout){
                    items[i] = null;
                    return;
                }
            }
        }
    }

    public void clear() {
        for(int i=0; i< items.length; i++){
            items[i] = null;
        }
    }
}
