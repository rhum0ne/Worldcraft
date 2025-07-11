package fr.rhumun.game.worldcraftopengl.content.materials.blocks;

import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.InteractableMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench.SawmillGui;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import fr.rhumun.game.worldcraftopengl.content.GuiTypes;

/**
 * Material representing a sawmill block that opens its crafting interface when
 * interacted with.
 */
public class SawmillMaterial extends Material implements PlaceableMaterial, InteractableMaterial {

    public SawmillMaterial() {
        super(Texture.SAWMILL_FRONT);
        this.addToType(GuiTypes.FUNCTIONAL_BLOCKS);
        setBackTexture(Texture.SAWMILL_SIDE_2);
        setTopTexture(Texture.SAWMILL_TOP);
        setBottomTexture(Texture.SAWMILL_BOTTOM);
        setLeftTexture(Texture.SAWMILL_SIDE);
        setRightTexture(Texture.SAWMILL_SIDE_3);
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
        GAME.getGraphicModule().getGuiModule().openGUI(new SawmillGui());
    }
}
