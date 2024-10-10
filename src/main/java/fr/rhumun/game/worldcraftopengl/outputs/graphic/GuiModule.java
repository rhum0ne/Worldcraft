package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.CrosshairRenderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.Hotbar;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.ItemsRenderer;
import org.lwjgl.nuklear.*;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.util.Collections;

import static org.lwjgl.bgfx.BGFXInit.ALLOCATOR;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

public class GuiModule {

    private GraphicModule graphicModule;

    private CrosshairRenderer crosshair;
    private Hotbar hotbar;
    private ItemsRenderer itemsRenderer;

    public GuiModule(GraphicModule graphicModule) {
        this.graphicModule = graphicModule;

        this.crosshair = new CrosshairRenderer(graphicModule);
        this.hotbar = new Hotbar(graphicModule);
        this.itemsRenderer = new ItemsRenderer(graphicModule);
    }

    public void init(){
        this.crosshair.init();
        this.hotbar.init();
        this.itemsRenderer.init();
    }

    public void render(){
        this.crosshair.render();
        this.hotbar.render();
        this.itemsRenderer.render();
    }

    public void cleanup(){
        this.crosshair.cleanup();
    }

    public void updateInventory(Player player){
        this.itemsRenderer.setItems(player.getInventory());
    }

}
