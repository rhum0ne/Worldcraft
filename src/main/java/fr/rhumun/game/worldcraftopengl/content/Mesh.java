package fr.rhumun.game.worldcraftopengl.content;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static fr.rhumun.game.worldcraftopengl.Game.TEXTURES_PATH;
import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memAllocInt;

@Getter
public class Mesh {

    private final FloatBuffer verticesBuffer;
    private final FloatBuffer normalsBuffer;
    private final FloatBuffer texCoordsBuffer;
    private final IntBuffer indicesBuffer;

    @Setter
    private IntBuffer boneIDsBuffer;

    public Mesh(final Obj obj){
        this.verticesBuffer = ObjData.getVertices(obj);
        this.normalsBuffer = ObjData.getNormals(obj);
        this.texCoordsBuffer = ObjData.getTexCoords(obj, 2);
        this.indicesBuffer = ObjData.getFaceVertexIndices(obj);
    }

    public static Mesh loadSkinnedMesh(String path) {
        System.out.println("Chargement du mesh : " + path);
        path = TEXTURES_PATH + path;

        AIScene scene = aiImportFile(path, aiProcess_Triangulate | aiProcess_JoinIdenticalVertices);
        if (scene == null || scene.mNumMeshes() == 0){
            System.err.println("❌ Impossible de charger le mesh : " + path + " : No scene");
            return null;
        }

        AIMesh aiMesh = AIMesh.create(scene.mMeshes().get(0));

        int vertexCount = aiMesh.mNumVertices();
        FloatBuffer vBuf = memAllocFloat(vertexCount * 3);
        FloatBuffer nBuf = memAllocFloat(vertexCount * 3);
        FloatBuffer tBuf = memAllocFloat(vertexCount * 2);
        IntBuffer iBuf = memAllocInt(aiMesh.mNumFaces() * 3);
        IntBuffer bBuf = memAllocInt(vertexCount);

        // Initialiser tous les boneIDs à 0
        for (int i = 0; i < vertexCount; i++) bBuf.put(0);
        bBuf.rewind();

        // Pos, Normals, TexCoords
        AIVector3D.Buffer vertices = aiMesh.mVertices();
        AIVector3D.Buffer normals = aiMesh.mNormals();
        AIVector3D.Buffer texCoords = aiMesh.mTextureCoords(0);

        for (int i = 0; i < vertexCount; i++) {
            AIVector3D v = vertices.get(i);
            AIVector3D n = normals.get(i);
            vBuf.put(v.x()).put(v.y()).put(v.z());
            nBuf.put(n.x()).put(n.y()).put(n.z());
            if (texCoords != null) {
                AIVector3D t = texCoords.get(i);
                tBuf.put(t.x()).put(t.y());
            } else {
                tBuf.put(0).put(0);
            }
        }
        vBuf.flip(); nBuf.flip(); tBuf.flip();

        // Indices
        AIFace.Buffer faces = aiMesh.mFaces();
        for (int i = 0; i < faces.capacity(); i++) {
            AIFace face = faces.get(i);
            iBuf.put(face.mIndices());
        }
        iBuf.flip();

        // Bones
        int numBones = aiMesh.mNumBones();
        System.out.println("Nombre de bones : " + aiMesh.mNumBones());

        PointerBuffer aiBones = aiMesh.mBones();
        for (int i = 0; i < numBones; i++) {
            AIBone bone = AIBone.create(aiBones.get(i));
            int boneIndex = i;
            AIVertexWeight.Buffer weights = bone.mWeights();
            System.out.println("Bone: " + bone.mName().dataString());
            for (int j = 0; j < bone.mNumWeights(); j++) {
                AIVertexWeight vw = weights.get(j);
                int vertexId = vw.mVertexId();
                bBuf.put(vertexId, boneIndex);
                System.out.println("  Influences vertex: " + vw.mVertexId() + " weight: " + vw.mWeight());
            }
        }

        bBuf.rewind();

        Mesh mesh = new Mesh(vBuf, nBuf, tBuf, iBuf);
        mesh.setBoneIDsBuffer(bBuf);
        aiReleaseImport(scene);

        return mesh;
    }

    public Mesh(FloatBuffer verticesBuffer, FloatBuffer normalsBuffer, FloatBuffer texCoordsBuffer, IntBuffer indicesBuffer) {
        this.verticesBuffer = verticesBuffer;
        this.normalsBuffer = normalsBuffer;
        this.texCoordsBuffer = texCoordsBuffer;
        this.indicesBuffer = indicesBuffer;
    }
}
