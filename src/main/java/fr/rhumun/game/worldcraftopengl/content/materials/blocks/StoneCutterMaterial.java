package fr.rhumun.game.worldcraftopengl.content.materials.blocks;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.InteractableMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench.stone_cutter.StoneCutterGui;
import fr.rhumun.game.worldcraftopengl.worlds.Block;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import fr.rhumun.game.worldcraftopengl.content.GuiTypes;

public class StoneCutterMaterial extends Material implements PlaceableMaterial, InteractableMaterial {

    public StoneCutterMaterial() {
        super(Texture.STONE_CUTTER_FRONT);
        this.addToType(GuiTypes.FUNCTIONAL_BLOCKS);
        this.setDurability(10f);
        this.setTopTexture(Texture.STONE_CUTTER_TOP);
        this.setBottomTexture(Texture.STONE_CUTTER_BOTTOM);
        this.setLeftTexture(Texture.STONE_CUTTER_SIDE);
        this.setRightTexture(Texture.STONE_CUTTER_SIDE);
        this.setBackTexture(Texture.STONE_CUTTER_SIDE_2);
    }

    @Override
    public Sound getPlaceSound() {
        return SoundPack.STONE.getRandom();
    }

    @Override
    public Sound getBreakSound() {
        return SoundPack.STONE_DIG.getRandom();
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.OPAQUE;
    }

    @Override
    public void interact(Player player, Block block) {
        GAME.getGraphicModule().getGuiModule().openGUI(new StoneCutterGui());
    }
}
