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
}
