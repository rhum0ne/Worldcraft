package fr.rhumun.game.worldcraftopengl.content.materials.items.types;

import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.content.materials.Materials;
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
        final float STEP = 0.02f;
        Vector3f direction = player.getRayDirection();
        Vector3f pos = new Vector3f(
                (float) player.getLocation().getX(),
                (float) player.getLocation().getY(),
                (float) player.getLocation().getZ());

        Block target = null;
        for (float dist = 0; dist < player.getReach(); dist += STEP) {
            target = player.getWorld().getBlockAt(pos, true);
            if (target != null && target.getMaterial() != null) {
                break;
            }
            pos.add(direction.x * STEP,
                    direction.y * STEP,
                    direction.z * STEP);
        }

        if (target != null &&
                target.getMaterial() == Materials.WATER &&
                target.getState() == 8) {
            target.setMaterial(null);
            FluidSimulator.onBlockUpdate(target);
            player.getInventory().setItem(player.getSelectedSlot(),
                    new ItemStack(Materials.WATER_BUCKET));
            player.updateInventory();
        }
    }
}
