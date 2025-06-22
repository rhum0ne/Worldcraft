package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.ForcedModelMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.types.PointLight;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;
import org.joml.Vector3f;

public class TorchMaterial extends PointLight implements PlaceableMaterial, ForcedModelMaterial {
    public TorchMaterial() {
        super(Texture.TORCH);
        this.ambient = new Vector3f(0.4f, 0.3f, 0.0f);
        this.diffuse = new Vector3f(0.8f, 0.6f, 0.1f);
        this.specular = new Vector3f(0.05f, 0.05f, 0.0f);
        this.constant = 0.03f;
        this.linear = 0.05f;
        this.quadratic = 0.02f;
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
        return OpacityType.CLOSE_TRANSPARENT;
    }

    @Override
    public Model getModel() {
        return Model.CROSS;
    }
}
