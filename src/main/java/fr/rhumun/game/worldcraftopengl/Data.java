package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.entities.Location;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GuiModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.DebugMenu;

public class Data {

    private final Game game;

    private int FPS;
    private double[] playerPos = new double[5];


    public Data(Game game) {
        this.game = game;
    }

    public void setFPS(int FPS) {
        this.FPS = FPS;
        game.getGraphicModule().getGuiModule().getDebugMenu().setFPS(FPS);
    }

    public void setPlayerPos(Location loc) {
        this.playerPos = new double[]{
                loc.getX(),
                loc.getY(),
                loc.getZ(),
                loc.getYaw(),
                loc.getPitch()
        };

        GuiModule guiModule = game.getGraphicModule().getGuiModule();
        if(guiModule != null) guiModule.getDebugMenu().setPlayerPos(this.playerPos);
    }
}
