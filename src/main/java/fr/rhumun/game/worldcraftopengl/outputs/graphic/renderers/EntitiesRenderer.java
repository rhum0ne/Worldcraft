package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.content.Block;
import fr.rhumun.game.worldcraftopengl.content.Mesh;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.Entity;
import fr.rhumun.game.worldcraftopengl.entities.ItemEntity;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderUtils;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.BlockUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class EntitiesRenderer extends GlobalRenderer{

    private final Player player;

    public EntitiesRenderer(GraphicModule graphicModule, Player player) {
        super(graphicModule, ShaderUtils.ENTITY_SHADER);
        this.player = player;
        this.init();
    }

    public void render(){
        update();
        glBindVertexArray(this.getVAO());

        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBufferData(GL_ARRAY_BUFFER, this.getVerticesArray(), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.getIndicesArray(), GL_STATIC_DRAW);

        glDrawElements(GL_TRIANGLES, this.getIndicesArray().length, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void update(){
        this.getVertices().clear();
        this.getIndices().clear();
        Iterator<Entity> it = player.getLocation().getWorld().getEntities().iterator();

        Entity e;
        while(it.hasNext()){
            e = it.next();
            if(e == player) continue;

            Model model = e.getModel();
            if(model == null) continue;

            if(e instanceof ItemEntity i)
                if(model==Model.BLOCK) BlockUtil.rasterDroppedBlockItem(e.getLocation(), i.getMaterial(), this.getVertices(), this.getIndices());
                else raster(e, model);
            else raster(e, model);
        }

        this.toArrays();
    }

    private void raster(Entity entity, Model model) {
        Mesh obj = model.get();
        if (obj == null) return;

        FloatBuffer verticesBuffer = obj.getVerticesBuffer().duplicate();
        FloatBuffer normalsBuffer = obj.getNormalsBuffer().duplicate();
        FloatBuffer texCoordsBuffer = obj.getTexCoordsBuffer().duplicate();
        IntBuffer indicesBuffer = obj.getIndicesBuffer().duplicate();

        double x = entity.getLocation().getX();
        double y = entity.getLocation().getY();
        double z = entity.getLocation().getZ();

        while (indicesBuffer.hasRemaining()) {
            int vertexIndex = indicesBuffer.get();

            // Normales
            float nx = normalsBuffer.get(vertexIndex * 3);
            float ny = normalsBuffer.get(vertexIndex * 3 + 1);
            float nz = normalsBuffer.get(vertexIndex * 3 + 2);

            // Position du sommet
            float vx = (float) (x + verticesBuffer.get(vertexIndex * 3));
            float vy = (float) (y + verticesBuffer.get(vertexIndex * 3 + 1));
            float vz = (float) (z + verticesBuffer.get(vertexIndex * 3 + 2));

            int texture = entity.getTextureID();
            // Coordonnées de texture
            float u = texCoordsBuffer.get(vertexIndex * 2);
            float v = texCoordsBuffer.get(vertexIndex * 2 + 1);

            // Ajoute le sommet dans le renderer approprié

            float[] vertexData = new float[]{vx, vy, vz, u, v, texture, nx, ny, nz};
            this.addVertex(vertexData);
        }
    }

    private void addVertex(float[] vertexData) {
        this.getVertices().add(vertexData);
        this.addIndice();
    }
}
