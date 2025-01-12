package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components;

import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GuiModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderUtils;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.TextureUtils;
import lombok.Getter;
import lombok.Setter;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.Game.GUI_ZOOM;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

@Getter
@Setter
public abstract class Component{

    private int VBO, EBO, VAO;


    private int x;
    private int y;
    private final int width;
    private final int heigth;
    private Texture texture;
    private Gui container;
    private GuiModule guiModule;

    private float[] vertices;
    private boolean isInitialized = false;

    // Indices pour dessiner un quad avec deux triangles
    private int[] indices;

    public Component(int x, int y, int width, int heigth, Texture texture, Gui container){

        this.x = x;
        this.y = y;
        this.width = GUI_ZOOM*width;
        this.heigth = GUI_ZOOM*heigth;
        this.texture = texture;
        this.container = container;
        this.guiModule = GAME.getGraphicModule().getGuiModule();

        updateVertices();
    }


    public boolean hasContainer(){ return this.container != null; }

    public int getX(){ return this.x*((this instanceof Gui) ? 1 : GUI_ZOOM) + ((this.hasContainer()) ? this.container.getX() : 0); }

    public int getY(){ return this.y*((this instanceof Gui) ? 1 : GUI_ZOOM) + ((this.hasContainer()) ? this.container.getY() : 0); }

    public boolean isCursorIn(){
        int x = getGuiModule().getCursorX();
        int y = getGuiModule().getCursorY();
        return x >= this.getX() && x < this.getX() + width && y >= this.getY() && y < this.getY() + heigth;
    }

    public void set2DTexture(Texture texture) {
        this.texture = texture;
        updateVertices();
    }

    public void set2DCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
        updateVertices();
    }

    public void updateVertices(){
        if(texture == null) return;

        int x = this.getX();
        int y = this.getY();

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

        if(isInitialized) updateVAO();
    }

    public void init() {
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();

        glUseProgram(this.getShader());
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

        updateVAO();
        this.isInitialized = true;
    }

    public void render() {
        if(!isInitialized) this.init();

        update();
        if(indices == null || vertices == null) return;
        glUseProgram(this.getShader());
        glBindVertexArray(this.getVAO());
        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());
//        if(this instanceof TextComponent) {
//            glBindTexture(GL_TEXTURE_2D, this.getTextureArray());
//        }

        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public int getShader() {
        return ShaderUtils.PLAN_SHADERS.id;
    }
    public int getTextureArray() { return TextureUtils.GUIS_TEXTURES; }

    public abstract void update();

    public void updateVAO(){
        glUseProgram(this.getShader());
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
