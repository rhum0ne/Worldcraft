package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.Inventory;
import fr.rhumun.game.worldcraftopengl.Item;
import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.blocks.MeshArrays;
import fr.rhumun.game.worldcraftopengl.blocks.Model;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ShaderUtils;

import java.nio.FloatBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class ItemsRenderer extends Renderer {

    private boolean toUpdate = false;

    public ItemsRenderer(GraphicModule graphicModule) {
        super(graphicModule);
    }

    @Override
    public void init() {
        super.init();

        glUseProgram(ShaderUtils.PLAN_SHADERS.id);
        glBindVertexArray(this.getVAO());
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


// Envoi des vertices au VBO
        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBufferData(GL_ARRAY_BUFFER, this.getVerticesArray(), GL_STATIC_DRAW);
// Envoi des indices au IBO
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.indicesArray, GL_STATIC_DRAW);

// Position (aPos)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

// Coordonnées de texture (aTexCoord)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 1, GL_FLOAT, false, 6 * Float.BYTES, 5 * Float.BYTES);
        glEnableVertexAttribArray(2);


// Désactiver VAO
        glBindVertexArray(0);

    }

    public void update() {
// Envoi des vertices au VBO
        glBindVertexArray(this.getVAO());
        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBufferData(GL_ARRAY_BUFFER, this.getVerticesArray(), GL_STATIC_DRAW);
// Envoi des indices au IBO
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.indicesArray, GL_STATIC_DRAW);
    }

    @Override
    public void render() {
        if(toUpdate) update();

        glEnable(GL_BLEND);
        glBindVertexArray(this.getVAO());

        // Activer la texture
        //glBindTexture(GL_TEXTURE_2D, Texture.CROSSHAIR.getId());

        // Dessiner le quad avec les indices
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);
        glDisable(GL_BLEND);
    }


    @Override
    public void cleanup() {
        glDeleteBuffers(this.getVBO());
        glDeleteVertexArrays(this.getVAO());
    }

    public void setItems(Inventory inventory){
        this.getVertices().clear();
        this.setIndice(0);

        int i = 0;
        for(Item item : inventory.getItems()){
            if(item == null) continue;
            addItem(item.getMaterial(), i);
            i++;
        }

        this.toArrays();
        toUpdate = true;
    }

    public void addItem(Material item, int s){
        System.out.println("Adding item " + item + " in " + s);
        MeshArrays mesh = Model.BLOCK.get();
        int numVertices = mesh.getNumVertices();

        FloatBuffer verticesBuffer = mesh.getVertices();
        FloatBuffer texCoordsBuffer = mesh.getTexCoords();

//        double x = block.getLocation().getX();
//        double y = block.getLocation().getY();
//        double z = block.getLocation().getZ();

        for (int i = 0; i < numVertices; i++) {
            float vx = (float) (verticesBuffer.get(i * 3) / 12 - 0.44 + 0.11*s);
            float vy = (float) (verticesBuffer.get(i * 3 + 1) / 10 - 0.95);
            float vz = (float) (verticesBuffer.get(i * 3 + 2) / 12);

//            float dx = vx;
//            float dz = vz;
//            vx += vz/2;
//            vz+= dx/2;
//
//            vy+=dx/4+dz/4;

            float u = texCoordsBuffer.get(i * 2);
            float v = texCoordsBuffer.get(i * 2 + 1);

            addVertex(new float[]{vx, vy, vz, u, v, item.getTextureID()});
        }
    }

    private void addVertex(float[] vertexData) {
        System.out.println(Arrays.toString(vertexData));
        this.getVertices().add(vertexData);
            // L'index du sommet est simplement l'index actuel dans la liste
            // Par exemple, si c'est le 4e sommet qu'on ajoute, son index sera 3
        this.addIndice();

            //indices.add(index);
    }

//        this.getVertices().add(new float[]{(float) (-1+0.2*i), -1f, 0f, -1, 0, item.getTextureID()});
//        this.getVertices().add(new float[]{(float) (-0.8+0.2*i), -1f, 0f, -1, 0, item.getTextureID()});
//        this.getVertices().add(new float[]{(float) (-0.8+0.2*i), -0.8f, 0f, -1, 0, item.getTextureID()});
//        this.getVertices().add(new float[]{(float) (-1+0.2*i), -0.8f, 0f, -1, 0, item.getTextureID()});
//
//        this.addIndice();
//        this.addIndice();
//        this.addIndice();
//        this.addIndice();
}
