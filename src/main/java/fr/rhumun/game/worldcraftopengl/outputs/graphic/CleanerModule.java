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
        for (Iterator<Renderer> it = renderers.iterator(); it.hasNext(); ) {
            Renderer renderer = it.next();
            if (renderer != null) {
                renderer.cleanup();
            }
            it.remove();
        }
    }

    public void add(Renderer renderer) {
        renderers.add(renderer);
    }
}
