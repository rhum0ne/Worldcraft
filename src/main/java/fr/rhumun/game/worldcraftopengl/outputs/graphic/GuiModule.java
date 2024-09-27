package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import org.lwjgl.nuklear.*;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

import static org.lwjgl.bgfx.BGFXInit.ALLOCATOR;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

public class GuiModule {

    private GraphicModule graphicModule;
    private long window;

    public void renderGui(){
        setup2D(800, 600);

        glDepthMask(false);
        glColor4f(1f, 0, 0, 1f);
        glBegin(GL_POINT);

        glVertex2f(0f, 0f);
        glEnd();
        glDepthMask(true);
    }

    public void setup2D(int width, int height) {
        // Activer la projection 2D
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        // Définir la projection orthographique (coordonnées 0,0 en haut à gauche)
        glOrtho(0, width, height, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // Désactiver le test de profondeur (UI doit être au-dessus du rendu 3D)
        //glDisable(GL_DEPTH_TEST);
    }


}
