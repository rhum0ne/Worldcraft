package fr.rhumun.game.worldcraftopengl.controls.event;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.controls.Controls;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.ChatGui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu.CreateWorldGui;
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

            ChatGui chat = game.getGraphicModule().getGuiModule().getChat();
            if(chat.isShowed() && (!Controls.exists(key) || (Controls.get(key) != Controls.ENTER && Controls.get(key) != Controls.ESCAPE))){
                chat.getEnteredText().print(String.valueOf((char) key));
                return;
            }

            if(game.getGraphicModule().getGuiModule().hasGUIOpened() &&
                    game.getGraphicModule().getGuiModule().getGui() instanceof CreateWorldGui create){
                if(key == 259){ // backspace
                    create.backspace();
                } else if(!Controls.exists(key) || (Controls.get(key) != Controls.ENTER && Controls.get(key) != Controls.ESCAPE)) {
                    create.appendChar((char) key);
                } else if(Controls.get(key) == Controls.ENTER){
                    create.createWorld();
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
