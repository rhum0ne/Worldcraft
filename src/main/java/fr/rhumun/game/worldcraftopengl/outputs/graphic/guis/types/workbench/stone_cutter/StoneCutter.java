package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench.stone_cutter;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench.Workbench;

public class StoneCutter extends Workbench {

    protected StoneCutter() {
        super("Stone Cutter");

        this.addResults(new ItemStack(Material.STONE, Model.BLOCK, 1), new ItemStack[]{
                new ItemStack(Material.COBBLE, Model.BLOCK, 1),
                new ItemStack(Material.STONE, Model.SLAB, 2),
                new ItemStack(Material.STONE_BRICK, Model.BLOCK, 1),
                new ItemStack(Material.STONE_BRICK, Model.CYLINDER, 1),
                new ItemStack(Material.STONE_BRICK, Model.SLAB, 2),
                new ItemStack(Material.CRACKED_STONE_BRICK, Model.BLOCK, 1),
                new ItemStack(Material.CRACKED_STONE_BRICK, Model.SLAB, 2)
        });

        this.addResults(new ItemStack(Material.COBBLE, Model.BLOCK, 1), new ItemStack[]{
                new ItemStack(Material.COBBLE, Model.SLAB, 2),
                new ItemStack(Material.COBBLE, Model.CYLINDER, 1)
        });
    }
}
