package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;

/**
 * Simple sawmill converting logs into planks or slabs.
 */
public class Sawmill extends Workbench {

    public Sawmill() {
        super("Sawmill");
        this.addResults(new ItemStack(Material.LOG), new ItemStack[]{
                        new ItemStack(Material.PLANKS, Model.BLOCK,4),
                        new ItemStack(Material.PLANKS, Model.SLAB,8),
                        new ItemStack(Material.PLANKS, Model.CYLINDER, 4) });

        this.addResults(new ItemStack(Material.PLANKS), new ItemStack[]{
                new ItemStack(Material.PLANKS, Model.SLAB,2),
                new ItemStack(Material.PLANKS, Model.CYLINDER, 1) });

        this.addResults(new ItemStack(Material.BIRCH_LOG), new ItemStack[]{
                new ItemStack(Material.BIRCH_PLANKS, Model.BLOCK, 4),
                new ItemStack(Material.BIRCH_PLANKS, Model.SLAB, 8),
                new ItemStack(Material.BIRCH_PLANKS, Model.CYLINDER, 4) });

        this.addResults(new ItemStack(Material.BIRCH_PLANKS), new ItemStack[]{
                new ItemStack(Material.BIRCH_PLANKS, Model.SLAB, 2),
                new ItemStack(Material.BIRCH_PLANKS, Model.CYLINDER, 1) });
    }
}
