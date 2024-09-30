package fr.rhumun.game.worldcraftopengl.blocks;

import lombok.Setter;
import org.joml.Vector3f;

@Setter
public abstract class PointLight extends AbstractMaterial{

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
