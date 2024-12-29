package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis;

import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.Renderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ShaderUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

@Getter
@Setter
public abstract class Component{

    private int VBO, EBO, VAO;


    private final int x;
    private final int y;
    private final int width;
    private final int heigth;
    private Texture texture;

    private float[] vertices;
    private boolean isInitialized = false;

    // Indices pour dessiner un quad avec deux triangles
    private int[] indices;

    public Component(int x, int y, int width, int heigth, Texture texture){

        this.x = x;
        this.y = y;
        this.width = width;
        this.heigth = heigth;
        this.texture = texture;
    }

    public void set2DTexture(Texture texture) {
        this.texture = texture;
        set2DCoordinates(this.x, this.y);
    }

    public void set2DCoordinates(int x, int y) {
        vertices = new float[]{
                // Positions        // Coordonnées de texture
                x,  y, 0.0f,   0.0f, 1.0f, texture.getId(),   // Haut gauche
                x + width,  y, 0.0f,   1.0f, 1.0f, texture.getId(),    // Haut droit
                x, y + heigth, 0.0f,   0.0f, 0.0f, texture.getId(),     // Bas gauche
                x + width, y + heigth, 0.0f,   1.0f, 0.0f, texture.getId(),    // Bas droit
        };
        indices =  new int[]{
                0, 2, 1,   // Premier triangle
                2, 3, 1    // Deuxième triangle
        };

        updateVAO();
    }

    public void init() {
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();

        glUseProgram(ShaderUtils.PLAN_SHADERS.id);
        glBindVertexArray(this.getVAO());
        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());


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

        if(hasTexture()) set2DTexture(texture);

        this.isInitialized = true;
    }

    public void render() {
        if(!isInitialized) this.init();

        update();
        if(indices == null || vertices == null) return;
        glBindVertexArray(this.getVAO());
        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());
        glUseProgram(ShaderUtils.PLAN_SHADERS.id);

        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public abstract void update();

    public void updateVAO(){
        glUseProgram(ShaderUtils.PLAN_SHADERS.id);
        glBindVertexArray(this.getVAO());

        if(vertices != null && indices != null) { // Vérifiez si le tableau vertices n'est pas nul
            glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
            glBufferData(GL_ARRAY_BUFFER, vertices.clone(), GL_STATIC_DRAW);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.clone(), GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        }

        glBindVertexArray(0);
    }
    public void cleanup() {
        glDeleteBuffers(this.getVBO());
        glDeleteVertexArrays(this.getVAO());
    }

    public boolean hasTexture(){ return this.texture != null; }


}
