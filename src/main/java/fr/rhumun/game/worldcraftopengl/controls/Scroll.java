package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GuiModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.ScrollableGui;
import org.lwjgl.glfw.GLFWScrollCallback;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class Scroll extends GLFWScrollCallback {

    private final Game game;

    public Scroll(Game game) {
        this.game = game;
    }

    @Override
    public void invoke(long window, double xoffset, double yoffset) {
        GuiModule guiModule = game.getGraphicModule().getGuiModule();
        if (guiModule.hasGUIOpened() && guiModule.getGui() instanceof ScrollableGui sg) {
            sg.onScroll(yoffset);
            return;
        }

        if(game.isPaused()) return;

        if (yoffset > 0) {
            game.getPlayer().setSelectedSlot((game.getPlayer().getSelectedSlot() +8) % 9);
        } else if (yoffset < 0) {
            game.getPlayer().setSelectedSlot((game.getPlayer().getSelectedSlot() + 1) % 9);
        }
    }

}
