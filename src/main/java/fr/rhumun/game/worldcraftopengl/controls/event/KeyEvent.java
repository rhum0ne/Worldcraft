package fr.rhumun.game.worldcraftopengl.controls.event;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.controls.Controls;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyEvent implements GLFWKeyCallbackI {

    private final Game game;
    private final Player player;

    public KeyEvent(Game game, Player player){
        this.game = game;
        this.player = player;
    }
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            List<Controls> pressedKeys = game.getPressedKeys();
            if(Controls.exists(key) && !pressedKeys.contains(Controls.get(key))){
                Controls control = Controls.get(key);
                pressedKeys.add(control);
            }
        }

        if (action == GLFW_RELEASE) {
            List<Controls> pressedKeys = game.getPressedKeys();
            if(Controls.exists(key)/* && pressedKeys.contains(Controls.get(key))*/){
                Controls control = Controls.get(key);
                pressedKeys.remove(control);
                control.release(player);
            }
        }
    }
}
