package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public abstract class AbstractChunkRenderer {


    private final ArrayList<Renderer> renderers = new ArrayList<>();
    private boolean areRenderersInitialized = false;
    @Setter
    private int distanceFromPlayer;

    private int verticesNumber;

    public AbstractChunkRenderer(){

    }

    public abstract void update();
    public abstract void updateData();

    public abstract void render();


    public abstract void cleanup();

}
