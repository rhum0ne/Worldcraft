package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.props.Block;

public class LeftClick extends Control {
    @Override
    public void onKeyPressed(Player player) {
        Block block = player.getSelectedBlock();
        if(block == null || block.getMaterial() == null) return;
        block.setMaterial(null);
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
