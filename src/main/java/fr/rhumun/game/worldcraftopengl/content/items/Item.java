package fr.rhumun.game.worldcraftopengl.content.items;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.types.ForcedModelMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import lombok.Getter;

@Getter
public class Item {

    private Material material;
    private Model model;

    public Item(Material material) {
        this(material, (material.getMaterial() instanceof ForcedModelMaterial fmm) ? fmm.getModel() : Model.BLOCK);
    }

    public Item(Material material, Model model){
        this.material = material;
        this.model = model;
    }
}
