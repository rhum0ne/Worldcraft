package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.blocks.Block;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.InteractableMaterial;

public class RightClick extends Control {
    @Override
    public void onKeyPressed(Player player) {
        Block block = player.getSelectedBlock();
        if(block != null) {
            Material material = block.getMaterial();
            if (material != null && material.getMaterial() instanceof InteractableMaterial itM) {
                itM.interact(player, block);
                return;
            }
        }

        player.placeBlock(player.getSelectedMaterial());
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
