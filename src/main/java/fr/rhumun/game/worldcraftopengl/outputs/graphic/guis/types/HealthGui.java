package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Image;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class HealthGui extends Gui {

    private static final int HEART_SIZE = 18;
    private final Image[] hearts = new Image[10];

    public HealthGui() {
        super(10, 10, HEART_SIZE * 10, HEART_SIZE, null);

        for (int i = 0; i < hearts.length; i++) {
            int x = i * HEART_SIZE;
            createImage(x, 0, HEART_SIZE, HEART_SIZE, Texture.HEART_CONTAINER);
            hearts[i] = createImage(x, 0, HEART_SIZE, HEART_SIZE, null);
        }
    }

    @Override
    public void update() {
        Player p = GAME.getPlayer();
        if (p == null || p.isInCreativeMode()) {
            for (Image heart : hearts) {
                heart.set2DTexture(null);
            }
            return;
        }

        int health = Math.max(0, Math.min(p.getHealth(), 20));
        int full = health / 2;
        boolean half = (health % 2) == 1;

        for (int i = 0; i < hearts.length; i++) {
            if (i < full) {
                hearts[i].set2DTexture(Texture.HEART_FULL);
            } else if (i == full && half) {
                hearts[i].set2DTexture(Texture.HEART_HALF);
            } else {
                hearts[i].set2DTexture(null);
            }
        }
    }
}
