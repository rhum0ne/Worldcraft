package fr.rhumun.game.worldcraftopengl.content.materials.blocks;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.Materials;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.*;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;
import fr.rhumun.game.worldcraftopengl.worlds.Block;

public class BirchDoorTopMaterial extends Material implements PlaceableMaterial, ForcedModelMaterial, InteractableMaterial, Multiblock, RotableMaterial {

    public BirchDoorTopMaterial() {
        super(Texture.BIRCH_DOOR_TOP);
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
        return Model.DOOR;
    }

    @Override
    public void interact(Player player, Block block) {
        int newState = block.getState() ^ 4;

        boolean open = (newState & 4) != 0;
        if(open) player.playSound(SoundPack.DOOR_OPEN.getRandom());
        else player.playSound(SoundPack.DOOR_CLOSE.getRandom());

        block.setState(newState);
        Block down = block.getBlockAtDown();
        if (down != null && !down.isAir()) {
            down.setState(newState);
        }
        block.getChunk().setToUpdate(true);
    }

    @Override
    public void onPlace(Block block) {
        Block up = block.getBlockAtDown();
        if (up != null && up.isAir()) {
            up.setModel(block.getModel()).setMaterial(Materials.BIRCH_DOOR).setState(block.getState());
        }
    }

    @Override
    public boolean showInCreativeInventory() {
        return false;
    }
}

