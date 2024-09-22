package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.props.Material;
import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.props.Block;

import static fr.rhumun.game.worldcraftopengl.props.Material.PLANKS;

public class RightClick extends Control {
    @Override
    public void onKeyPressed(Player player) {
        player.placeBlock(player.getSelectedMaterial());
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
