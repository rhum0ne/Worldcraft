package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils;

import org.lwjgl.opengl.GL11;

public class DebugUtils {
    private long lastTime;
    private int frames;

    public DebugUtils(){
        lastTime = System.nanoTime(); // Initialiser lastTime avec le temps actuel
    }

    public void checkGLError() {
        int error = GL11.glGetError();
        while (error != GL11.GL_NO_ERROR) {
            System.err.println("OpenGL error: " + error);
            error = GL11.glGetError();
        }
    }

    public void calculateFPS() {
        long currentTime = System.nanoTime();
        long deltaTime = currentTime - lastTime;

        frames++;

        if (deltaTime >= 1_000_000_000L) { // 1 seconde en nanosecondes
            int fps = frames;
            frames = 0;
            lastTime = currentTime;

            // Afficher les FPS ou utiliser les FPS comme vous le souhaitez
            System.out.println("FPS: " + fps);
        }
    }
}
