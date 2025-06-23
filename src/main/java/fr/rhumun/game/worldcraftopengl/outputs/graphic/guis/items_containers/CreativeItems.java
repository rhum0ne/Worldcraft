package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.items_containers;

import fr.rhumun.game.worldcraftopengl.content.GuiTypes;
import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.content.items.ItemContainer;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.types.ForcedModelMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Materials;
import lombok.Getter;

@Getter
public class CreativeItems implements ItemContainer {

    private ItemStack[] items = new ItemStack[11*9];
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
        for(Material material : Materials.Registry){
            if(material instanceof ForcedModelMaterial f && f.getModel() != model && material.showInCreativeInventory()) continue;
            this.items[id] = new ItemStack(material, model);
            id++;
        }
        setEmpty(id);
        showedModel = model;
    }

    public void reset(){
        int id=0;
        for(Material material : Materials.Registry){
            if(!material.showInCreativeInventory()) continue;
            if(id>=11*9) break;
            this.items[id] = new ItemStack(material);
            id++;
        }
        setEmpty(id);
        showedModel = null;
    }

    @Override
    public ItemStack[] getItems() {
        return items;
    }

    @Override
    public void setItem(int Slot, ItemStack item) {
        items[Slot] = item;
    }

    public void setEmpty(){ setEmpty(0); }
    public void setEmpty(int start){
        for(; start< items.length; start++){
            items[start] = null;
        }
    }

    public void setType(GuiTypes type){
        int id=0;
        Model model = showedModel != null ? showedModel : Model.BLOCK;
        for(Material material : type.getMaterials()){
            if(id>=11*9) break;
            this.items[id] = new ItemStack( material, model );
            id++;
        }
        setEmpty(id);
    }
}
