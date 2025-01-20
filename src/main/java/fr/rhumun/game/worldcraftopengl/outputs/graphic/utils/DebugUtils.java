package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils;

import fr.rhumun.game.worldcraftopengl.Game;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.opengl.GL43C.GL_DEBUG_OUTPUT;
import static org.lwjgl.opengl.GL43C.glDebugMessageCallback;
import static org.lwjgl.system.MemoryUtil.NULL;

public class DebugUtils {
    private long lastTime;
    private int frames;

    public DebugUtils(){
        lastTime = System.nanoTime(); // Initialiser lastTime avec le temps actuel
    }


    public void setDebug(boolean state){
        if(state) {
            glEnable(GL_DEBUG_OUTPUT);
            glDebugMessageCallback((source, type, id, severity, length, message, userParam) -> {
                System.err.println("GL CALLBACK: " + GLDebugSeverityToString(severity) + " - " + GLDebugMessageTypeToString(type) + ": " + GLDebugSourceToString(source) + " - " + GLMessageToString(message, length));
            }, NULL);
        }else{
            glDisable(GL_DEBUG_OUTPUT);
        }
    }

    public void checkGLError() {
        int errorCode = GL11.glGetError();
        if (errorCode != GL11.GL_NO_ERROR) {
            String error = interpretGLError(errorCode);
            Game.GAME.errorLog("OpenGL Error: " + error);
        }
    }

    private String interpretGLError(int errorCode) {
        return switch (errorCode) {
            case GL11.GL_INVALID_ENUM -> "Invalid Enum";
            case GL11.GL_INVALID_VALUE -> "Invalid Value";
            case GL11.GL_INVALID_OPERATION -> "Invalid Operation";
            case GL11.GL_STACK_OVERFLOW -> "Stack Overflow";
            case GL11.GL_STACK_UNDERFLOW -> "Stack Underflow";
            case GL11.GL_OUT_OF_MEMORY -> "Out of Memory";
            default -> "Unknown Error (" + errorCode + ")";
        };
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



    public static String GLDebugSeverityToString(int severity) {
        return switch (severity) {
            case GL_DEBUG_SEVERITY_HIGH -> "HIGH";
            case GL_DEBUG_SEVERITY_MEDIUM -> "MEDIUM";
            case GL_DEBUG_SEVERITY_LOW -> "LOW";
            case GL_DEBUG_SEVERITY_NOTIFICATION -> "NOTIFICATION";
            default -> "UNKNOWN SEVERITY";
        };
    }

    public static String GLDebugMessageTypeToString(int type) {
        return switch (type) {
            case GL_DEBUG_TYPE_ERROR -> "ERROR";
            case GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR -> "DEPRECATED BEHAVIOR";
            case GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR -> "UNDEFINED BEHAVIOR";
            case GL_DEBUG_TYPE_PORTABILITY -> "PORTABILITY";
            case GL_DEBUG_TYPE_PERFORMANCE -> "PERFORMANCE";
            case GL_DEBUG_TYPE_MARKER -> "MARKER";
            case GL_DEBUG_TYPE_PUSH_GROUP -> "PUSH GROUP";
            case GL_DEBUG_TYPE_POP_GROUP -> "POP GROUP";
            case GL_DEBUG_TYPE_OTHER -> "OTHER";
            default -> "UNKNOWN TYPE";
        };
    }

    public static String GLDebugSourceToString(int source) {
        return switch (source) {
            case GL_DEBUG_SOURCE_API -> "API";
            case GL_DEBUG_SOURCE_WINDOW_SYSTEM -> "WINDOW SYSTEM";
            case GL_DEBUG_SOURCE_SHADER_COMPILER -> "SHADER COMPILER";
            case GL_DEBUG_SOURCE_THIRD_PARTY -> "THIRD PARTY";
            case GL_DEBUG_SOURCE_APPLICATION -> "APPLICATION";
            case GL_DEBUG_SOURCE_OTHER -> "OTHER";
            default -> "UNKNOWN SOURCE";
        };
    }

    public static String GLMessageToString(long message, int length) {
        return MemoryUtil.memUTF8(message, length);
    }
}
