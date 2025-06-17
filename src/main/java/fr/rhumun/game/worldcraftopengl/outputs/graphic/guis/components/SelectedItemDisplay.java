package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GuiModule;

import static fr.rhumun.game.worldcraftopengl.Game.GUI_ZOOM;

/**
 * Component used to display the currently selected item at the cursor
 * position when a GUI is opened.
 */
public class SelectedItemDisplay extends Slot {

    private ItemStack item;
    private ItemStack displayedItem;
    private int lastX;
    private int lastY;

    public SelectedItemDisplay() {
        super(0, 0, DEFAULT_SIZE, -1, null);
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void setItem(ItemStack item) {
        this.item = item;
    }

    @Override
    public void update() {
        GuiModule guiModule = Game.GAME.getGraphicModule().getGuiModule();
        int x = (guiModule.getCursorX() - getWidth() / 2) / GUI_ZOOM;
        int y = (guiModule.getCursorY() - getHeight() / 2) / GUI_ZOOM;
        set2DCoordinates(x, y);

        if (item != displayedItem || x != lastX || y != lastY) {
            if (item == null) {
                setTexture(null);
                getText().setText("");
            } else {
                setTexture(item.getMaterial().getMaterial().getTexture());
                if (item.getQuantity() == 1) getText().setText("");
                else getText().setText(String.valueOf(item.getQuantity()));
            }
            updateVertices(item);
            displayedItem = item;
            lastX = x;
            lastY = y;
        }
    }

    @Override
    public void onClick(Player player) {
        // No action when clicking this component
    }
}
