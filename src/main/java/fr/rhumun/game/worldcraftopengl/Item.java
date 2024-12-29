package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.blocks.Model;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.ForcedModelMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.Material;
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
