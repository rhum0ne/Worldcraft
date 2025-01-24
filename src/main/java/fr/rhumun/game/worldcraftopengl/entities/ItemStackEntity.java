package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.items.Item;

public class ItemStackEntity extends Entity{

    public final Item itemStack;

    public ItemStackEntity(Game game, Item itemStack, float x, float y , float z) {
        super(game, 0, 0.2f, 0.2f, 0, 0, 0, 0, 0, x, y, z, 0, 0);
        this.itemStack = itemStack;
    }


}
