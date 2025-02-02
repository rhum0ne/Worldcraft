package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;

public class OtterEntity extends Entity {

    public OtterEntity(Game game, double x, double y, double z, float yaw, float pitch) {
        super(game, Model.OTTER, (short) Texture.OTTER.getId(), 1, 0.3f, 0.6f, 1, 1, 2, 0.1f, 1, x, y, z, yaw, pitch);
    }
}
