package fr.rhumun.game.worldcraftopengl.content.models.entities;

import fr.rhumun.game.worldcraftopengl.Game;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;
            long nodePtr = MemoryUtil.memGetAddress(aiBone.address() + AIBone.MNODE);

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
        Map<String, AINode> nodeMap = new HashMap<>();
        List<AnimationChannel<?>> channels = new ArrayList<>();

        // Bones and inverse bind matrices
        AIMesh aiMesh = AIMesh.create(scene.mMeshes().get(0));
        PointerBuffer aiBones = aiMesh.mBones();
        for (int i = 0; i < aiMesh.mNumBones(); i++) {
            AIBone aiBone = AIBone.create(aiBones.get(i));
            String name = aiBone.mName().dataString();
            Bone bone = bones.computeIfAbsent(name, n -> new Bone(n, bones.size()));
            // Inverse bind matrix
            AIMatrix4x4 off = aiBone.mOffsetMatrix();
            bone.inverseBindMatrix.set(
                off.a1(), off.a2(), off.a3(), off.a4(),
                off.b1(), off.b2(), off.b3(), off.b4(),
                off.c1(), off.c2(), off.c3(), off.c4(),
                off.d1(), off.d2(), off.d3(), off.d4());

            long nodePtr = AIBone.nmNode(aiBone.address());
            AINode node = AINode.createSafe(nodePtr);
            nodeMap.put(name, node);
            if (node != null) {
                AIMatrix4x4 nTrans = node.mTransformation();
                Matrix4f mat = new Matrix4f(
                        nTrans.a1(), nTrans.a2(), nTrans.a3(), nTrans.a4(),
                        nTrans.b1(), nTrans.b2(), nTrans.b3(), nTrans.b4(),
                        nTrans.c1(), nTrans.c2(), nTrans.c3(), nTrans.c4(),
                        nTrans.d1(), nTrans.d2(), nTrans.d3(), nTrans.d4());
                Vector3f t = new Vector3f();
                Quaternionf r = new Quaternionf();
                Vector3f s = new Vector3f();
                mat.getTranslation(t);
                mat.getUnnormalizedRotation(r);
                mat.getScale(s);
                bone.setTranslation(t);
                bone.setRotation(r);
                bone.setScale(s);
            }
        }

        // Establish hierarchy
        for (Map.Entry<String, AINode> entry : nodeMap.entrySet()) {
            String name = entry.getKey();
            AINode node = entry.getValue();
            if (node == null) continue;
            AINode parent = node.mParent();
            if (parent != null) {
                String parentName = parent.mName().dataString();
                Bone child = bones.get(name);
                Bone par = bones.get(parentName);
                if (child != null && par != null) {
                    child.parent = par;
                    par.children.add(child);
                }
            }
        }

        // Animation channels (only first animation)
        AIAnimation animation = AIAnimation.create(scene.mAnimations().get(0));

        for (int i = 0; i < animation.mNumChannels(); i++) {
            AINodeAnim channel = AINodeAnim.create(animation.mChannels().get(i));
            String boneName = channel.mNodeName().dataString();

            bones.computeIfAbsent(boneName, name -> new Bone(name, bones.size()));

            AIVectorKey.Buffer positions = channel.mPositionKeys();
            List<Keyframe<Vector3f>> posKeys = new ArrayList<>();
            for (int k = 0; k < channel.mNumPositionKeys(); k++) {
                AIVectorKey key = positions.get(k);
                posKeys.add(new Keyframe<>((float) key.mTime(),
                        new Vector3f(key.mValue().x(), key.mValue().y(), key.mValue().z())));
            }
            if (!posKeys.isEmpty())
                channels.add(new AnimationChannel<>(boneName, "translation", posKeys));

            AIQuatKey.Buffer rotations = channel.mRotationKeys();
            List<Keyframe<Quaternionf>> rotKeys = new ArrayList<>();
            for (int k = 0; k < channel.mNumRotationKeys(); k++) {
                AIQuatKey key = rotations.get(k);
                rotKeys.add(new Keyframe<>((float) key.mTime(),
                        new Quaternionf(key.mValue().x(), key.mValue().y(), key.mValue().z(), key.mValue().w())));
            }
            if (!rotKeys.isEmpty())
                channels.add(new AnimationChannel<>(boneName, "rotation", rotKeys));

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
}
