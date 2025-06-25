package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.content.materials.Materials;

/**
 * Simple sawmill converting logs into planks or slabs.
 */
public class Sawmill extends Workbench {

    public Sawmill() {
        super("Sawmill");
        this.addResults(new ItemStack(Materials.LOG), new ItemStack[]{
                        new ItemStack(Materials.PLANKS, Model.BLOCK,4),
                        new ItemStack(Materials.PLANKS, Model.SLAB,8),
                        new ItemStack(Materials.PLANKS, Model.CYLINDER, 4) });

        this.addResults(new ItemStack(Materials.PLANKS), new ItemStack[]{
                new ItemStack(Materials.PLANKS, Model.SLAB,2),
                new ItemStack(Materials.PLANKS, Model.CYLINDER, 1) });

        this.addResults(new ItemStack(Materials.BIRCH_LOG), new ItemStack[]{
                new ItemStack(Materials.BIRCH_PLANKS, Model.BLOCK, 4),
                new ItemStack(Materials.BIRCH_PLANKS, Model.SLAB, 8),
                new ItemStack(Materials.BIRCH_PLANKS, Model.CYLINDER, 4) });

        this.addResults(new ItemStack(Materials.BIRCH_PLANKS), new ItemStack[]{
                new ItemStack(Materials.BIRCH_PLANKS, Model.SLAB, 2),
                new ItemStack(Materials.BIRCH_PLANKS, Model.CYLINDER, 1) });

        this.addResults(new ItemStack(Materials.ACACIA_LOG), new ItemStack[]{
                new ItemStack(Materials.ACACIA_PLANKS, Model.BLOCK, 4),
                new ItemStack(Materials.ACACIA_PLANKS, Model.SLAB, 8),
                new ItemStack(Materials.ACACIA_PLANKS, Model.CYLINDER, 4) });

        this.addResults(new ItemStack(Materials.ACACIA_PLANKS), new ItemStack[]{
                new ItemStack(Materials.ACACIA_PLANKS, Model.SLAB, 2),
                new ItemStack(Materials.ACACIA_PLANKS, Model.CYLINDER, 1) });

        this.addResults(new ItemStack(Materials.CHERRY_LOG), new ItemStack[]{
                new ItemStack(Materials.CHERRY_PLANKS, Model.BLOCK, 4),
                new ItemStack(Materials.CHERRY_PLANKS, Model.SLAB, 8),
                new ItemStack(Materials.CHERRY_PLANKS, Model.CYLINDER, 4) });

        this.addResults(new ItemStack(Materials.CHERRY_PLANKS), new ItemStack[]{
                new ItemStack(Materials.CHERRY_PLANKS, Model.SLAB, 2),
                new ItemStack(Materials.CHERRY_PLANKS, Model.CYLINDER, 1) });

        this.addResults(new ItemStack(Materials.SPRUCE_LOG), new ItemStack[]{
                new ItemStack(Materials.SPRUCE_PLANKS, Model.BLOCK, 4),
                new ItemStack(Materials.SPRUCE_PLANKS, Model.SLAB, 8),
                new ItemStack(Materials.SPRUCE_PLANKS, Model.CYLINDER, 4) });

        this.addResults(new ItemStack(Materials.SPRUCE_PLANKS), new ItemStack[]{
                new ItemStack(Materials.SPRUCE_PLANKS, Model.SLAB, 2),
                new ItemStack(Materials.SPRUCE_PLANKS, Model.CYLINDER, 1) });

        this.addResults(new ItemStack(Materials.DARK_PLANKS), new ItemStack[]{
                new ItemStack(Materials.DARK_PLANKS, Model.SLAB, 2),
                new ItemStack(Materials.DARK_PLANKS, Model.CYLINDER, 1) });
    }
}
