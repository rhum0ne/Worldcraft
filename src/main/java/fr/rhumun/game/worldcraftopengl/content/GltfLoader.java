package fr.rhumun.game.worldcraftopengl.content;

import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import fr.rhumun.game.worldcraftopengl.Game;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

/**
 * Utility class responsible for loading glTF files and converting them into
 * AnimatedMesh instances. The actual extraction of vertex data and animations
 * is not fully implemented but the skeleton of the loader is provided for
 * future developments.
 */
public class GltfLoader {

    public static AnimatedMesh loadAnimatedMesh(String fileName) {
        // Until proper glTF parsing is implemented we fall back to the OBJ
        // counterpart so meshes continue to load without crashing.
        String objName = fileName.replace(".gltf", ".obj");

        try {
            var obj = ObjUtils.convertToRenderable(
                    ObjReader.read(new FileInputStream(Game.TEXTURES_PATH +
                            "models/" + objName)));

            FloatBuffer vertices = ObjData.getVertices(obj).duplicate();
            FloatBuffer normals = ObjData.getNormals(obj).duplicate();
            FloatBuffer texCoords = ObjData.getTexCoords(obj, 2).duplicate();
            IntBuffer indices = ObjData.getFaceVertexIndices(obj).duplicate();

            int vertexCount = vertices.capacity() / 3;
            FloatBuffer boneIndices = BufferUtils.createFloatBuffer(vertexCount * 4);
            FloatBuffer boneWeights = BufferUtils.createFloatBuffer(vertexCount * 4);
            // Populate weights so that the fallback mesh is rendered at
            // its original position when used with the skinned shader.
            for (int i = 0; i < vertexCount; i++) {
                boneIndices.put(i * 4, 0f);
                boneWeights.put(i * 4, 1f);
            }

            return new AnimatedMesh(vertices, normals, texCoords, indices,
                    boneIndices, boneWeights, 0);
        } catch (IOException e) {
            Game.GAME.errorLog(e);
            return null;
        }
    }
}
