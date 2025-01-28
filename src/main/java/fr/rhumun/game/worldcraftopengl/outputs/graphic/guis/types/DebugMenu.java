package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Component;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.TextComponent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DebugMenu extends Gui {

    private boolean isShowed = false;
    private TextComponent FPS;
    private TextComponent playerPos;

    public DebugMenu() {
        super(0, 10, 200, 100, null);

        this.addText(0, 0, "Debug Menu [F3]");
        FPS = this.addText(0, 16, "FPS : NA");
        playerPos = this.addText(0, 32, "Loading Positions...");
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
}
