package fr.rhumun.game.worldcraftopengl.content.materials.blocks;

import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.InteractableMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench.tool_box.ToolBoxGui;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.GuiTypes;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

/**
 * Material representing a toolbox crafting block.
 */
public class ToolBoxMaterial extends Material implements PlaceableMaterial, InteractableMaterial {

    public ToolBoxMaterial() {
        super(Texture.CRAFTING_TABLE_FRONT);
        this.addToType(GuiTypes.FUNCTIONAL_BLOCKS);
        setBackTexture(Texture.CRAFTING_TABLE_SIDE_1);
        setTopTexture(Texture.CRAFTING_TABLE_TOP);
        setBottomTexture(Texture.CRAFTING_TABLE_BOTTOM);
        setLeftTexture(Texture.CRAFTING_TABLE_SIDE);
        setRightTexture(Texture.CRAFTING_TABLE_SIDE_2);
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
    public void interact(Player player, Block block) {
        GAME.getGraphicModule().getGuiModule().openGUI(new ToolBoxGui());
    }
}
