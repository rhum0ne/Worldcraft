package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.FullscreenTiledGui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.TextComponent;

/**
 * Simple GUI used to display a loading message while heavy operations are
 * running. It shows the elapsed time since it was created.
 */
public class LoadingGui extends FullscreenTiledGui {

    private final TextComponent timerText;
    private final long startTime = System.currentTimeMillis();
    private final String label;

    public LoadingGui(String label) {
        super(Texture.DARK_COBBLE);
        this.label = label;
        this.addText(0, -40, label);
        this.timerText = this.addText(0, 40, "0 ms");
        this.setAlignCenter(true);
    }

    @Override
    public void update() {
        super.update();
        long elapsed = System.currentTimeMillis() - startTime;
        timerText.setText(elapsed + " ms");
    }
}
