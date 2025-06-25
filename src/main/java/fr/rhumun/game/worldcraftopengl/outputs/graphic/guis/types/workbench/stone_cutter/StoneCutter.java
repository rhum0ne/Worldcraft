package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench.stone_cutter;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.content.materials.Materials;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench.Workbench;

public class StoneCutter extends Workbench {

    protected StoneCutter() {
        super("Stone Cutter");

        this.addResults(new ItemStack(Materials.STONE, Model.BLOCK, 1), new ItemStack[]{
                new ItemStack(Materials.COBBLE, Model.BLOCK, 1),
                new ItemStack(Materials.STONE, Model.SLAB, 2),
                new ItemStack(Materials.STONE_BRICK, Model.BLOCK, 1),
                new ItemStack(Materials.STONE_BRICK, Model.CYLINDER, 1),
                new ItemStack(Materials.STONE_BRICK, Model.SLAB, 2),
                new ItemStack(Materials.CRACKED_STONE_BRICK, Model.BLOCK, 1),
                new ItemStack(Materials.CRACKED_STONE_BRICK, Model.SLAB, 2)
        });

        this.addResults(new ItemStack(Materials.COBBLE, Model.BLOCK, 1), new ItemStack[]{
                new ItemStack(Materials.COBBLE, Model.SLAB, 2),
                new ItemStack(Materials.COBBLE, Model.CYLINDER, 1)
        });

        this.addResults(new ItemStack(Materials.DARK_STONE, Model.BLOCK, 1), new ItemStack[]{
                new ItemStack(Materials.DARK_COBBLE, Model.BLOCK, 1),
                new ItemStack(Materials.DARK_STONE, Model.SLAB, 2),
                new ItemStack(Materials.DARK_STONE_BRICK, Model.BLOCK, 1),
                new ItemStack(Materials.DARK_STONE_BRICK, Model.CYLINDER, 1),
                new ItemStack(Materials.DARK_STONE_BRICK, Model.SLAB, 2)
        });

        this.addResults(new ItemStack(Materials.DARK_COBBLE, Model.BLOCK, 1), new ItemStack[]{
                new ItemStack(Materials.DARK_COBBLE, Model.SLAB, 2),
                new ItemStack(Materials.DARK_COBBLE, Model.CYLINDER, 1)
        });

        this.addResults(new ItemStack(Materials.CALCITE, Model.BLOCK, 1), new ItemStack[]{
                new ItemStack(Materials.CALCITE_BRICK, Model.BLOCK, 1),
                new ItemStack(Materials.CALCITE, Model.SLAB, 2),
                new ItemStack(Materials.CALCITE_BRICK, Model.CYLINDER, 1),
                new ItemStack(Materials.CALCITE_BRICK, Model.SLAB, 2)
        });

        this.addResults(new ItemStack(Materials.CALCITE_BRICK, Model.BLOCK, 1), new ItemStack[]{
                new ItemStack(Materials.CALCITE_BRICK, Model.SLAB, 2),
                new ItemStack(Materials.CALCITE_BRICK, Model.CYLINDER, 1)
        });

        this.addResults(new ItemStack(Materials.BLACKSTONE, Model.BLOCK, 1), new ItemStack[]{
                new ItemStack(Materials.POLISHED_BLACKSTONE_BRICKS, Model.BLOCK, 1),
                new ItemStack(Materials.BLACKSTONE, Model.SLAB, 2),
                new ItemStack(Materials.POLISHED_BLACKSTONE_BRICKS, Model.CYLINDER, 1),
                new ItemStack(Materials.POLISHED_BLACKSTONE_BRICKS, Model.SLAB, 2)
        });

        this.addResults(new ItemStack(Materials.MUD_BRICKS, Model.BLOCK, 1), new ItemStack[]{
                new ItemStack(Materials.MUD_BRICKS, Model.SLAB, 2),
                new ItemStack(Materials.MUD_BRICKS, Model.CYLINDER, 1)
        });

        this.addResults(new ItemStack(Materials.STONE_DIORITE, Model.BLOCK, 1), new ItemStack[]{
                new ItemStack(Materials.STONE_DIORITE, Model.SLAB, 2),
                new ItemStack(Materials.STONE_DIORITE, Model.CYLINDER, 1)
        });
    }
}
