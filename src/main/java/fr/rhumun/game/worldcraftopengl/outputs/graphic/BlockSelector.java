package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.content.Block;
import fr.rhumun.game.worldcraftopengl.content.Mesh;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.Renderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengles.GLES20.glDeleteBuffers;

public class BlockSelector extends Renderer {

    private Block selectedBlock;
    private final Player player;

    public BlockSelector(GraphicModule graphicModule, Player player) {
        super(graphicModule, ShaderUtils.SELECTED_BLOCK_SHADER);
        this.player = player;
    }

    @Override
    public void init() {
        super.init();
        glUseProgram(ShaderUtils.SELECTED_BLOCK_SHADER.id);
        glBindVertexArray(this.getVAO());
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

// Configuration des attributs de sommet pour position, coordonnées de texture et ID de texture
        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBufferData(GL_ARRAY_BUFFER, this.getVerticesArray(), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());

// Attribut de position (3 floats)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Délier le VAO
        glBindVertexArray(0);
    }

    @Override
    public void render() {
        update();

        glBindVertexArray(this.getVAO());
        glDisable(GL_BLEND);

        glDrawElements(GL_LINES, this.getIndicesArray().length, GL_UNSIGNED_INT, 0);

        glEnable(GL_BLEND);
        glBindVertexArray(0);
    }

    private void update() {
        Block actualSelectedBlock = player.getSelectedBlock();
        if(selectedBlock == actualSelectedBlock) return;
        this.getVertices().clear();
        this.getIndices().clear();
        this.selectedBlock = actualSelectedBlock;

        if(this.selectedBlock != null && this.selectedBlock.getMaterial() != null)
            raster(this.selectedBlock, this.selectedBlock.getModel().get());

        this.toArrays();
        updateVAO();
    }

    public void updateVAO() {
        glBindVertexArray(this.getVAO());

        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBufferData(GL_ARRAY_BUFFER, this.getVerticesArray().clone(), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.getIndicesArray().clone(), GL_STATIC_DRAW);

        glBindVertexArray(0);
    }

    private void raster(Block block, Mesh obj) {
        FloatBuffer verticesBuffer = obj.getVerticesBuffer().duplicate();
        IntBuffer indicesBuffer = obj.getIndicesBuffer().duplicate();

        double x = block.getLocation().getX();
        double y = block.getLocation().getY();
        double z = block.getLocation().getZ();

        //if(isBlock(block, x, y ,z)) return;
        float yOffset = 0;
        if(block.getModel() == Model.SLAB && !block.isOnTheFloor()) yOffset = 0.5f;

        while (indicesBuffer.hasRemaining()) {
            int vertexIndex = indicesBuffer.get();

            // Position du sommet
            float vx = (float) (x + verticesBuffer.get(vertexIndex * 3));
            float vy = (float) (y + verticesBuffer.get(vertexIndex * 3 + 1))+yOffset;
            float vz = (float) (z + verticesBuffer.get(vertexIndex * 3 + 2));

            // Ajoute le sommet dans le renderer approprié

            float[] vertexData = new float[]{vx, vy, vz};
            this.addVertex(vertexData);
        }
    }


    private void addVertex(float[] vertexData) {
        this.getVertices().add(vertexData);
        this.addIndice();
    }

    @Override
    public void cleanup() {
        glDeleteBuffers(this.getVBO());
        glDeleteVertexArrays(this.getVAO());
    }
}
