package fr.rhumun.game.worldcraftopengl.content.materials.items;

import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.content.materials.Materials;
import fr.rhumun.game.worldcraftopengl.content.materials.items.types.ItemMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.items.types.UsableItem;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.worlds.utils.fluids.FluidSimulator;
import org.joml.Vector3f;

public class BucketItemMaterial extends ItemMaterial implements UsableItem {

    public BucketItemMaterial(Texture texture) {
        super(texture);
    }

    @Override
    public void onClick(Player player) {

        Block target = player.getSelectedBlock(false);

        if (target != null &&
                target.getMaterial() == Materials.WATER &&
                target.getState() == 8) {
            target.setMaterial(null);
            target.setState(0);
            FluidSimulator.onBlockUpdate(target);

            if(player.isInCreativeMode()) return;
            player.getInventory().setItem(player.getSelectedSlot(),
                    new ItemStack(Materials.WATER_BUCKET));
            player.updateInventory();
        }
    }
}
