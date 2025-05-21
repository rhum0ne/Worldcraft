package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.Block;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.AbstractChunkRenderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.LightChunkRenderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.Renderer;
import lombok.Getter;
import lombok.Setter;

import static fr.rhumun.game.worldcraftopengl.Game.DEBUG;
import static fr.rhumun.game.worldcraftopengl.Game.GAME;

@Getter @Setter
public class LightChunk extends AbstractChunk{

    private Material[][][] materials;
    private boolean[][][] isVisible;

    private boolean isGenerated = false;
    private boolean toUpdate = false;
    private boolean toUnload = false;
    private boolean loaded = false;

    public LightChunk(short renderID, int X, int Z, World world) {
        super(renderID, X, Z, world);

        materials = new Material[Game.CHUNK_SIZE][world.getHeigth()][Game.CHUNK_SIZE];
        isVisible = new boolean[Game.CHUNK_SIZE][world.getHeigth()][Game.CHUNK_SIZE];

        if(DEBUG)
            for (int x = 0; x < Game.CHUNK_SIZE; x++)
                for (int z = 0; z < Game.CHUNK_SIZE; z++)
                    materials[x][80][z] = Material.RED_WOOL;



        updateAllBlock();

        this.toUpdate = true;
    }

    public void updateAllBlock() {
        if(!isGenerated) generate();

        int sizeX = materials.length;
        int sizeY = materials[0].length;
        int sizeZ = materials[0][0].length;

        isVisible = new boolean[sizeX][sizeY][sizeZ];

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    Material mat = materials[x][y][z];
                    if (mat == null) continue; // rien à afficher

                    boolean visible = false;

                    // Check les 6 directions autour
                    if (x == 0 || isMaterialTransparent(x - 1, y, z)) visible = true;
                    else if (x == sizeX - 1 || isMaterialTransparent(x + 1, y, z)) visible = true;

                    else if (y == 0 || isMaterialTransparent(x, y - 1, z)) visible = true;
                    else if (y == sizeY - 1 || isMaterialTransparent(x, y + 1, z)) visible = true;

                    else if (z == 0 || isMaterialTransparent(x, y, z - 1)) visible = true;
                    else if (z == sizeZ - 1 || isMaterialTransparent(x, y, z + 1)) visible = true;

                    isVisible[x][y][z] = visible;
                }
            }
        }
    }

    private boolean isMaterialTransparent(int x, int y, int z){
        Material material = materials[x][y][z];
        return material == null ? true : material.getOpacity() != OpacityType.OPAQUE;
    }


    public void unload() {
        if(!this.isGenerated()) {
            this.toUnload = true;
            return;
        }

        if(!this.isLoaded()) return;

        GAME.debug("Unloading chunk " + this.toString());
        this.loaded = false;

        this.materials = null;
        this.isVisible = null;

        for(Renderer renderer : this.getRenderer().getRenderers())
            GAME.getGraphicModule().cleanup(renderer);

        this.cleanup();
    }

    @Override
    public boolean generate() {
        if (this.isGenerated()) return true;

        try {
            long start = System.currentTimeMillis();
            //this.getWorld().getGenerator().generate(this);
            this.setGenerated(true);
            updateAllBlock();
            this.setToUpdate(true);
            //updateBordersChunks();
            long end = System.currentTimeMillis();
            GAME.debug("Finished Generating " + this + " in " + (end - start) + " ms");

            if(toUnload) unload();
            return true;
        } catch (Exception e) {
            GAME.errorLog(e);
        }
        return false;
    }

    public String toString(){
        return "Light Chunk : [ " + this.getX() + " : " + this.getZ() + " ]";
    }
}
