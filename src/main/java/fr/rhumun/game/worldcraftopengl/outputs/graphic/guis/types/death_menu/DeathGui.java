package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.death_menu;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.FullscreenTiledGui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

/**
 * GUI displayed when the player dies. It offers options to respawn or
 * quit back to the title menu.
 */
public class DeathGui extends FullscreenTiledGui {

    public DeathGui() {
        super(Texture.DARK_COBBLE);

        this.addText(0, -80, "Vous \u00eates mort");

        this.addButton(new Button(0, 20, 400, 40, this, "R\u00e9apparaitre") {
            @Override
            public void onClick(Player player) {
                player.respawn();
            }
        });

        this.addButton(new Button(0, 80, 400, 40, this, "Quitter") {
            @Override
            public void onClick(Player player) {
                GAME.quitWorld();
            }
        });

        this.setAlignCenter(true);
    }
}
