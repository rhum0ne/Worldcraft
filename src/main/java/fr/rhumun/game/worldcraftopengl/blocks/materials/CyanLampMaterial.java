package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.PointLight;
import fr.rhumun.game.worldcraftopengl.blocks.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import org.joml.Vector3f;

public class CyanLampMaterial extends PointLight {
    public CyanLampMaterial() {
        super(Texture.LAMP);
        this.ambient = new Vector3f(0.3f, 0.2f, 0.5f); // Violet pâle pour l'ambient
        this.diffuse = new Vector3f(0.8f, 0.5f, 0.4f); // Violet plus saturé pour le diffuse
        this.specular = new Vector3f(0.05f, 0.09f, 0.07f); // Speculaire légèrement doré pour des reflets
        this.constant = 0.03f;   // Ajuster pour la distance d'atténuation
        this.linear = 0.05f;     // Ajuster pour la propagation de la lumière
        this.quadratic = 0.01f; // Ajuster pour l'atténuation à longue distance
    }


    @Override
    public Sound getSound() {
        return Sound.STONE;
    }

    @Override
    public boolean isOpaque() {
        return true;
    }
}