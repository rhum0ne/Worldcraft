package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.InteractableMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.types.PointLight;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;
import org.joml.Vector3f;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class PurpleLampMaterial extends PointLight implements InteractableMaterial {
    public PurpleLampMaterial() {
        super(Texture.LAMP);
        this.ambient = new Vector3f(0.3f, 0.0f, 0.5f); // Violet pâle pour l'ambient
        this.diffuse = new Vector3f(0.5f, 0.0f, 0.8f); // Violet plus saturé pour le diffuse
        this.specular = new Vector3f(0.05f, 0.0f, 0.1f); // Speculaire légèrement doré pour des reflets
        this.constant = 0.1f;   // Ajuster pour la distance d'atténuation
        this.linear = 0.05f;     // Ajuster pour la propagation de la lumière
        this.quadratic = 0.01f; // Ajuster pour l'atténuation à longue distance
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
