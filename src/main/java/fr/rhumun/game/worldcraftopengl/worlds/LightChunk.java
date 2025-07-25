package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.Renderer;
import lombok.Getter;
import lombok.Setter;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

@Getter @Setter
public class LightChunk extends AbstractChunk{

    private Material[][][] materials;
    private boolean[][][] isVisible;

    public LightChunk(short renderID, int X, int Z, World world) {
        super(renderID, X, Z, world);

        materials = new Material[Game.CHUNK_SIZE][world.getHeigth()][Game.CHUNK_SIZE];
        isVisible = new boolean[Game.CHUNK_SIZE][world.getHeigth()][Game.CHUNK_SIZE];

        this.generate();

        this.setToUpdate(true);
    }

    public int getLODLevel(double distance) {
        if (distance < Game.LOD*200*200/2) return 1;       // haute qualité
        if (distance < Game.LOD*350*350/2) return 2;       // 2x2
        if (distance < Game.LOD*450*450/2) return 4;       // 4x4
        if (distance < Game.LOD*600*600/2) return 8;       // 8x8
        return 16;                          // max compression
    }


    public void updateAllBlock() {
        if (!isGenerated()) generate();

        int sizeX = materials.length;
        int sizeY = materials[0].length;
        int sizeZ = materials[0][0].length;

        if (isVisible == null ||
                isVisible.length != sizeX ||
                isVisible[0].length != sizeY ||
                isVisible[0][0].length != sizeZ) {
            isVisible = new boolean[sizeX][sizeY][sizeZ];
        } else {
            for (int x = 0; x < sizeX; x++)
                for (int y = 0; y < sizeY; y++)
                    java.util.Arrays.fill(isVisible[x][y], false);
        }

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    Material mat = materials[x][y][z];
                    if (mat == null) continue;

                    // Par défaut on suppose le bloc non visible
                    boolean visible = false;

                    // Lecture directe (aucun appel de méthode)
                    // Bords => visible automatiquement
                    if (x == 0 || materials[x - 1][y][z] == null) visible = true;
                    else if (x == sizeX - 1 || materials[x + 1][y][z] == null) visible = true;

                    else if (y == 0 || materials[x][y - 1][z] == null) visible = true;
                    else if (y == sizeY - 1 || materials[x][y + 1][z] == null) visible = true;

                    else if (z == 0 || materials[x][y][z - 1] == null) visible = true;
                    else if (z == sizeZ - 1 || materials[x][y][z + 1] == null) visible = true;

                    isVisible[x][y][z] = visible;
                }
            }
        }
    }


    private Material getMaterialAt(int x, int y, int z) {
        if (y < 0 || y >= getWorld().getHeigth()) return null;
        if (x >= 0 && x < Game.CHUNK_SIZE && z >= 0 && z < Game.CHUNK_SIZE) {
            return materials[x][y][z];
        }

        int chunkShift = 4; // car 2^4 = 16
        int chunkMask = 0b1111;

        int cX = this.getX() + (x >> chunkShift);
        int cZ = this.getZ() + (z >> chunkShift);
        int localX = x & chunkMask;
        int localZ = z & chunkMask;

        if (cX == this.getX() && cZ == this.getZ()) {
            return materials[localX][y][localZ];
        }

        AbstractChunk neighbor = getWorld().getChunks().getAbstractChunk(cX, cZ);
        if (neighbor instanceof LightChunk l) {
            if (l.materials == null) return null;
            return l.materials[localX][y][localZ];
        } else if (neighbor instanceof Chunk c) {
            if (c.blocks == null) return null;
            return c.blocks[localX][y][localZ].getMaterial();
        }

        return null;
    }

    private boolean isMaterialTransparent(int x, int y, int z){
        Material material = getMaterialAt(x, y, z);
        return material == null || material.getOpacity() != OpacityType.OPAQUE;
    }


    public synchronized void unload() {
        if(!this.isGenerated()) {
            this.setToUnload(true);
            return;
        }

        if(!this.isLoaded()) return;

        this.setLoaded(false);
        getWorld().getChunks().unregisterChunk(this);

        this.materials = null;
        this.isVisible = null;

        for(Renderer renderer : this.getRenderer().getRenderers())
            GAME.getGraphicModule().cleanup(renderer);

        this.cleanup();
    }

    @Override
    public synchronized boolean generate() {
        if (this.isGenerated()) return true;


        try {
            long start = System.currentTimeMillis();

            this.getWorld().getGenerator().generate(this);

            this.setGenerated(true);
            updateAllBlock();
            this.setToUpdate(true);
            //updateBordersChunks();
            long end = System.currentTimeMillis();
            GAME.debug("Finished Generating " + this + " in " + (end - start) + " ms");

            if(isToUnload()) unload();
            return true;
        } catch (Exception e) {
            GAME.errorLog("Error during generating light chunk " + this.toString());
            GAME.errorLog(e.getMessage());
            GAME.errorLog(e.getStackTrace().toString());
        }
        return false;
    }

    public String toString(){
        return "Light Chunk : [ " + this.getX() + " : " + this.getZ() + " ]";
    }

    public void setMaterial(int x, int y, int z, Material material) {
        this.materials[x][y][z] = material;
    }
}
