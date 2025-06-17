package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.types.InteractableMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench.SawmillGui;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

/**
 * Material representing a sawmill block that opens its crafting interface when
 * interacted with.
 */
public class SawmillMaterial extends AbstractMaterial implements InteractableMaterial {

    public SawmillMaterial() {
        super(Texture.BOOKSHELF); // reuse bookshelf texture for now
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
