package fr.rhumun.game.worldcraftopengl.content.materials.items;

import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Materials;
import fr.rhumun.game.worldcraftopengl.content.materials.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import lombok.Getter;

@Getter
public class BlockItemMaterial extends ItemMaterial {

    private Material materialToPlace;

    public BlockItemMaterial(Texture texture, Material materialToPlace) {
        super(texture);
        this.materialToPlace = materialToPlace;
    }

    public Sound getPlaceSound() { return this.materialToPlace instanceof PlaceableMaterial pM ? pM.getPlaceSound() : null; }
}
