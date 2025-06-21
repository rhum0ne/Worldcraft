package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static fr.rhumun.game.worldcraftopengl.Game.CHUNK_SIZE;
import static fr.rhumun.game.worldcraftopengl.Game.SHOW_DISTANCE;

/**
 * Simple renderer generating a layer of moving clouds.
 */
public class CloudRenderer extends GlobalRenderer {
    private static final float CLOUD_SIZE = 32f;
    private static final int CLOUD_COUNT = 40;

    private final List<Vector2f> clouds = new ArrayList<>();
    private final Random random = new Random();
    private float offset = 0f;

    public CloudRenderer(GraphicModule graphicModule) {
        super(graphicModule, ShaderManager.CLOUD_SHADER);
        generateClouds();
        init();
    }

    private void generateClouds() {
        int range = SHOW_DISTANCE * CHUNK_SIZE;
        for (int i = 0; i < CLOUD_COUNT; i++) {
            float x = random.nextFloat() * range * 2 - range;
            float z = random.nextFloat() * range * 2 - range;
            clouds.add(new Vector2f(x, z));
        }
    }

    private void buildVertices() {
        getVertices().clear();
        getIndices().clear();
        float y = getGraphicModule().getWorld().getHeigth() + 50;
        for (Vector2f p : clouds) {
            float x1 = p.x + offset;
            float z1 = p.y;
            float x2 = x1 + CLOUD_SIZE;
            float z2 = z1 + CLOUD_SIZE;
            addAllVertices(new float[][]{
                    {x1, y, z1, 0f, 0f, 0f, 0f, -1f, 0f},
                    {x2, y, z1, 1f, 0f, 0f, 0f, -1f, 0f},
                    {x2, y, z2, 1f, 1f, 0f, 0f, -1f, 0f},
                    {x1, y, z2, 0f, 1f, 0f, 0f, -1f, 0f}
            });
            addRawIndices(new int[]{0,1,2,0,2,3});
        }
        toArrays();
    }

    @Override
    public void render() {
        buildVertices();
        super.render();
        offset += 0.01f;
    }
}
