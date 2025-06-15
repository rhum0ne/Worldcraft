package fr.rhumun.game.worldcraftopengl.content.items;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.types.ForcedModelMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Item {

    private final Material material;
    private final Model model;

    public Item(Material material) {
        this(material, (material.getMaterial() instanceof ForcedModelMaterial fmm) ? fmm.getModel() : Model.BLOCK);
    }

    public Item(Material material, Model model){
        this.material = material;
        this.model = model;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(material, item.material) &&
                Objects.equals(model, item.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, model);
    }

    @Override
    public String toString() {
        return "Item{" + "material=" + material + ", model=" + model + '}';
    }
}
