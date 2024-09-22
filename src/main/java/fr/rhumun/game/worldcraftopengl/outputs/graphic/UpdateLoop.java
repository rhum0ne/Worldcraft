package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import java.util.TimerTask;

public class UpdateLoop implements Runnable {

    GraphicModule graphicModule;

    public UpdateLoop(GraphicModule graphicModule){
        this.graphicModule = graphicModule;
    }
    @Override
    public void run() {
        graphicModule.updateViewMatrix();
    }
}
