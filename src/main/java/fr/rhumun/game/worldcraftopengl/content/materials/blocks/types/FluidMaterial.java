package fr.rhumun.game.worldcraftopengl.content.materials.blocks.types;

import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import lombok.Getter;

@Getter
public abstract class FluidMaterial extends Material implements PlaceableMaterial{
    float Opacity;
    private int viscosity = 5;

    public FluidMaterial(Texture texture, float friction, float density) {
        super(texture, friction, density);
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.LIQUID;
    }
}
