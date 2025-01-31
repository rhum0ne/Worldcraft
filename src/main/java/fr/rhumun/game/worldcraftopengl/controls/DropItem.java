package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.content.items.Item;
import fr.rhumun.game.worldcraftopengl.entities.EntityFactory;
import fr.rhumun.game.worldcraftopengl.entities.Player;

public class DropItem extends Control {
    @Override
    public void onKeyPressed(Player player) {
        System.out.println("DROP");

        Item item = player.getSelectedItem();

        if(item==null) return;

        EntityFactory.createItemEntity(player.getLocation(), item);
        player.getInventory().setItem(player.getSelectedSlot(), null);
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
