package fr.rhumun.game.worldcraftopengl.controls.event;

import fr.rhumun.game.worldcraftopengl.Camera;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

public class CursorEvent implements GLFWCursorPosCallbackI {

    private float lastX = 600.0f, lastY = 400.0f;  // Centre de l'écran
    private boolean firstMouse = true;

    private final Camera camera;

    public CursorEvent(Camera cam){
        this.camera = cam;
    }

    @Override
    public void invoke(long window, double xpos, double ypos) {
        if (firstMouse) {
            lastX = (float) xpos;
            lastY = (float) ypos;
            firstMouse = false;
        }

        float xOffset = (float) xpos - lastX;
        float yOffset = lastY - (float) ypos;  // Inverser l'offset vertical pour correspondre aux conventions OpenGL

        lastX = (float) xpos;
        lastY = (float) ypos;

        // Appel à la méthode pour ajuster la caméra en fonction des mouvements de la souris
        processMouseMovement(xOffset, yOffset);
    }


    // Lorsqu'un mouvement de souris est détecté, ajustez les angles de la caméra
    public void processMouseMovement(float xOffset, float yOffset) {
        float sensitivity = 0.1f;  // Sensibilité de la souris
        xOffset *= sensitivity;
        yOffset *= sensitivity;

        camera.setYaw(camera.getYaw() + xOffset);   // Modifier le "yaw" avec l'offset horizontal
        camera.setPitch(camera.getPitch() + yOffset);  // Modifier le "pitch" avec l'offset vertical
    }
}
