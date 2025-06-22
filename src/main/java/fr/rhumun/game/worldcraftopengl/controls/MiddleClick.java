package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.worlds.Block;

public class MiddleClick extends Control {
    @Override
    public void onKeyPressed(Player player) {
        Block block = player.getSelectedBlock();

        if(block != null) player.getInventory().getItems()[player.getSelectedSlot()] = new ItemStack(block.getMaterial(), block.getModel());
        player.updateInventory();
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
