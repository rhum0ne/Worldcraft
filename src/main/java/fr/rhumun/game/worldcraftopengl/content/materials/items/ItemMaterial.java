package fr.rhumun.game.worldcraftopengl.content.materials.items;

import fr.rhumun.game.worldcraftopengl.content.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;

/** Simple material used for non-placeable inventory items. */
public class ItemMaterial extends AbstractMaterial {
    public ItemMaterial(Texture texture) {
        super(texture);
    }
}
