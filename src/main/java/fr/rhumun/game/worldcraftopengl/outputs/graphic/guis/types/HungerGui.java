package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Image;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class HungerGui extends Gui {

    private static final int ICON_SIZE = 24;
    private final Image[] foods = new Image[10];

    public HungerGui() {
        super(10, 64, ICON_SIZE * 10, ICON_SIZE, null);

        for (int i = 0; i < foods.length; i++) {
            int x = i * ICON_SIZE;
            createImage(x, 0, ICON_SIZE, ICON_SIZE, Texture.HUNGER_CONTAINER);
            foods[i] = createImage(x, 0, ICON_SIZE, ICON_SIZE, Texture.HUNGER_CONTAINER);
        }
    }

    @Override
    public void update() {
        Player p = GAME.getPlayer();
        if (p == null || p.isInCreativeMode()) {
            for (Image food : foods) {
                food.set2DTexture(null);
            }
            return;
        }

        int hunger = Math.max(0, Math.min(p.getFood(), 20));
        int full = hunger / 2;
        boolean half = (hunger % 2) == 1;

        for (int i = 0; i < foods.length; i++) {
            if (i < full) {
                foods[i].set2DTexture(Texture.HUNGER_FULL);
            } else if (i == full && half) {
                foods[i].set2DTexture(Texture.HUNGER_HALF);
            } else {
                foods[i].set2DTexture(Texture.HUNGER_CONTAINER);
            }
        }
    }
}
