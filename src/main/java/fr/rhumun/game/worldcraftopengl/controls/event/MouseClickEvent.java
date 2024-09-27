package fr.rhumun.game.worldcraftopengl.controls.event;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.controls.Controls;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

public class MouseClickEvent implements GLFWMouseButtonCallbackI {

    private final Game game;

    public MouseClickEvent(Game game){
        this.game = game;
    }

    @Override
    public void invoke(long window, int button, int action, int mods) {
        //System.out.println("Mouse button: " + button);
        if (action == GLFW.GLFW_PRESS) {
            Controls.get(button).press(game.getPlayer());
        }
        else if (action == GLFW.GLFW_RELEASE) {
            Controls.get(button).release(game.getPlayer());
        }
    }
}
