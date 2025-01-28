package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.TextComponent;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class DebugMenu extends Gui {

    private boolean isShowed = false;
    private TextComponent FPS;
    private TextComponent playerPos;
    private TextComponent verticesChunksCount;
    private TextComponent memoryAndProcessors;

    public DebugMenu() {
        super(0, 10, 200, 100, null);

        this.addText(0, 0, "Debug Menu [F3]");
        FPS = this.addText(0, 16, "FPS : NA");
        playerPos = this.addText(0, 32, "Loading Positions...");
        verticesChunksCount = this.addText(0, 70, "V: NA");
        memoryAndProcessors = this.addText(0, 90, "Memory (F/M/T): " +
                (Runtime.getRuntime().freeMemory() / (1024 * 1024)) + " MB | " +
                (Runtime.getRuntime().maxMemory() / (1024 * 1024)) + " MB | " +
                (Runtime.getRuntime().totalMemory() / (1024 * 1024)) + " MB |   AP: " +
                Runtime.getRuntime().availableProcessors());

        verticesChunksCount.setColor(Color.ORANGE);
        memoryAndProcessors.setColor(Color.ORANGE);
    }

    public void updateLiveData(){
        memoryAndProcessors.setText("Memory (F/M/T): " +
                (Runtime.getRuntime().freeMemory() / (1024 * 1024)) + " MB | " +
                (Runtime.getRuntime().maxMemory() / (1024 * 1024)) + " MB | " +
                (Runtime.getRuntime().totalMemory() / (1024 * 1024)) + " MB |   AP: " +
                Runtime.getRuntime().availableProcessors());

    }

    public void setFPS(int fps){
        this.FPS.setText("FPS : " + fps);
    }

    public void setPlayerPos(double[] pos){
        this.playerPos.setText("X:" + String.format("%.5g",pos[0]) +
                " Y:" + String.format("%.4g",pos[1]) +
                " Z:" + String.format("%.4g",pos[2]) + "\n" +
                "Yaw:" + String.format("%.4g",pos[3]) +
                " Pitch:" + String.format("%.4g",pos[4]));
    }

    public void setVerticesChunksCount(int count){
        this.verticesChunksCount.setText("V: " + count);
    }

    public void setShowed(boolean show){
        this.isShowed = show;

        Game.SHOWING_FPS = show;
        Game.SHOWING_RENDERER_DATA = show;
    }

    public void render(){
        this.updateLiveData();
        super.render();
    }
}
