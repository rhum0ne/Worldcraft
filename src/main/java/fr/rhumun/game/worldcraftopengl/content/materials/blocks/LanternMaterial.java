package fr.rhumun.game.worldcraftopengl.content.materials.blocks;

import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.ToolType;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.ForcedModelMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.InteractableMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PointLight;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;
import org.joml.Vector3f;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import fr.rhumun.game.worldcraftopengl.content.GuiTypes;

public class LanternMaterial extends PointLight implements PlaceableMaterial, ForcedModelMaterial, InteractableMaterial {
    public LanternMaterial() {
        super(Texture.LANTERN);
        this.addToType(GuiTypes.FUNCTIONAL_BLOCKS);
        this.setDurability(4f);
        this.setToolType(ToolType.GLASS);
        // Couleurs adaptées pour imiter une lumière de feu
        this.ambient = new Vector3f(0.3f, 0.1f, 0.0f); // Teinte chaude et orangée pour l'ambient
        this.diffuse = new Vector3f(0.5f, 0.1f, 0.0f); // Orange intense pour le diffuse
        this.specular = new Vector3f(0.1f, 0.05f, 0.0f); // Speculaire jaune/or pour des reflets plus doux
        this.constant = 0.03f;   // Atténuation plus faible pour une lumière plus proche
        this.linear = 0.05f;     // Atténuation modérée pour une propagation douce
        this.quadratic = 0.02f;  // Atténuation plus marquée à longue distance
    }

    @Override
    public Sound getPlaceSound() {
        return SoundPack.GLASS.getRandom();
    }
    @Override
    public Sound getBreakSound() {
        return SoundPack.GLASS_BREAK.getRandom();
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.CLOSE_TRANSPARENT;
    }

    @Override
    public Model getModel() {
        return Model.CROSS;
    }

    @Override
    public void interact(Player player, Block block) {
        if(block.getState() == 0){
            block.setState((byte) 1);
        }
        else {
            block.setState((byte) 0);
        }
        GAME.getGraphicModule().getLightningsUtils().updateLights();
    }
}
