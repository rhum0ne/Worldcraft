package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.items.Item;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Slot;

/**
 * Simple sawmill converting logs into planks or slabs.
 */
public class Sawmill extends Workbench {

    public Sawmill() {
        super("Sawmill");
        this.addResults(new Item(Material.LOG), new Item[]{ new Item(Material.PLANKS, Model.SLAB), new Item(Material.PLANKS, Model.CYLINDER) });
        this.addResults(new Item(Material.BIRCH_LOG), new Item[]{ new Item(Material.BIRCH_PLANKS, Model.SLAB), new Item(Material.BIRCH_PLANKS, Model.CYLINDER) });
    }
}
