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
        super(176, 100, Texture.DARK_COBBLE);
        new ResultSlot(100, 40, Slot.DEFAULT_SIZE, new Item(Material.PLANKS), 4, this);
        new ResultSlot(140, 40, Slot.DEFAULT_SIZE, new Item(Material.PLANKS, Model.SLAB), 8, this);
    }

    @Override
    protected void craft(Player player, Item result, int amount) {
        Item input = getInput();
        if (input == null) return;
        Material mat = input.getMaterial();
        if (mat != Material.LOG && mat != Material.BIRCH_LOG) return;

        clearInput();
        for (int i = 0; i < amount; i++) {
            player.addItem(new Item(result.getMaterial(), result.getModel()));
        }
        inputSlot.update();
    }
}
