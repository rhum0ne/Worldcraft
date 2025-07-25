package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.pause_menu;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.entities.player.Gamemode;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Checkbox;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

/**
 * Simple settings menu allowing toggling of a few runtime options.
 */
public class SettingsGui extends CenteredGUI {

    public SettingsGui() {
        super(500, 400, null);

        this.addText(0, -160, "Parametres");

        Checkbox vsync = new Checkbox(-100, -80, "V-Sync", this);
        this.addText(50, -80, "V-Sync");
        vsync.setChecked(Game.ENABLE_VSYNC);
        vsync.setOnChange(() -> Game.ENABLE_VSYNC = vsync.isChecked());
        this.addButton(vsync);

        Checkbox greedy = new Checkbox(-100, -30, "Greedy Meshing", this);
        this.addText(50, -30, "Greedy meshing");
        greedy.setChecked(Game.GREEDY_MESHING);
        greedy.setOnChange(() -> Game.GREEDY_MESHING = greedy.isChecked());
        this.addButton(greedy);

        Checkbox debug = new Checkbox(-100, 20, "Debug", this);
        this.addText(50, 20, "Debug");
        debug.setChecked(Game.DEBUG);
        debug.setOnChange(() -> Game.DEBUG = debug.isChecked());
        this.addButton(debug);

        Checkbox creative = new Checkbox(-100, 70, "Mode Creatif", this);
        this.addText(50, 70, "Mode Creatif");
        creative.setChecked(GAME.getPlayer().getGamemode() == Gamemode.CREATIVE);
        creative.setOnChange(() -> GAME.getPlayer().setGamemode(
                creative.isChecked() ? Gamemode.CREATIVE : Gamemode.SURVIVAL));
        this.addButton(creative);

        this.addButton(new Button(0, 140, 400, 40, this, "Retour") {
            @Override
            public void onClick(Player player) {
                GAME.getGraphicModule().getGuiModule().openGUI(new PauseGui());
            }
        });

        this.setAlignCenter(true);
    }
}
