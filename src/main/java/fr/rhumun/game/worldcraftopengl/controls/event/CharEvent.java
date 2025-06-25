package fr.rhumun.game.worldcraftopengl.controls.event;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GuiModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.TypingGui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.ChatGui;
import org.lwjgl.glfw.GLFWCharCallbackI;

/**
 * Callback translating typed characters to the active GUI or chat.
 */
public class CharEvent implements GLFWCharCallbackI {

    private final Game game;

    public CharEvent(Game game) {
        this.game = game;
    }

    @Override
    public void invoke(long window, int codepoint) {
        char c = (char) codepoint;
        GuiModule guiModule = game.getGraphicModule().getGuiModule();
        if (guiModule.hasGUIOpened() && guiModule.getGui() instanceof TypingGui tg) {
            tg.typeChar(c);
            return;
        }
        ChatGui chat = guiModule.getChat();
        if (chat.isShowed()) {
            chat.typeChar(c);
        }
    }
}
