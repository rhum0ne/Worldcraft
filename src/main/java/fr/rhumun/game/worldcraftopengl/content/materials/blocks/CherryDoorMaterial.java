package fr.rhumun.game.worldcraftopengl.content.materials.blocks;

import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.Materials;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.*;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;

public class CherryDoorMaterial extends Material implements PlaceableMaterial, ForcedModelMaterial, InteractableMaterial, Multiblock, RotableMaterial {
    public CherryDoorMaterial() {
        super(Texture.CHERRY_DOOR);
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
        return Model.DOOR;
    }

    @Override
    public void interact(Player player, Block block) {
        int newState = block.getState() ^ 4;

        boolean open = (newState & 4) != 0;
        if(open) player.playSound(SoundPack.DOOR_OPEN.getRandom());
        else player.playSound(SoundPack.DOOR_CLOSE.getRandom());

        block.setState(newState);
        Block up = block.getBlockAtUp();
        if (up != null && up.getMaterial() != null) {
            up.setState(newState);
        }
        block.getChunk().setToUpdate(true);
    }

    @Override
    public void onPlace(Block block) {
        Block up = block.getBlockAtUp();
        if (up != null && up.getMaterial() == null) {
            up.setModel(block.getModel()).setMaterial(Materials.CHERRY_DOOR_TOP).setState(block.getState());
        }
    }

    @Override
    public boolean showInCreativeInventory() {
        return false;
    }
}
