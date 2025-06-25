package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import lombok.Getter;
import lombok.Setter;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.*;

@Getter
@Setter
public abstract class Renderer {
    GraphicModule graphicModule;
    private final Shader shader;
    private int VBO, EBO, VAO;

    private final ArrayList<float[]> vertices = new ArrayList<>();
    private float[] verticesArray = new float[0];

    private FloatBuffer verticesBuffer;

    private final ArrayList<Integer> indices = new ArrayList<>();
    private int indice;
    int[] indicesArray = new int[0];
    private IntBuffer indicesBuffer;

    public Renderer(GraphicModule graphicModule, Shader shader) {
        //System.out.println("Creating Renderer");
        this.graphicModule = graphicModule;
        //glBindVertexArray(this.graphicModule.VAO);
        this.shader = shader;
    }

    public void addIndice(){ indices.add((indices.isEmpty()) ? 0 : (indices.getLast()+1)); }

    public void addAllIndices(int[] indices){
        for(int i : indices) this.indices.add(i);
    }

    public void addRawIndices(int[] rawIndices){
        int start = (indices.isEmpty()) ? 0 : (indices.getLast()+1);

        for(int i : rawIndices){
            this.indices.add(start + i);
        }
    }

    public void toArrays(){
        if(vertices.isEmpty()){
            indicesArray = new int[0];
            verticesArray = new float[0];
            return;
        }

        verticesArray = new float[vertices.size()*vertices.getFirst().length];
        int index = 0;
        for (float[] vertex : vertices) {
            for (float v : vertex) {
                verticesArray[index++] = v;
                //if(j%5 == 0 && j!=0) System.out.println("Texture ID " + vertices.get(i)[j]);
            }
        }

        indicesArray = new int[indices.size()];
//        for (int i = 0; i < indice; i++) {
//            indicesArray[i] = i;
//        }
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
    }

    public void init(){
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();
    }

    protected void fillBuffers() {
        if(verticesBuffer == null || verticesBuffer.capacity() < verticesArray.length) {
            if(verticesBuffer != null) memFree(verticesBuffer);
            verticesBuffer = memAllocFloat(verticesArray.length);
        }
        verticesBuffer.clear();
        verticesBuffer.put(verticesArray).flip();

        if(indicesBuffer == null || indicesBuffer.capacity() < indicesArray.length) {
            if(indicesBuffer != null) memFree(indicesBuffer);
            indicesBuffer = memAllocInt(indicesArray.length);
        }
        indicesBuffer.clear();
        indicesBuffer.put(indicesArray).flip();
    }

    protected void freeBuffers() {
        if(verticesBuffer != null) {
            memFree(verticesBuffer);
            verticesBuffer = null;
        }
        if(indicesBuffer != null) {
            memFree(indicesBuffer);
            indicesBuffer = null;
        }
    }

    public abstract void render();
    public abstract void cleanup();

    public void addAllVertices(float[][] vertices) {
        this.vertices.addAll(Arrays.asList(vertices));
    }
}
