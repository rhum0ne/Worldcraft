package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.content.items.Item;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.content.Block;

public class MiddleClick extends Control {
    @Override
    public void onKeyPressed(Player player) {
        Block block = player.getSelectedBlock();

        if(block != null) player.getInventory().getItems()[player.getSelectedSlot()] = new Item(block.getMaterial(), block.getModel());
        player.updateInventory();
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
