package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils;

import static org.lwjgl.opengl.GL11C.*;

/**
 * Utility class to reduce redundant OpenGL state changes.
 */
public final class GLStateManager {
    private static int currentProgram = 0;
    private static boolean blend = false;
    private static boolean depthTest = false;
    private static boolean cullFace = false;

    private GLStateManager() {}

    public static void useProgram(int program) {
        if (currentProgram != program) {
            glUseProgram(program);
            currentProgram = program;
        }
    }

    public static void enable(int flag) {
        switch (flag) {
            case GL_BLEND -> {
                if (!blend) {
                    glEnable(GL_BLEND);
                    blend = true;
                }
            }
            case GL_DEPTH_TEST -> {
                if (!depthTest) {
                    glEnable(GL_DEPTH_TEST);
                    depthTest = true;
                }
            }
            case GL_CULL_FACE -> {
                if (!cullFace) {
                    glEnable(GL_CULL_FACE);
                    cullFace = true;
                }
            }
            default -> glEnable(flag);
        }
    }

    public static void disable(int flag) {
        switch (flag) {
            case GL_BLEND -> {
                if (blend) {
                    glDisable(GL_BLEND);
                    blend = false;
                }
            }
            case GL_DEPTH_TEST -> {
                if (depthTest) {
                    glDisable(GL_DEPTH_TEST);
                    depthTest = false;
                }
            }
            case GL_CULL_FACE -> {
                if (cullFace) {
                    glDisable(GL_CULL_FACE);
                    cullFace = false;
                }
            }
            default -> glDisable(flag);
        }
    }

    /** Reset tracked state. */
    public static void reset() {
        currentProgram = 0;
        blend = depthTest = cullFace = false;
    }
}
