package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench.tool_box;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;

/**
 * GUI displaying the toolbox next to the player's inventory.
 */
public class ToolBoxGui extends CenteredGUI {

    private static final int TOOLBOX_WIDTH = 176;
    private static final int INVENTORY_WIDTH = 356;
    private static final int INVENTORY_HEIGHT = 166;
    private static final int GAP = 20;

    public ToolBoxGui() {
        super(TOOLBOX_WIDTH + GAP + INVENTORY_WIDTH, INVENTORY_HEIGHT, null);
        ToolBox toolBox = new ToolBox();
        this.addComponent(toolBox);
    }
}
