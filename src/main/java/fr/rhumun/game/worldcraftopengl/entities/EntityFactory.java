package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.content.items.Item;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class EntityFactory {

    public static Entity createItemEntity(Location loc, Item item){
        Entity entity = new Entity(GAME, item.getModel(), (short) item.getMaterial().getId(), 1, 0.2f, 0.2f, 0, 0, 0, 0, 0, loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        loc.getWorld().getEntities().add(entity);
        return entity;
    }

}
