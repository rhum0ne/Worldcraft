package fr.rhumun.game.worldcraftopengl.content.models.entities;

import fr.rhumun.game.worldcraftopengl.Game;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.assimp.*;

import java.util.*;

public class GltfAnimationLoader {

    public static record Result(Map<String, Bone> bones, Map<String, Animation> animations) {}

    public static Result load(String path) {
        path = Game.TEXTURES_PATH + path;

        AIScene scene = Assimp.aiImportFile(path,
                Assimp.aiProcess_Triangulate | Assimp.aiProcess_JoinIdenticalVertices);

        if (scene == null || scene.mAnimations() == null || scene.mNumAnimations() == 0) {
            System.err.println("❌ Aucun animation trouvée dans : " + path);
            return null;
        }

        Map<String, Bone> bones = new LinkedHashMap<>();
        Map<String, Animation> animations = new LinkedHashMap<>();

        processNodes(scene.mRootNode(), null, bones);

        // Récupérer les matrices d'offset depuis les meshes
        for (int m = 0; m < scene.mNumMeshes(); m++) {
            AIMesh mesh = AIMesh.create(scene.mMeshes().get(m));
            PointerBuffer aiBones = mesh.mBones();
            for (int i = 0; i < mesh.mNumBones(); i++) {
                AIBone aiBone = AIBone.create(aiBones.get(i));
                String name = aiBone.mName().dataString();
                Bone b = bones.computeIfAbsent(name, n -> new Bone(n, bones.size()));
                AIMatrix4x4 mat = aiBone.mOffsetMatrix();
                b.offsetMatrix.set(
                        mat.a1(), mat.b1(), mat.c1(), mat.d1(),
                        mat.a2(), mat.b2(), mat.c2(), mat.d2(),
                        mat.a3(), mat.b3(), mat.c3(), mat.d3(),
                        mat.a4(), mat.b4(), mat.c4(), mat.d4()
                );
            }
        }

        for (int a = 0; a < scene.mNumAnimations(); a++) {
            AIAnimation animation = AIAnimation.create(scene.mAnimations().get(a));
            String animName = animation.mName().dataString();
            if (animName == null || animName.isEmpty()) animName = "anim" + a;
            List<AnimationChannel<?>> animChannels = new ArrayList<>();

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
                    animChannels.add(new AnimationChannel<>(boneName, "translation", posKeys));

                AIQuatKey.Buffer rotations = channel.mRotationKeys();
                List<Keyframe<Quaternionf>> rotKeys = new ArrayList<>();
                for (int k = 0; k < channel.mNumRotationKeys(); k++) {
                    AIQuatKey key = rotations.get(k);
                    rotKeys.add(new Keyframe<>((float) key.mTime(),
                            new Quaternionf(key.mValue().x(), key.mValue().y(), key.mValue().z(), key.mValue().w())));
                }
                if (!rotKeys.isEmpty())
                    animChannels.add(new AnimationChannel<>(boneName, "rotation", rotKeys));

                AIVectorKey.Buffer scales = channel.mScalingKeys();
                List<Keyframe<Vector3f>> scaleKeys = new ArrayList<>();
                for (int k = 0; k < channel.mNumScalingKeys(); k++) {
                    AIVectorKey key = scales.get(k);
                    scaleKeys.add(new Keyframe<>((float) key.mTime(),
                            new Vector3f(key.mValue().x(), key.mValue().y(), key.mValue().z())));
                }
                if (!scaleKeys.isEmpty())
                    animChannels.add(new AnimationChannel<>(boneName, "scale", scaleKeys));
            }

            animations.put(animName, new Animation(animName, (float) animation.mDuration(), (float) animation.mTicksPerSecond(), animChannels));
        }

        Assimp.aiReleaseImport(scene);
        return new Result(bones, animations);
    }

    private static void processNodes(AINode node, Bone parent, Map<String, Bone> bones) {
        String name = node.mName().dataString();
        Bone bone = bones.computeIfAbsent(name, n -> new Bone(n, bones.size()));
        bone.parent = parent;
        if (parent != null) parent.children.add(bone);

        AIMatrix4x4 m = node.mTransformation();
        bone.setBindMatrix(new Matrix4f(
                m.a1(), m.b1(), m.c1(), m.d1(),
                m.a2(), m.b2(), m.c2(), m.d2(),
                m.a3(), m.b3(), m.c3(), m.d3(),
                m.a4(), m.b4(), m.c4(), m.d4()
        ));

        if (node.mChildren() != null) {
            for (int i = 0; i < node.mNumChildren(); i++) {
                AINode child = AINode.create(node.mChildren().get(i));
                processNodes(child, bone, bones);
            }
        }
    }
}
