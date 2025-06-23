package fr.rhumun.game.worldcraftopengl.content.materials.blocks;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.*;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;
import org.joml.Vector3f;
import fr.rhumun.game.worldcraftopengl.content.GuiTypes;

public class JackOLanternMaterial extends PointLight implements PlaceableMaterial, ForcedModelMaterial, RotableMaterial {
    public JackOLanternMaterial() {
        super(Texture.PUMPKIN);
        this.addToType(GuiTypes.FUNCTIONAL_BLOCKS);
        this.setTopAndBottomTexture(Texture.PUMPKIN_TOP);
        this.setFrontTexture(Texture.JACK_O_LANTERN);

        // Couleurs adaptées pour imiter une lumière de feu
        this.ambient = new Vector3f(0.3f, 0.1f, 0.0f); // Teinte chaude et orangée pour l'ambient
        this.diffuse = new Vector3f(0.5f, 0.1f, 0.0f); // Orange intense pour le diffuse
        this.specular = new Vector3f(0.1f, 0.05f, 0.0f); // Speculaire jaune/or pour des reflets plus doux
        this.constant = 0.1f;   // Atténuation plus faible pour une lumière plus proche
        this.linear = 0.02f;     // Atténuation modérée pour une propagation douce
        this.quadratic = 0.001f;  // Atténuation plus marquée à longue distance
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
}
