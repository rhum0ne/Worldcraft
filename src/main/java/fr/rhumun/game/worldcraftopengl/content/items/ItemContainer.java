package fr.rhumun.game.worldcraftopengl.content.items;

public interface ItemContainer {

    ItemStack[] getItems();
    void setItem(int Slot, ItemStack item);
}
