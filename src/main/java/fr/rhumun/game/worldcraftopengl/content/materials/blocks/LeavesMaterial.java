package fr.rhumun.game.worldcraftopengl.content.materials.blocks;

import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.content.materials.Materials;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.BreakEvent;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.ToolType;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;
import fr.rhumun.game.worldcraftopengl.content.GuiTypes;
import fr.rhumun.game.worldcraftopengl.worlds.Block;

import java.util.Random;

public class LeavesMaterial extends Material implements PlaceableMaterial, BreakEvent {

    private Random rand = new Random();

    public LeavesMaterial() {
        super(Texture.LEAVES);
        this.addToType(GuiTypes.NATURAL);
        this.setDurability(1f);
        this.setToolType(ToolType.VEGETABLE);
    }

    @Override
    public Sound getPlaceSound() {
        return SoundPack.WET_GRASS.getRandom();
    }
    @Override
    public Sound getBreakSound() {
        return SoundPack.WET_GRASS.getRandom();
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.TRANSPARENT;
    }

    @Override
    public boolean isLootable(){ return false; }

    @Override
    public void onBreak(Block block, Player player){
        if(rand.nextBoolean())
            block.getWorld().spawnItem(new ItemStack(Materials.APPLE), block.getLocation());
    }
}
