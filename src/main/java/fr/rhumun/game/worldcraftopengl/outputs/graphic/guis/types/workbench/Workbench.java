package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench;

import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.content.items.ItemContainer;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.ClickableSlot;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Slot;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import lombok.Getter;

import java.util.HashMap;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.Game.GUI_ZOOM;
import static fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.IntegrablePlayerInventory.*;

/**
 * Base GUI for crafting stations. It provides a single input slot and manages
 * items internally so subclasses only need to register result slots.
 */
@Getter
public abstract class Workbench extends CenteredGUI implements ItemContainer {

    private ItemStack input;
    protected final ClickableSlot inputSlot;
    private HashMap<ItemStack, ItemStack[]> crafts = new HashMap<>();
    private ResultSlot[] resultSlots = new ResultSlot[9];

    protected Workbench(String name) {
        super(364, 320, Texture.WORKBENCH);
        this.setItemContainer(this);
        // create input slot on the left
        this.inputSlot = this.createClickableSlot(167, 30, Slot.DEFAULT_SIZE);

        this.addText(4, 4, name);

        for(int i=0; i<9; i++){
            int x = 7 + i*41;
            int y = 69;
            resultSlots[i] = new ResultSlot(x, y, Slot.DEFAULT_SIZE, this);
            this.addComponent(resultSlots[i]);
        }

        int invX = 4 * GUI_ZOOM;
        int invY = 141 * GUI_ZOOM;
        Gui inventory = new Gui(invX, invY, INVENTORY_WIDTH, INVENTORY_HEIGHT, null, this);
        inventory.setItemContainer(GAME.getPlayer().getInventory());

        addInventoryComponentsTo( inventory);

        this.addComponent(inventory);
    }

    protected void clearInput() {
        input = null;
        updateResults();
    }

    public ItemStack[] getOutputs() {
        return input != null ? crafts.get(input) : null;
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        input = item;
        this.updateResults();
    }

    @Override
    public ItemStack[] getItems() {
        return new ItemStack[]{input};
    }

    public void updateResults(){
        System.out.println("update results");
        clearResults();
        if(input == null || !crafts.containsKey(input)) return;
        System.out.println("input: " + input.toString());

        for(int i=0; i<resultSlots.length; i++){
            ItemStack result = i<crafts.get(input).length ? crafts.get(input)[i] : null;
            resultSlots[i].setItem(result);
            if(result == null) continue;
            System.out.println("result " + i + ": " + result.toString());
        }
    }

    private void clearResults() {
        for(ResultSlot slot : resultSlots) slot.setItem(null);
    }

    /**
     * Called when a result slot is clicked. Subclasses should perform the
     * actual crafting logic here.
     */
    public void craft(Player player, ItemStack result){
        player.getInventory().addItem(result);
        this.clearInput();
    }

    protected void addResults(ItemStack item, ItemStack[] items) {
        crafts.put(item, items);
    }
}
