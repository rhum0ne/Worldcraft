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
        this(ObjData.getVertices(obj),
             ObjData.getNormals(obj),
             ObjData.getTexCoords(obj, 2),
             ObjData.getFaceVertexIndices(obj));
    }

    public Mesh(FloatBuffer verticesBuffer, FloatBuffer normalsBuffer,
                FloatBuffer texCoordsBuffer, IntBuffer indicesBuffer){
        this.verticesBuffer = verticesBuffer;
        this.normalsBuffer = normalsBuffer;
        this.texCoordsBuffer = texCoordsBuffer;
        this.indicesBuffer = indicesBuffer;
    }
}