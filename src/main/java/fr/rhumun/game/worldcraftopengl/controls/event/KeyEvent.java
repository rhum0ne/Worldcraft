package fr.rhumun.game.worldcraftopengl.controls.event;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.controls.Controls;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.ChatGui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GuiModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.TypingGui;
import org.lwjgl.glfw.GLFW;
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

            GuiModule guiModule = game.getGraphicModule().getGuiModule();
            if(guiModule.hasGUIOpened() && guiModule.getGui() instanceof TypingGui tg
                    && (!Controls.exists(key) || (Controls.get(key) != Controls.ENTER && Controls.get(key) != Controls.ESCAPE))) {
                if(key == GLFW.GLFW_KEY_BACKSPACE || key == GLFW.GLFW_KEY_DELETE) {
                    tg.typeChar('\b');
                }
                return;
            }

            ChatGui chat = guiModule.getChat();
            if(chat.isShowed() && (!Controls.exists(key) || (Controls.get(key) != Controls.ENTER && Controls.get(key) != Controls.ESCAPE))){
                if(key == GLFW.GLFW_KEY_BACKSPACE || key == GLFW.GLFW_KEY_DELETE) {
                    chat.typeChar('\b');
                }
                return;
            }

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
