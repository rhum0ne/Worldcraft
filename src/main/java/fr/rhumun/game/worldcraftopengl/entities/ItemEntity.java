package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import lombok.Getter;

@Getter
public class ItemEntity extends Entity{

    private final Material material;

    public ItemEntity(Game game, Model model, Material material, Location loc) {
        super(game, model, (short) 0, 1, 0.2f, 0.2f, 0, 0, 0, 0, 0, loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        this.material = material;
    }
}
