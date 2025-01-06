package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;

public class ShowTrianlges extends Control {
    @Override
    public void onKeyPressed(Player player) {
        getGame().setShowingTriangles(!getGame().isShowingTriangles());
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
