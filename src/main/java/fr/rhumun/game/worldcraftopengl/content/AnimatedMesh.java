package fr.rhumun.game.worldcraftopengl.content;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * AnimatedMesh extends Mesh by adding bone informations used for skinning.
 */
public class AnimatedMesh extends Mesh {

    private final FloatBuffer boneIndicesBuffer;
    private final FloatBuffer boneWeightsBuffer;
    private final int boneCount;

    public AnimatedMesh(FloatBuffer verticesBuffer, FloatBuffer normalsBuffer,
                        FloatBuffer texCoordsBuffer, IntBuffer indicesBuffer,
                        FloatBuffer boneIndicesBuffer, FloatBuffer boneWeightsBuffer,
                        int boneCount) {
        super(verticesBuffer, normalsBuffer, texCoordsBuffer, indicesBuffer);
        this.boneIndicesBuffer = boneIndicesBuffer;
        this.boneWeightsBuffer = boneWeightsBuffer;
        this.boneCount = boneCount;
    }

    public FloatBuffer getBoneIndicesBuffer() {
        return boneIndicesBuffer;
    }

    public FloatBuffer getBoneWeightsBuffer() {
        return boneWeightsBuffer;
    }

    public int getBoneCount() {
        return boneCount;
    }
}
