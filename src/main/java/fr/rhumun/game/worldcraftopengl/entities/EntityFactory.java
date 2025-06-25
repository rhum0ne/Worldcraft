package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class EntityFactory {

    public static Entity createItemEntity(Location loc, ItemStack item){
        Entity entity = new ItemEntity(item.getModel(), item.getMaterial(), loc, item.getQuantity());
        loc.getWorld().getEntities().add(entity);
        return entity;
    }

}
