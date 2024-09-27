package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.blocks.Block;

public class MiddleClick extends Control {
    @Override
    public void onKeyPressed(Player player) {
        Block block = player.getSelectedBlock();

        if(block != null) player.setSelectedMaterial(block.getMaterial());
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
