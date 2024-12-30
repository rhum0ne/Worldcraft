package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.items_containers;

import fr.rhumun.game.worldcraftopengl.Item;
import fr.rhumun.game.worldcraftopengl.ItemContainer;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CreativeSlot;

public class CreativeItems implements ItemContainer {

    private Item[] items = new Item[11*9];

    public CreativeItems(){
        int id=0;
        for(Material material : Material.values()){
            this.items[id] = new Item(material);
            id++;
        }
    }

    @Override
    public Item[] getItems() {
        return items;
    }

    @Override
    public void setItem(int Slot, Item item) {
        items[Slot] = item;
    }
}
