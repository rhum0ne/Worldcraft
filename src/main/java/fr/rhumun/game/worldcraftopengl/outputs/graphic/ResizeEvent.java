package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;

import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL20.*;

public class ResizeEvent implements GLFWFramebufferSizeCallbackI {

    private final GraphicModule graphicModule;

    public ResizeEvent(GraphicModule graphicModule){
        this.graphicModule = graphicModule;
    }
    @Override
    public void invoke(long window, int width, int height) {
        glViewport(0, 0, width, height);

        // Recalculer la matrice de projection
        graphicModule.projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(45.0f), (float) width / height, 0.1f, 100.0f);

        // Mettre à jour la matrice de projection dans le shader
        glUseProgram(GraphicModule.shaders);
        int projectionLoc = glGetUniformLocation(GraphicModule.shaders, "projection");
        glUniformMatrix4fv(projectionLoc, false, graphicModule.projectionMatrix.get(new float[16]));
    }
}