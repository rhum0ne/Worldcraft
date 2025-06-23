package fr.rhumun.game.worldcraftopengl.content.materials.items;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;

public abstract class ConsumableItem extends ItemMaterial implements UsableItem{

    public ConsumableItem(Texture texture) {
        super(texture);
    }

    @Override
    public void onClick(Player player) {
        player.getInventory().removeItem(this);
        applyEffect(player);
    }

    public abstract boolean canConsume(Player player);

    public abstract void applyEffect(Player player);
}
