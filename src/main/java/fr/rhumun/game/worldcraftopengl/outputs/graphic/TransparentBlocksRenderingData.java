package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TransparentBlocksRenderingData {
    private final List<float[]> vertices = new ArrayList<>();
    private float[] verticesArray = new float[0];

    //private final List<Integer> indices = new ArrayList<>();
    private int indice;
    int[] indicesArray = new int[0];

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
}
