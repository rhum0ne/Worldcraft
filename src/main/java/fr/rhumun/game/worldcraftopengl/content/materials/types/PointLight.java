package fr.rhumun.game.worldcraftopengl.content.materials.types;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

@Setter
@Getter
public abstract class PointLight extends Material {

    public Vector3f ambient;
    public Vector3f diffuse;
    public Vector3f specular;
    public float constant;
    public float linear;
    public float quadratic;


    public PointLight(Texture texture) {
        super(texture);
    }
}
