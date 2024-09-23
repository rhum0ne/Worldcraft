package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import org.lwjgl.opengl.GL;

import java.util.TimerTask;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

public class UpdateLoop extends Thread {

    GraphicModule graphicModule;
    private long sharedWindow;

    public UpdateLoop(GraphicModule graphicModule){
        this.graphicModule = graphicModule;
    }
    /*@Override
    public void run() {
        // Assurez-vous que GLFW est initialisé
        glfwMakeContextCurrent(graphicModule.getWindow());

        // Définissez les hints pour la version d'OpenGL
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE); // Pour MacOS

        // Créez la fenêtre partagée
        sharedWindow = glfwCreateWindow(1, 1, "Shared Window", NULL, graphicModule.getWindow());
        if (sharedWindow == NULL) {
            throw new RuntimeException("Failed to create the shared OpenGL context");
        }

        // Assurez-vous que le contexte est activé sur le thread actuel
        glfwMakeContextCurrent(sharedWindow);
        GL.createCapabilities();  // Crée les capacités OpenGL pour le contexte partagé

        while(!glfwWindowShouldClose(sharedWindow)) {
            graphicModule.updateViewMatrix();
        }
    }*/

    public void run(){
        graphicModule.updateViewMatrix();
    }
}
