package fr.rhumun.game.worldcraftopengl.content.models.entities;

import org.lwjgl.assimp.*;

import java.io.File;

public class RockyAnimationTest {

    public static void main(String[] args) {
        String path = "E:\\Devellopement\\Games\\Worldcraft\\src\\main\\resources\\assets\\models\\Rocky.gltf"; // adapte si nécessaire

        AIScene scene = Assimp.aiImportFile(
                path,
                Assimp.aiProcess_Triangulate | Assimp.aiProcess_JoinIdenticalVertices
        );

        if (scene == null || scene.mNumAnimations() == 0) {
            System.err.println("❌ Aucun animation trouvée.");
            return;
        }

        System.out.println("✔️ Animations trouvées : " + scene.mNumAnimations());

        for (int i = 0; i < scene.mNumAnimations(); i++) {
            AIAnimation animation = AIAnimation.create(scene.mAnimations().get(i));
            String name = animation.mName().dataString();
            System.out.println("- Animation " + i + " : " + name);
            System.out.println("  -> Channels : " + animation.mNumChannels());

            for (int j = 0; j < animation.mNumChannels(); j++) {
                AINodeAnim channel = AINodeAnim.create(animation.mChannels().get(j));
                String boneName = channel.mNodeName().dataString();
                System.out.println("    • Bone animé : " + boneName);
            }
        }

        Assimp.aiReleaseImport(scene);
    }
}

