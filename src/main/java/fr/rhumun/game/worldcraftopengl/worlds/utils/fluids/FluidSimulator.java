package fr.rhumun.game.worldcraftopengl.worlds.utils.fluids;

import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;

import java.util.HashSet;
import java.util.Set;

/**
 * Fluid propagation helper used through block update events. When a block is
 * placed or removed, the neighbours are notified and fluids are propagated from
 * any liquid sources found around the changed block.
 */
public class FluidSimulator {

    /**
     * Triggered whenever a block is modified. The method will look for liquid
     * blocks touching the updated one and propagate their flow. If the updated
     * block is itself a liquid, it will also act as a source.
     */
    public static void onBlockUpdate(Block block) {
        if (block == null) return;

        Set<Block> visited = new HashSet<>();
        propagateFrom(block, visited);

        for (Block side : block.getSideBlocks()) {
            propagateFrom(side, visited);
        }
    }

    private static void propagateFrom(Block source, Set<Block> visited) {
        if (source == null || !visited.add(source)) return;

        Material mat = source.getMaterial();
        if (mat == null || !mat.isLiquid()) return;

        byte level = source.getState();
        if (level <= 0) return;

        Block down = source.getBlockAtDown();
        if (down != null && (down.getMaterial() == null || (down.getMaterial().isLiquid() && down.getState() < 8))) {
            down.setMaterial(mat);
            down.setState(8);
            propagateFrom(down, visited);
            return; // Downward flow has priority
        }

        spread(source.getBlockAtNorth(), mat, visited);
        spread(source.getBlockAtSouth(), mat, visited);
        spread(source.getBlockAtEast(), mat, visited);
        spread(source.getBlockAtWest(), mat, visited);
    }

    private static void spread(Block target, Material material, Set<Block> visited) {
        if (target == null) return;

        byte newState = computeState(target, material);
        if (newState <= 0) return;

        Material targetMat = target.getMaterial();
        if (targetMat == null || (targetMat.isLiquid() && target.getState() < newState)) {
            target.setMaterial(material);
            target.setState(newState);
            propagateFrom(target, visited);
        }
    }

    private static byte computeState(Block target, Material material) {
        Block above = target.getBlockAtUp();
        if (above != null && above.getMaterial() == material) {
            return 7;
        }

        byte max = 0;
        int count = 0;
        Block[] sides = new Block[] {
                target.getBlockAtNorth(),
                target.getBlockAtSouth(),
                target.getBlockAtEast(),
                target.getBlockAtWest()
        };

        for (Block b : sides) {
            if (b != null && b.getMaterial() == material) {
                byte lvl = b.getState();
                if (lvl > max) {
                    max = lvl;
                    count = 1;
                } else if (lvl == max) {
                    count++;
                }
            }
        }

        if (max == 0) return 0;
        if (count >= 2) return max;
        return (byte) (max - 1);
    }
}
