package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.types.ForcedModelMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.types.InteractableMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Multiblock;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;

public class AcaciaDoorMaterial extends AbstractMaterial implements ForcedModelMaterial, InteractableMaterial, Multiblock {
    public AcaciaDoorMaterial() {
        super(Texture.ACACIA_DOOR);
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
        return OpacityType.OPAQUE;
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
        Block up = block.getBlockAtUp();
        if (up != null && up.getMaterial() == null) {
            up.setModel(block.getModel()).setMaterial(Material.ACACIA_DOOR);
        }
    }
}
