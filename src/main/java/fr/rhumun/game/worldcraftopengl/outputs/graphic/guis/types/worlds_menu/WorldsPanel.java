package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.worlds.SaveManager;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;

import java.util.List;

public class WorldsPanel extends Gui {

    public WorldsPanel(int x, int y, Gui container) {
        super(x, y, 200, 200, null, container);

        int offset = 0;
        List<Seed> seeds = SaveManager.listWorldSeeds();
        for (Seed seed : seeds) {
            String name = SaveManager.getWorldName(seed);
            this.addButton(new LoadWorldButton(0, offset, seed, name, this));
            offset += 50;
        }
    }

    @Override
    public void update() {
        // Nothing to update
    }
}
