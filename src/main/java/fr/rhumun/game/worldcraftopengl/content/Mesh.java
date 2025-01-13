package fr.rhumun.game.worldcraftopengl.content;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import lombok.Getter;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * MeshArrays keeps all necessary arrays needed to be drawn with openGL.
 */
@Getter
public class Mesh {

    private final FloatBuffer verticesBuffer;
    private final FloatBuffer normalsBuffer;
    private final FloatBuffer texCoordsBuffer;
    private final IntBuffer indicesBuffer;

    public Mesh(final Obj obj){
        this.verticesBuffer = ObjData.getVertices(obj);
        this.normalsBuffer = ObjData.getNormals(obj);
        this.texCoordsBuffer = ObjData.getTexCoords(obj, 2);
        this.indicesBuffer = ObjData.getFaceVertexIndices(obj);
    }
}