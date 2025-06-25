package fr.rhumun.game.worldcraftopengl.content.materials.items.types;

import fr.rhumun.game.worldcraftopengl.content.GuiTypes;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import lombok.Getter;

@Getter
public class BlockItemMaterial extends ItemMaterial {

    private Material materialToPlace;

    public BlockItemMaterial(Texture texture, Material materialToPlace) {
        super(texture);
        this.materialToPlace = materialToPlace;
        this.addToType(GuiTypes.FUNCTIONAL_BLOCKS);
    }

    public Sound getPlaceSound() { return this.materialToPlace instanceof PlaceableMaterial pM ? pM.getPlaceSound() : null; }
}
