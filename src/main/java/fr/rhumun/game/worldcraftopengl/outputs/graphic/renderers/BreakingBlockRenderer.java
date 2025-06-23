package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.content.Mesh;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.BlockUtil;
import fr.rhumun.game.worldcraftopengl.worlds.Block;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class BreakingBlockRenderer extends GlobalRenderer {

    private final Player player;
    private Block block;
    private int stage = -1;

    public BreakingBlockRenderer(GraphicModule graphicModule, Player player) {
        super(graphicModule, ShaderManager.GLOBAL_SHADERS);
        this.player = player;
        this.init();
    }

    @Override
    public void render() {
        update();
        if (getIndicesArray().length == 0) return;

        glBindVertexArray(this.getVAO());
        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBufferData(GL_ARRAY_BUFFER, this.getVerticesArray().clone(), GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.getIndicesArray().clone(), GL_STATIC_DRAW);

        glEnable(GL_POLYGON_OFFSET_FILL);
        glPolygonOffset(-1f, -1f);
        glDepthMask(false);
        glDrawElements(GL_TRIANGLES, this.getIndicesArray().length, GL_UNSIGNED_INT, 0);
        glDepthMask(true);
        glDisable(GL_POLYGON_OFFSET_FILL);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    private void update() {
        Block target = player.getBreakingBlock();
        int currentStage = player.getBreakingStage();
        if (target == block && currentStage == stage) return;
        block = target;
        stage = currentStage;
        getVertices().clear();
        getIndices().clear();
        if (block == null || stage < 0) {
            toArrays();
            return;
        }
        raster(block, Texture.getDestroyStage(stage));
        toArrays();
    }

    private void raster(Block block, Texture texture) {
        float id = texture.getId();
        if(block.getModel() == null || block.getModel() == Model.BLOCK) {
            float[][] vertices = BlockUtil.createVertices(block, block, new float[]{id,id,id,id,id,id});
            int[] indices = BlockUtil.createIndices(0);

            addAllVertices(vertices);
            addAllIndices(indices);
            return;
        }

        Mesh obj = block.getModel().get();
        if (obj == null) return;

        FloatBuffer verticesBuffer = obj.getVerticesBuffer().duplicate();
        FloatBuffer normalsBuffer = obj.getNormalsBuffer().duplicate();
        FloatBuffer texCoordsBuffer = obj.getTexCoordsBuffer().duplicate();
        IntBuffer indicesBuffer = obj.getIndicesBuffer().duplicate();

        double x = block.getLocation().getX();
        double y = block.getLocation().getY();
        double z = block.getLocation().getZ();

        while (indicesBuffer.hasRemaining()) {
            int vertexIndex = indicesBuffer.get();

            float nx = normalsBuffer.get(vertexIndex * 3);
            float ny = normalsBuffer.get(vertexIndex * 3 + 1);
            float nz = normalsBuffer.get(vertexIndex * 3 + 2);

            if (block.hasBlockAtFace(nx, ny, nz)) continue;

            float offset = 0.002f;
            float vx = (float) (x + verticesBuffer.get(vertexIndex * 3) + nx * offset);
            float vy = (float) (y + verticesBuffer.get(vertexIndex * 3 + 1) + ny * offset);
            float vz = (float) (z + verticesBuffer.get(vertexIndex * 3 + 2) + nz * offset);

            float u = texCoordsBuffer.get(vertexIndex * 2);
            float v = texCoordsBuffer.get(vertexIndex * 2 + 1);

            float[] vertexData = new float[]{vx, vy, vz, u, v, texture.getId(), nx, ny, nz};
            addVertex(vertexData);
        }
    }

    private void addVertex(float[] vertexData) {
        getVertices().add(vertexData);
        addIndice();
    }
}
