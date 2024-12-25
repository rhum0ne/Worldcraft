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


    private final float x;
    private final float y;
    private final float x2;
    private final float y2;
    private Texture texture;

    private float[] vertices;

    // Indices pour dessiner un quad avec deux triangles
    private int[] indices = {
            0, 2, 1,   // Premier triangle
            2, 3, 1    // Deuxième triangle
    };

    public Component(float x, float y, float x2, float y2, Texture texture){

        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.texture = texture;

        if(hasTexture()) setTexture(texture);

        init();
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        vertices = new float[]{
                // Positions        // Coordonnées de texture
                x,  y, 0.0f,   0.0f, 1.0f, texture.getId(),   // Haut gauche
                x2,  y, 0.0f,   1.0f, 1.0f, texture.getId(),    // Haut droit
                x, y2, 0.0f,   0.0f, 0.0f, texture.getId(),     // Bas gauche
                x2, y2, 0.0f,   1.0f, 0.0f, texture.getId(),    // Bas droit
        };
    }

    public void init() {
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();

        glUseProgram(ShaderUtils.PLAN_SHADERS.id);
        glBindVertexArray(this.getVAO());


// Envoi des vertices au VBO
        if(hasTexture()) {
            glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
// Envoi des indices au IBO
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        }

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

    public void render() {
        update();
        if(!hasTexture() || vertices == null) return;
        glBindVertexArray(this.getVAO());

        System.out.println(Arrays.toString(this.vertices));

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        //CRASH
        glBindVertexArray(0);
    }

    public abstract void update();

    public void updateVAO(){
        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        if(vertices != null) { // Vérifiez si le tableau vertices n'est pas nul
            System.out.println("Mise a jour VBO : " + vertices.length);
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        }
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());
        if(indices != null) { // Vérifiez si le tableau indices n'est pas nul
            System.out.println("Mise a jour EBO");
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        }
        System.out.println("Done!");
    }
    public void cleanup() {
        glDeleteBuffers(this.getVBO());
        glDeleteVertexArrays(this.getVAO());
    }

    public boolean hasTexture(){ return this.texture != null; }


}
