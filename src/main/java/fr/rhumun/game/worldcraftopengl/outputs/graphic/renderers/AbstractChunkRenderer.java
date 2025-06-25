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

    private volatile boolean vaoDirty = false;
    private volatile boolean updating = false;

    public AbstractChunkRenderer(){

    }

    public abstract void update();
    public abstract void updateData();

    public abstract void render();


    public abstract void cleanup();

    public boolean needsVaoUpdate() { return vaoDirty; }
    public void markVaoDirty() { vaoDirty = true; }
    public void markVaoClean() { vaoDirty = false; }

    public boolean isUpdating() { return updating; }
    public void setUpdating(boolean updating) { this.updating = updating; }

}
