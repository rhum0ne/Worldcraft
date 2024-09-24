package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import org.lwjgl.glfw.GLFWErrorCallback;
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

    @Override
    public void start(){
        super.start();
    }

//    @Override
//    public void run() {
//// Initialize GLFW
//        if (!glfwInit()) {
//            throw new IllegalStateException("Unable to initialize GLFW");
//        }
//        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
//
//// Configure OpenGL version hints
//        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
//        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
//        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
//        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);  // Needed for MacOS
//
//// Ensure that the main window's context is created and active
////        glfwMakeContextCurrent(graphicModule.getWindow());
////        GL.createCapabilities();  // Creates OpenGL capabilities for the main window
//
//// Create the shared OpenGL context in a dummy window
//        sharedWindow = glfwCreateWindow(1, 1, "Shared Window", NULL, graphicModule.getWindow());
//        if (sharedWindow == NULL) {
//            throw new RuntimeException("Failed to create the shared OpenGL context");
//        }
//
//// Make sure the context is current on this thread
//        glfwMakeContextCurrent(sharedWindow);
//        GL.createCapabilities();  // Creates OpenGL capabilities for the shared context
//
//// Main loop
//        while (!glfwWindowShouldClose(sharedWindow)) {
//            graphicModule.updateViewMatrix();
//        }
//
//    }

    public void run(){
        graphicModule.updateViewMatrix();
    }
}
