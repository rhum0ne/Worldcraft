package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;

public class WorldRendererBool extends Control {
    @Override
    public void onKeyPressed(Player player) {
        Game.UPDATE_WORLD_RENDER = !Game.UPDATE_WORLD_RENDER;
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
