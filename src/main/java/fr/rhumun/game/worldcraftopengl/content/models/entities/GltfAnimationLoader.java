package fr.rhumun.game.worldcraftopengl.content.models.entities;

import fr.rhumun.game.worldcraftopengl.Game;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.assimp.*;

import java.util.*;

public class GltfAnimationLoader {

    public static record Result(Map<String, Bone> bones, List<AnimationChannel<?>> channels) {}

    public static Result load(String path) {
        path = Game.TEXTURES_PATH + path;

        AIScene scene = Assimp.aiImportFile(path,
                Assimp.aiProcess_Triangulate | Assimp.aiProcess_JoinIdenticalVertices);

        if (scene == null || scene.mAnimations() == null || scene.mNumAnimations() == 0) {
            System.err.println("❌ Aucun animation trouvée dans : " + path);
            return null;
        }

        Map<String, Bone> bones = new HashMap<>();
        List<AnimationChannel<?>> channels = new ArrayList<>();

        // Créer les bones à partir du mesh pour conserver les mêmes indices
        if (scene.mNumMeshes() > 0) {
            AIMesh mesh = AIMesh.create(scene.mMeshes().get(0));
            PointerBuffer aiBones = mesh.mBones();
            for (int i = 0; i < mesh.mNumBones(); i++) {
                AIBone aiBone = AIBone.create(aiBones.get(i));
                String name = aiBone.mName().dataString();
                Bone bone = bones.computeIfAbsent(name, n -> new Bone(n, i));

                Matrix4f offset = toMatrix(aiBone.mOffsetMatrix());
                Matrix4f base = new Matrix4f(offset).invert();
                Vector3f t = new Vector3f();
                Quaternionf r = new Quaternionf();
                Vector3f s = new Vector3f();
                base.getTranslation(t);
                base.getUnnormalizedRotation(r);
                base.getScale(s);
                bone.setTranslation(t);
                bone.setRotation(r);
                bone.setScale(s);
            }
        }

        // Construction de la hiérarchie des bones
        buildHierarchy(scene.mRootNode(), null, bones);

        AIAnimation animation = AIAnimation.create(scene.mAnimations().get(0)); // on ne traite que la première

        for (int i = 0; i < animation.mNumChannels(); i++) {
            AINodeAnim channel = AINodeAnim.create(animation.mChannels().get(i));
            String boneName = channel.mNodeName().dataString();

            // Créer le bone si pas encore présent
            bones.computeIfAbsent(boneName, name -> new Bone(name, bones.size()));

            // Position
            AIVectorKey.Buffer positions = channel.mPositionKeys();
            List<Keyframe<Vector3f>> posKeys = new ArrayList<>();
            for (int k = 0; k < channel.mNumPositionKeys(); k++) {
                AIVectorKey key = positions.get(k);
                posKeys.add(new Keyframe<>((float) key.mTime(),
                        new Vector3f(key.mValue().x(), key.mValue().y(), key.mValue().z())));
            }
            if (!posKeys.isEmpty())
                channels.add(new AnimationChannel<>(boneName, "translation", posKeys));

            // Rotation
            AIQuatKey.Buffer rotations = channel.mRotationKeys();
            List<Keyframe<Quaternionf>> rotKeys = new ArrayList<>();
            for (int k = 0; k < channel.mNumRotationKeys(); k++) {
                AIQuatKey key = rotations.get(k);
                rotKeys.add(new Keyframe<>((float) key.mTime(),
                        new Quaternionf(key.mValue().x(), key.mValue().y(), key.mValue().z(), key.mValue().w())));
            }
            if (!rotKeys.isEmpty())
                channels.add(new AnimationChannel<>(boneName, "rotation", rotKeys));

            // Scale
            AIVectorKey.Buffer scales = channel.mScalingKeys();
            List<Keyframe<Vector3f>> scaleKeys = new ArrayList<>();
            for (int k = 0; k < channel.mNumScalingKeys(); k++) {
                AIVectorKey key = scales.get(k);
                scaleKeys.add(new Keyframe<>((float) key.mTime(),
                        new Vector3f(key.mValue().x(), key.mValue().y(), key.mValue().z())));
            }
            if (!scaleKeys.isEmpty())
                channels.add(new AnimationChannel<>(boneName, "scale", scaleKeys));
        }

        Assimp.aiReleaseImport(scene);
        return new Result(bones, channels);
    }

    private static Matrix4f toMatrix(AIMatrix4x4 aiMat) {
        return new Matrix4f(
                aiMat.a1(), aiMat.a2(), aiMat.a3(), aiMat.a4(),
                aiMat.b1(), aiMat.b2(), aiMat.b3(), aiMat.b4(),
                aiMat.c1(), aiMat.c2(), aiMat.c3(), aiMat.c4(),
                aiMat.d1(), aiMat.d2(), aiMat.d3(), aiMat.d4()
        );
    }

    private static void buildHierarchy(AINode node, Bone parent, Map<String, Bone> bones) {
        String name = node.mName().dataString();
        Bone current = bones.computeIfAbsent(name, n -> new Bone(n, bones.size()));
        current.parent = parent;
        if (parent != null) parent.children.add(current);

        Matrix4f transform = toMatrix(node.mTransformation());
        Vector3f t = new Vector3f();
        Quaternionf r = new Quaternionf();
        Vector3f s = new Vector3f();
        transform.getTranslation(t);
        transform.getUnnormalizedRotation(r);
        transform.getScale(s);
        current.setTranslation(t);
        current.setRotation(r);
        current.setScale(s);

        if (node.mNumChildren() > 0) {
            PointerBuffer children = node.mChildren();
            for (int i = 0; i < node.mNumChildren(); i++) {
                buildHierarchy(AINode.create(children.get(i)), current, bones);
            }
        }
    }
}
