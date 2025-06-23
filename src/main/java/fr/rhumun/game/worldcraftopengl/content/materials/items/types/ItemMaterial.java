package fr.rhumun.game.worldcraftopengl.content.materials.items.types;

import fr.rhumun.game.worldcraftopengl.content.GuiTypes;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;

/** Simple material used for non-placeable inventory items. */
public class ItemMaterial extends Material {
    public ItemMaterial(Texture texture) {
        super(texture);

        this.addToType(GuiTypes.ITEMS);
    }
}
