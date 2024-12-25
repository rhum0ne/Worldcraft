package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Item;
import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.blocks.Block;

public class MiddleClick extends Control {
    @Override
    public void onKeyPressed(Player player) {
        Block block = player.getSelectedBlock();

        if(block != null) player.getInventory().getItems()[player.getSelectedSlot()] = new Item(block.getMaterial());
        player.updateInventory();
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
