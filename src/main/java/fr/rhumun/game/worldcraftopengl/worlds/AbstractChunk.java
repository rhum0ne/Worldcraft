package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.AbstractChunkRenderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.ChunkRenderer;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractChunk {
    private final short renderID;

    private final int X;
    private final int Z;

    private final World world;
    private AbstractChunkRenderer renderer;

    private boolean generated = false;
    private boolean locked = false;
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

    public void unload(){

        if(!this.isGenerated() || this.isLocked()) {
            this.setToUnload(true);
            return;
        }

        if (this.renderer != null) {
            //this.renderer.cleanup();
            this.renderer = null;
        }
    }
    public void cleanup(){
        this.getRenderer().cleanup();
        this.renderer = null;
    }

    public abstract boolean generate();


    public void lock(){ this.locked = true; }
    public void unlock(){ this.locked = false; }
}
