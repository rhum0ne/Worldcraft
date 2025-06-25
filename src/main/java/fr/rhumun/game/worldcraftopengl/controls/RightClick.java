package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.content.materials.items.types.BlockItemMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.items.types.UsableItem;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.InteractableMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PlaceableMaterial;

public class RightClick extends Control {

    public RightClick(){
        super(false, true);
    }
    @Override
    public void onKeyPressed(Player player) {
        if(getGame().isPaused()){
            getGame().getGraphicModule().getGuiModule().rightClick(player);
            return;
        }


        Block block = player.getSelectedBlock();
        if(block != null) {
            Material material = block.getMaterial();
            if (material != null && material instanceof InteractableMaterial itM) {
                itM.interact(player, block);
                return;
            }
        }

        ItemStack item = player.getSelectedItem();
        if(item != null && (item.getMaterial() instanceof PlaceableMaterial || item.getMaterial() instanceof BlockItemMaterial))
            player.placeBlock(item);

        else if(item != null && item.getMaterial() instanceof UsableItem itM)
            itM.onClick(player);

    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
