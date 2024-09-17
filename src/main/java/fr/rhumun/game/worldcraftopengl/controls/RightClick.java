package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.props.Material;
import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.props.Block;

public class RightClick extends Control {
    @Override
    public void onKeyPressed(Player player) {
        // Déterminer la face du bloc où le joueur a cliqué
        Block block = player.getBlockToPlace();
        if (block == null) {
            System.out.println("Impossible de déterminer la face du bloc.");
            return;
        }


        // Appeler la méthode pour placer le bloc dans le jeu
        block.setMaterial(Material.DIRT);
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
