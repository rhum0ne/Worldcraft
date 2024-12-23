package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.Player;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.TimerTask;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

public class UpdateLoop extends Thread {

    private GraphicModule graphicModule;
    private Game game;
    private Player player;

    public UpdateLoop(GraphicModule graphicModule, Game game, Player player){
        this.graphicModule = graphicModule;
        this.game = game;
        this.player = player;
    }

    @Override
    public void start(){
        super.start();
    }

    public void run(){
        if(game.getGraphicModule() == null) return;

        //player.getSavedChunksManager().tryLoadChunks();
        game.getGraphicModule().updateViewMatrix();
    }
}
