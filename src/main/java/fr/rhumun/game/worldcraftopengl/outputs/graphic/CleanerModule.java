package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.Renderer;

import java.util.ArrayList;

public class CleanerModule {

    private final GraphicModule graphicModule;
    private ArrayList<Renderer> renderers = new ArrayList<>();

    public CleanerModule(GraphicModule graphicModule){
        this.graphicModule = graphicModule;
    }

    public void clean(){
        ArrayList<Renderer> work = new ArrayList<>(renderers);
        for(Renderer renderer : work){
            renderer.cleanup();
            renderers.remove(renderer);
        }
    }

    public void add(Renderer renderer) {
        renderers.add(renderer);
    }
}
