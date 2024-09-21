package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import lombok.Getter;

import java.nio.FloatBuffer;

/**
 * MeshArrays keeps all necessary arrays needed to be drawn with openGL.
 */
@Getter
public class MeshArrays {

    private final FloatBuffer vertices;
    private final FloatBuffer normals;
    private final FloatBuffer texCoords;

    private final int numVertices;

    private float minX;
    private float minY;
    private float minZ;

    private float maxX;
    private float maxY;
    private float maxZ;

    public MeshArrays(FloatBuffer vertices, FloatBuffer normals, FloatBuffer texCoords, int numVertices, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.vertices = vertices;
        this.normals = normals;
        this.texCoords = texCoords;
        this.numVertices = numVertices;

        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;

    }
}