package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

@Getter
@Setter
public abstract class Renderer {
    GraphicModule graphicModule;
    private int VBO, EBO, VAO;

    private final List<float[]> vertices = new ArrayList<>();
    private float[] verticesArray = new float[0];

    //private final List<Integer> indices = new ArrayList<>();
    private int indice;
    int[] indicesArray = new int[0];

    public Renderer(GraphicModule graphicModule) {
        //System.out.println("Creating Renderer");
        this.graphicModule = graphicModule;
        //glBindVertexArray(this.graphicModule.VAO);
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();
    }

    public void addIndice(){ indice++; }

    public void toArrays(){
        verticesArray = new float[vertices.size()*10];
        int index = 0;
        for (float[] vertex : vertices) {
            for (float v : vertex) {
                verticesArray[index++] = v;
                //if(j%5 == 0 && j!=0) System.out.println("Texture ID " + vertices.get(i)[j]);
            }
        }

        indicesArray = new int[indice];
        for (int i = 0; i < indice; i++) {
            indicesArray[i] = i;
        }
    }

    public abstract void init();
    public abstract void render();
    public abstract void cleanup();
}
