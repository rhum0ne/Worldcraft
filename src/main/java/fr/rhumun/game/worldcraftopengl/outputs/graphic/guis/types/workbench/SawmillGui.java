package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Slot;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.Game.GUI_ZOOM;

/**
 * GUI showing the sawmill workbench next to the player's inventory.
 */
public class SawmillGui extends CenteredGUI {

    private static final int SAWMILL_WIDTH = 176;
    private static final int INVENTORY_WIDTH = 356;
    private static final int INVENTORY_HEIGHT = 166;
    private static final int GAP = 20;

    public SawmillGui() {
        super(SAWMILL_WIDTH + GAP + INVENTORY_WIDTH, INVENTORY_HEIGHT, null);

        Sawmill sawmill = new Sawmill();
        this.addComponent(sawmill);
    }
}
