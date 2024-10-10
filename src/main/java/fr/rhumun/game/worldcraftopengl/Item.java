package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.blocks.materials.types.Material;
import lombok.Getter;

@Getter
public class Item {

    private Material material;

    public Item(Material material) {
        this.material = material;
    }
}
