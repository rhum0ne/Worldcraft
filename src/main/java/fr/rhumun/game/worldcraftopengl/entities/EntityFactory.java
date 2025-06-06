package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.content.items.Item;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class EntityFactory {

    public static Entity createItemEntity(Location loc, Item item){
        Entity entity = new ItemEntity(GAME, item.getModel(), item.getMaterial(), loc);
        loc.getWorld().getEntities().add(entity);
        return entity;
    }

}
