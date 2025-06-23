package fr.rhumun.game.worldcraftopengl.content.materials.blocks;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.Materials;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.*;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;
import fr.rhumun.game.worldcraftopengl.worlds.Block;

public class AcaciaDoorTopMaterial extends Material implements PlaceableMaterial, ForcedModelMaterial, InteractableMaterial, Multiblock {

    public AcaciaDoorTopMaterial() {
        super(Texture.ACACIA_DOOR_TOP);
    }

    @Override
    public Sound getPlaceSound() {
        return SoundPack.WOOD.getRandom();
    }
    @Override
    public Sound getBreakSound() {
        return SoundPack.WOOD.getRandom();
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.TRANSPARENT;
    }

    @Override
    public Model getModel() {
        return Model.BLOCK;
    }

    @Override
    public void interact(Player player, Block block) {
        block.setState(block.getState() == 0 ? 1 : 0);
    }

    @Override
    public void onPlace(Block block) {
        Block up = block.getBlockAtDown();
        if (up != null && up.getMaterial() == null) {
            up.setModel(block.getModel()).setMaterial(Materials.ACACIA_DOOR);
        }
    }

    @Override
    public boolean showInCreativeInventory() {
        return false;
    }
}
