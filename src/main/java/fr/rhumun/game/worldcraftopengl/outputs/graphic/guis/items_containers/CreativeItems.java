package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.items_containers;

import fr.rhumun.game.worldcraftopengl.Item;
import fr.rhumun.game.worldcraftopengl.ItemContainer;
import fr.rhumun.game.worldcraftopengl.blocks.Model;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.ForcedModelMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.Material;
import lombok.Getter;

@Getter
public class CreativeItems implements ItemContainer {

    private Item[] items = new Item[11*9];
    private Model showedModel = null;

    public CreativeItems(){
        reset();
    }

    public void setModel(Model model){
        if(model==null) {
            reset();
            return;
        }

        int id=0;
        for(Material material : Material.values()){
            if(material.getMaterial() instanceof ForcedModelMaterial f && f.getModel() != model) continue;
            this.items[id] = new Item(material, model);
            id++;
        }
        showedModel = model;
    }

    public void reset(){
        int id=0;
        for(Material material : Material.values()){
            this.items[id] = new Item(material);
            id++;
        }
        showedModel = null;
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
