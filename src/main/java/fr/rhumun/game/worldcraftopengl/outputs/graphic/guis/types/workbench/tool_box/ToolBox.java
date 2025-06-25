package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench.tool_box;

import fr.rhumun.game.worldcraftopengl.content.items.ItemContainer;
import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.ClickableSlot;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Slot;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.content.materials.Materials;

import java.util.HashMap;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench.tool_box.ToolBoxCraft;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.Game.GUI_ZOOM;
import static fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.IntegrablePlayerInventory.*;

/**
 * Crafting interface using two input slots.
 */
public class ToolBox extends CenteredGUI implements ItemContainer {

    private final ItemStack[] inputs = new ItemStack[2];
    protected final ClickableSlot[] inputSlots = new ClickableSlot[2];
    private final HashMap<ToolBoxCraft, ItemStack[]> crafts = new HashMap<>();
    private final ToolBoxResultSlot[] resultSlots = new ToolBoxResultSlot[9];

    public ToolBox() {
        super(364, 320, Texture.WORKBENCH_2);
        this.setItemContainer(this);

        inputSlots[0] = this.createClickableSlot(127, 30, Slot.DEFAULT_SIZE);
        inputSlots[1] = this.createClickableSlot(207, 30, Slot.DEFAULT_SIZE);

        this.addText(4, 4, "Tool Box");

        // Example recipe: combine a stick and a log to craft a wooden axe
        this.addResults(new ItemStack(Materials.STICK), new ItemStack(Materials.PLANKS, 2),
                new ItemStack[]{ new ItemStack(Materials.WOODEN_AXE), new ItemStack(Materials.WOODEN_HOE), new ItemStack(Materials.WOODEN_PICKAXE), new ItemStack(Materials.WOODEN_SHOVEL), new ItemStack(Materials.WOODEN_SWORD) });

        this.addResults(new ItemStack(Materials.STICK), new ItemStack(Materials.ACACIA_PLANKS, 2),
                new ItemStack[]{ new ItemStack(Materials.WOODEN_AXE), new ItemStack(Materials.WOODEN_HOE), new ItemStack(Materials.WOODEN_PICKAXE), new ItemStack(Materials.WOODEN_SHOVEL), new ItemStack(Materials.WOODEN_SWORD) });

        this.addResults(new ItemStack(Materials.STICK), new ItemStack(Materials.BIRCH_PLANKS, 2),
                new ItemStack[]{ new ItemStack(Materials.WOODEN_AXE), new ItemStack(Materials.WOODEN_HOE), new ItemStack(Materials.WOODEN_PICKAXE), new ItemStack(Materials.WOODEN_SHOVEL), new ItemStack(Materials.WOODEN_SWORD) });

        this.addResults(new ItemStack(Materials.STICK), new ItemStack(Materials.CHERRY_PLANKS, 2),
                new ItemStack[]{ new ItemStack(Materials.WOODEN_AXE), new ItemStack(Materials.WOODEN_HOE), new ItemStack(Materials.WOODEN_PICKAXE), new ItemStack(Materials.WOODEN_SHOVEL), new ItemStack(Materials.WOODEN_SWORD) });

        this.addResults(new ItemStack(Materials.STICK), new ItemStack(Materials.SPRUCE_PLANKS, 2),
                new ItemStack[]{ new ItemStack(Materials.WOODEN_AXE), new ItemStack(Materials.WOODEN_HOE), new ItemStack(Materials.WOODEN_PICKAXE), new ItemStack(Materials.WOODEN_SHOVEL), new ItemStack(Materials.WOODEN_SWORD) });

        this.addResults(new ItemStack(Materials.STICK), new ItemStack(Materials.DARK_PLANKS, 2),
                new ItemStack[]{ new ItemStack(Materials.WOODEN_AXE), new ItemStack(Materials.WOODEN_HOE), new ItemStack(Materials.WOODEN_PICKAXE), new ItemStack(Materials.WOODEN_SHOVEL), new ItemStack(Materials.WOODEN_SWORD) });

        this.addResults(new ItemStack(Materials.STICK), new ItemStack(Materials.IRON_INGOT, 3),
                new ItemStack[]{ new ItemStack(Materials.IRON_AXE), new ItemStack(Materials.IRON_PICKAXE), new ItemStack(Materials.IRON_SHOVEL), new ItemStack(Materials.IRON_SWORD), new ItemStack(Materials.BUCKET) });

        for (int i = 0; i < 9; i++) {
            int x = 7 + i * 40;
            int y = 69;
            resultSlots[i] = new ToolBoxResultSlot(x, y, Slot.DEFAULT_SIZE, this);
            this.addComponent(resultSlots[i]);
        }

        int invX = 4 * GUI_ZOOM;
        int invY = 141 * GUI_ZOOM;
        Gui inventory = new Gui(invX, invY, INVENTORY_WIDTH, INVENTORY_HEIGHT, null, this);
        inventory.setItemContainer(GAME.getPlayer().getInventory());
        addInventoryComponentsTo(inventory);
        this.addComponent(inventory);
    }

    private ToolBoxCraft key(ItemStack a, ItemStack b) {
        if (a == null || b == null) return null;
        return new ToolBoxCraft(a, b);
    }

    protected void clearInputs() {
        inputs[0] = null;
        inputs[1] = null;
        updateResults();
    }

    public ItemStack[] getOutputs() {
        ToolBoxCraft k = key(inputs[0], inputs[1]);
        return k != null ? crafts.get(k) : null;
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        inputs[slot] = item;
        updateResults();
    }

    @Override
    public ItemStack[] getItems() {
        return inputs;
    }

    public void updateResults() {
        clearResults();
        ToolBoxCraft k = key(inputs[0], inputs[1]);
        if (k == null || !crafts.containsKey(k)) return;
        for (int i = 0; i < resultSlots.length; i++) {
            ItemStack result = i < crafts.get(k).length ? crafts.get(k)[i] : null;
            resultSlots[i].setItem(result);
        }
    }

    private void clearResults() {
        for (ToolBoxResultSlot slot : resultSlots) slot.setItem(null);
    }

    public void craft(Player player, ItemStack result) {
        if (result == null) return;
        player.getInventory().addItem(result);
        clearInputs();
    }

    protected void addResults(ItemStack in1, ItemStack in2, ItemStack[] results) {
        crafts.put(key(in1, in2), results);
    }
}
