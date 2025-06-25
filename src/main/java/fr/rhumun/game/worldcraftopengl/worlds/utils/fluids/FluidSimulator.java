package fr.rhumun.game.worldcraftopengl.worlds.utils.fluids;

import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.FluidMaterial;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;

import java.util.*;

/**
 * Simple progressive fluid propagation system. Each block update schedules
 * neighbouring liquid blocks which will propagate step by step according
 * to the viscosity of their material.
 */
public class FluidSimulator {

    /** Entry representing a block awaiting propagation. */
    private static class FluidEntry {
        final Block block;
        int delay;
        FluidEntry(Block block, int delay) {
            this.block = block;
            this.delay = delay;
        }
    }

    private static final Queue<FluidEntry> queue = new ArrayDeque<>();

    /** Called each game tick to process pending fluid updates. */
    public static void tick() {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            FluidEntry entry = queue.poll();
            if (entry == null) break;
            if (entry.delay > 0) {
                entry.delay--;
                queue.offer(entry);
                continue;
            }
            propagateStep(entry.block);
        }
    }

    /** Schedule a fluid update around the given block. */
    public static void onBlockUpdate(Block block) {
        if (block == null) return;
        queue.offer(new FluidEntry(block, 0));
        for (Block side : block.getSideBlocks()) {
            if (side != null) queue.offer(new FluidEntry(side, 0));
        }
    }

    private static void propagateStep(Block source) {
        if (source == null) return;
        Material mat = source.getMaterial();
        if (mat == null || !mat.isLiquid()) return;

        byte level = source.getState();
        if (level < 0) return;

        byte newState = computeState(source, mat);
        if(newState < level){
            source.setState(Math.max(newState, 0));
        }

        Block down = source.getBlockAtDown();
        if (down != null && (down.getMaterial() == null || down.getMaterial().isLiquid())) {
            if(down.getMaterial() != null && down.getMaterial().isLiquid() && down.getState() >= 7) return;

            down.setMaterial(mat);
            down.setState((byte)7);
            queue.offer(new FluidEntry(down, ((FluidMaterial) mat).getViscosity()));
            return; // Downward flow has priority
        }

        attemptSpread(source.getBlockAtNorth(), mat);
        attemptSpread(source.getBlockAtSouth(), mat);
        attemptSpread(source.getBlockAtEast(), mat);
        attemptSpread(source.getBlockAtWest(), mat);
    }

    private static void attemptSpread(Block target, Material material) {
        if (target == null || (target.getMaterial() != null && !target.getMaterial().isLiquid())) return;
        byte newState = computeState(target, material);
        if (newState < 0) {
            target.setMaterial(null);
            queue.offer(new FluidEntry(target, ((FluidMaterial) material).getViscosity()));
            return;
        }
        Material targetMat = target.getMaterial();
        if((targetMat == null || !targetMat.isLiquid()) && newState == 0) return;

        if (targetMat == null || (targetMat.isLiquid() && target.getState() != newState)) {
            target.setMaterial(material);
            target.setState(newState);
            queue.offer(new FluidEntry(target, ((FluidMaterial) material).getViscosity()));
        }
    }

    private static byte computeState(Block target, Material material) {
        Block above = target.getBlockAtUp();
        if(target.getState() == 8) return 8;
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
        if (count >= 2 && max == 8) return max;
        return (byte) (max - 1);
    }
}
