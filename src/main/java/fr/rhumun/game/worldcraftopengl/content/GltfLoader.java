package fr.rhumun.game.worldcraftopengl.content;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.io.GltfModelReader;
import fr.rhumun.game.worldcraftopengl.Game;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;

/**
 * Utility class responsible for loading glTF files and converting them into
 * AnimatedMesh instances. The actual extraction of vertex data and animations
 * is not fully implemented but the skeleton of the loader is provided for
 * future developments.
 */
public class GltfLoader {

    public static AnimatedMesh loadAnimatedMesh(String fileName) {
        Path path = Path.of(Game.TEXTURES_PATH + "models/" + fileName);
        try {
            GltfModelReader reader = new GltfModelReader();
            GltfModel model = reader.read(path.toFile().toURI());

            // TODO Extract buffers from the glTF model (positions, normals,
            //  texCoords, bone indices and weights, indices) and build the
            //  AnimatedMesh instance. This will also require parsing skins and
            //  animations to retrieve the bone hierarchy.
            FloatBuffer vertices = null;
            FloatBuffer normals = null;
            FloatBuffer texCoords = null;
            IntBuffer indices = null;
            FloatBuffer boneIndices = null;
            FloatBuffer boneWeights = null;
            int boneCount = 0;

            // Placeholder until proper extraction is implemented
            return new AnimatedMesh(vertices, normals, texCoords, indices,
                    boneIndices, boneWeights, boneCount);
        } catch (IOException e) {
            Game.GAME.errorLog(e);
            return null;
        }
    }
}
