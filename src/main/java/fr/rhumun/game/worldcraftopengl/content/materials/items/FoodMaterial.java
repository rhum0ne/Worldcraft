package fr.rhumun.game.worldcraftopengl.content.materials.items;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;

public class FoodMaterial extends ConsumableItem {

    private final int amout;

    public FoodMaterial(Texture texture, int amout) {
        super(texture);
        this.amout = amout;
    }

    @Override
    public boolean canConsume(Player player) {
        return player.isHungry();
    }

    @Override
    public void applyEffect(Player player) {
        player.addFood(amout);
    }

}
