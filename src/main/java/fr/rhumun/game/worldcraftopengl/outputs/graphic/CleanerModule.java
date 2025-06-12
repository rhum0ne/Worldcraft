package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.Renderer;

import java.util.ArrayList;
import java.util.Iterator;

public class CleanerModule {

    private final GraphicModule graphicModule;
    private final ArrayList<Renderer> renderers = new ArrayList<>();

    public CleanerModule(GraphicModule graphicModule){
        this.graphicModule = graphicModule;
    }

    public void clean(){
//        for (Renderer renderer : renderers) {
//            if (renderer != null) {
//                renderer.cleanup();
//            }
//        }
//
//        renderers.clear();
    }

    public void add(Renderer renderer) {
        renderers.add(renderer);
    }
}
