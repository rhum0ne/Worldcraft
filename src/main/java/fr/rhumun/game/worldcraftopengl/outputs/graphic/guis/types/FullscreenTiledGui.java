package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Resizable;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

/**
 * Base GUI filling the screen and repeating its texture
 * to avoid stretching small images.
 */
public class FullscreenTiledGui extends CenteredGUI implements Resizable {
    public FullscreenTiledGui(Texture texture) {
        super(GAME.getGraphicModule().getWidth(), GAME.getGraphicModule().getHeight(), texture);
    }

    @Override
    public void updateVertices() {
        if (!hasTexture()) return;
        int x = getX();
        int y = getY();
        float repeatX = (float) getWidth() / (getTexture().getWidth()*2);
        float repeatY = (float) getHeight() / (getTexture().getHeight()*2);
        setVertices(new float[]{
            x, y, 0f, 0f, repeatY, getTexture().getId(),
            x + getWidth(), y, 0f, repeatX, repeatY, getTexture().getId(),
            x, y + getHeight(), 0f, 0f, 0f, getTexture().getId(),
            x + getWidth(), y + getHeight(), 0f, repeatX, 0f, getTexture().getId(),
        });
        setIndices(new int[]{0, 2, 1, 2, 3, 1});
        if (isInitialized()) updateVAO();
    }

    @Override
    public void resize() {
        int x = getX();
        int y = getY();
        float repeatX = (float) getWidth() / (getTexture().getWidth()*2);
        float repeatY = (float) getHeight() / (getTexture().getHeight()*2);
        setVertices(new float[]{
                x, y, 0f, 0f, repeatY, getTexture().getId(),
                x + getWidth(), y, 0f, repeatX, repeatY, getTexture().getId(),
                x, y + getHeight(), 0f, 0f, 0f, getTexture().getId(),
                x + getWidth(), y + getHeight(), 0f, repeatX, 0f, getTexture().getId(),
        });
        setIndices(new int[]{0, 2, 1, 2, 3, 1});
        if (isInitialized()) updateVAO();
    }
}
