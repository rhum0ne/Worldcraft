package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.props.Block;

import static fr.rhumun.game.worldcraftopengl.outputs.audio.Sound.GRASS;

public class LeftClick extends Control {
    @Override
    public void onKeyPressed(Player player) {
        player.breakBlock();
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
