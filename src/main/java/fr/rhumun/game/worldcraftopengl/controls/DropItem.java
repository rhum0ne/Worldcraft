package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.content.items.Item;
import fr.rhumun.game.worldcraftopengl.entities.Entity;
import fr.rhumun.game.worldcraftopengl.entities.EntityFactory;
import fr.rhumun.game.worldcraftopengl.entities.Player;

public class DropItem extends Control {
    @Override
    public void onKeyPressed(Player player) {

        Item item = player.getSelectedItem();

        if(item==null) return;

        Entity entity = EntityFactory.createItemEntity(player.getLocation(), item);
        entity.getVelocity().add(player.getRayDirection());
        //System.out.println("DROP : " + item.getMaterial() + "  " + item.getModel());
        player.getInventory().setItem(player.getSelectedSlot(), null);
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
