package fr.rhumun.game.worldcraftopengl.content.models.entities;

import fr.rhumun.game.worldcraftopengl.Game;
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
}
