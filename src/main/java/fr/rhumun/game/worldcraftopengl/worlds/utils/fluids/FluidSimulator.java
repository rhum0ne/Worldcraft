package fr.rhumun.game.worldcraftopengl.worlds.utils.fluids;

import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Simple fluid propagation simulator. Fluid blocks with a state value represent
 * their remaining strength. Sources have a state of 8 and propagate to
 * neighbours with decreasing state values until 0.
 */
public class FluidSimulator {

    private record FluidUpdate(Block block, Material material, byte state) {}

    public static void tick(Set<Chunk> chunks) {
        Map<Block, FluidUpdate> updates = new HashMap<>();

        for (Chunk chunk : chunks) {
            Block[][][] blocks = chunk.getBlocks();
            int height = chunk.getWorld().getHeigth();
            for (int x = 0; x < blocks.length; x++) {
                for (int y = 0; y < height; y++) {
                    for (int z = 0; z < blocks[x][y].length; z++) {
                        Block block = blocks[x][y][z];
                        Material mat = block.getMaterial();
                        if (mat == null || !mat.isLiquid()) continue;

                        byte level = block.getState();
                        if (level <= 0) continue;

                        Block down = block.getBlockAtDown();
                        if (down != null && (down.getMaterial() == null || (down.getMaterial().isLiquid() && down.getState() < 8))) {
                            updates.put(down, new FluidUpdate(down, mat, (byte) 8));
                            continue;
                        }

                        if (level > 1) {
                            byte next = (byte) (level - 1);
                            spread(block.getBlockAtNorth(), mat, next, updates);
                            spread(block.getBlockAtSouth(), mat, next, updates);
                            spread(block.getBlockAtEast(), mat, next, updates);
                            spread(block.getBlockAtWest(), mat, next, updates);
                        }
                    }
                }
            }
        }

        for (FluidUpdate u : updates.values()) {
            u.block.setMaterial(u.material);
            u.block.setState(u.state);
        }
    }

    private static void spread(Block target, Material material, byte state, Map<Block, FluidUpdate> updates) {
        if (target == null) return;
        Material targetMat = target.getMaterial();
        if (targetMat == null || (targetMat.isLiquid() && target.getState() < state)) {
            updates.put(target, new FluidUpdate(target, material, state));
        }
    }
}
