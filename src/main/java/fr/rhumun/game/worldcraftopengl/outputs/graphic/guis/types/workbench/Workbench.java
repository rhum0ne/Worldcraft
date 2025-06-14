package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench;

import fr.rhumun.game.worldcraftopengl.content.items.Item;
import fr.rhumun.game.worldcraftopengl.content.items.ItemContainer;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.ClickableSlot;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Slot;
import fr.rhumun.game.worldcraftopengl.entities.Player;

/**
 * Base GUI for crafting stations. It provides a single input slot and manages
 * items internally so subclasses only need to register result slots.
 */
public abstract class Workbench extends CenteredGUI implements ItemContainer {

    private final Item[] items = new Item[1];
    protected final ClickableSlot inputSlot;

    protected Workbench(int width, int height, Texture texture) {
        super(width, height, texture);
        this.setItemContainer(this);
        // create input slot on the left
        this.inputSlot = this.createClickableSlot(20, 40, Slot.DEFAULT_SIZE);
    }

    protected Item getInput() {
        return items[0];
    }

    protected void clearInput() {
        items[0] = null;
    }

    @Override
    public Item[] getItems() {
        return items;
    }

    @Override
    public void setItem(int slot, Item item) {
        items[slot] = item;
    }

    /**
     * Called when a result slot is clicked. Subclasses should perform the
     * actual crafting logic here.
     */
    protected abstract void craft(Player player, Item result, int amount);
}
