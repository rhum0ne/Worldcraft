package fr.rhumun.game.worldcraftopengl.content.materials.blocks;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PointLight;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;
import org.joml.Vector3f;
import fr.rhumun.game.worldcraftopengl.content.GuiTypes;

public class RedstoneLampMaterial extends PointLight  implements PlaceableMaterial {
    public RedstoneLampMaterial() {
        super(Texture.REDSTONE_LAMP_ON);
        this.addToType(GuiTypes.FUNCTIONAL_BLOCKS);
        this.ambient = new Vector3f(0.4f, 0.3f, 0.0f);
        this.diffuse = new Vector3f(0.8f, 0.6f, 0.1f);
        this.specular = new Vector3f(0.05f, 0.05f, 0.0f);
        this.constant = 0.03f;
        this.linear = 0.05f;
        this.quadratic = 0.02f;
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
        return OpacityType.OPAQUE;
    }
}
