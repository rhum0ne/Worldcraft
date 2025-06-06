package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.worlds.SaveManager;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;

import java.util.List;

public class WorldsGui extends CenteredGUI {

    public WorldsGui() {
        super(500, 500, Texture.DARK_COBBLE);

        this.addText(0, -200, "Liste des Mondes");

        int y = -100;
        List<Seed> seeds = SaveManager.listWorldSeeds();
        for (Seed seed : seeds) {
            this.addButton(new LoadWorldButton(0, y, this, seed));
            y += 50;
        }

        this.addButton(new CreateWorldButton(0, y, this));
        y += 60;
        this.addButton(new BackButton(0, y, this));

        this.setAlignCenter(true);
    }
}
