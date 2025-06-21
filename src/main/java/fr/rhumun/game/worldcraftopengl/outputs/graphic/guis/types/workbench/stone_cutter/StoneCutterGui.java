package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench.stone_cutter;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench.Sawmill;

public class StoneCutterGui extends CenteredGUI {

    private static final int SAWMILL_WIDTH = 176;
    private static final int INVENTORY_WIDTH = 356;
    private static final int INVENTORY_HEIGHT = 166;
    private static final int GAP = 20;

    public StoneCutterGui() {
        super(SAWMILL_WIDTH + GAP + INVENTORY_WIDTH, INVENTORY_HEIGHT, null);

        StoneCutter stoneCutter = new StoneCutter();
        this.addComponent(stoneCutter);
    }

}
