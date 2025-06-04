package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.AbstractChunkRenderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.ChunkRenderer;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.locks.ReentrantLock;

@Getter @Setter
public abstract class AbstractChunk {
    private final short renderID;

    private final int X;
    private final int Z;

    private final World world;
    private AbstractChunkRenderer renderer;

    private boolean generated = false;
    private volatile boolean loading = false;
    @Setter
    private boolean toUpdate = false;
    private boolean toUnload = false;

    @Setter
    private boolean loaded = false;

    protected AbstractChunk(short renderID, int x, int z, World world) {
        this.renderID = renderID;
        X = x;
        Z = z;
        this.world = world;
    }

    public boolean isRendererInitialized() {
        return this.renderer != null;
    }

    public AbstractChunkRenderer getRenderer(){
        return (!isRendererInitialized()) ? this.renderer = ChunkRenderer.createChunkRenderer(this) : this.renderer;
    }

    public synchronized void unload(){

        if(!this.isGenerated()) {
            this.setToUnload(true);
            return;
        }

        if (this.renderer != null) {
            //this.renderer.cleanup();
            this.renderer = null;
        }
    }
    public synchronized void cleanup(){
        this.getRenderer().cleanup();
        this.renderer = null;
    }

    public abstract boolean generate();

    public synchronized void render() {
        this.getRenderer().render();
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}
