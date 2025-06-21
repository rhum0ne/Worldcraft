package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;

/** Toggle displaying entity and block hitboxes */
public class ToggleHitboxes extends Control {
    @Override
    public void onKeyPressed(Player player) {
        Game.SHOWING_HITBOXES = !Game.SHOWING_HITBOXES;
    }

    @Override
    public void onKeyReleased(Player player) {
        // no-op
    }
}
