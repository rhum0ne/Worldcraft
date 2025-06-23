package fr.rhumun.game.worldcraftopengl.content.items;

import fr.rhumun.game.worldcraftopengl.content.materials.items.ConsumableItem;

public interface ItemContainer {

    ItemStack[] getItems();
    void setItem(int Slot, ItemStack item);
}
