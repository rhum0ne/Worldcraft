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
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
            Controls.LEFT_CLICK.press(game.getPlayer());
        }
        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_PRESS) {
            Controls.RIGHT_CLICK.press(game.getPlayer());
        }
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
            Controls.LEFT_CLICK.release(game.getPlayer());
        }
    }
}
