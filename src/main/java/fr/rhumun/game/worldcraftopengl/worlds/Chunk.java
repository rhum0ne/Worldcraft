package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.Renderer;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.Biome;
import fr.rhumun.game.worldcraftopengl.worlds.SaveManager;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

import static fr.rhumun.game.worldcraftopengl.Game.CHUNK_SIZE;
import static fr.rhumun.game.worldcraftopengl.Game.GAME;

@Getter
@Setter
public class Chunk extends AbstractChunk {

    Block[][][] blocks;

    private HashSet<Block> visibleBlock = new HashSet<>();
    private HashSet<Block> lightningBlocks = new HashSet<>();

    private final Biome[][] biomesMap = new Biome[16][16];

    public Chunk(World world, short renderID, int X, int Z){
        super(renderID, X, Z, world);

        blocks = new Block[CHUNK_SIZE][world.getHeigth()][CHUNK_SIZE];

        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                for (int z = 0; z < blocks[x][y].length; z++) {
                    this.addBlock(x, y, z, new Block(this, (byte) x, (short) y, (byte) z));
                }
            }
        }

        this.setLoaded(true);
    }

    void updateAllBlock(){
        for (Block[][] value : blocks)
            for (Block[] item : value) {
                for (Block block : item) {
                    if(block == null) return;
                    block.updateIsSurrounded();
                }
            }
    }

    public void setBiome(Block block, Biome biome){
        this.biomesMap[block.getChunkX()][block.getChunkZ()] = biome;
    }

    public Biome getBiome(Block block){
        return this.biomesMap[block.getChunkX()][block.getChunkZ()];
    }

    public synchronized boolean generate() {
        if(!isLoaded()) return false;
        if (this.isGenerated()) return true;

        try {
            long start = System.currentTimeMillis();
            this.getWorld().getGenerator().generate(this);
            this.setGenerated(true);
            updateAllBlock();
            this.setToUpdate(true);
            updateBordersChunks();
            long end = System.currentTimeMillis();
            GAME.debug("Finished Generating " + this + " in " + (end - start) + " ms");

            if(this.isToUnload()) unload();
            return true;

        } catch (Exception e) {
            GAME.errorLog("Error during generating chunk " + this.toString());
            GAME.errorLog(e.getMessage());
            GAME.errorLog(e.getStackTrace().toString());
            return false;

        }
    }


    private void addBlock(int x, int y, int z, Block block) {
        this.blocks[x][y][z] = block;
        //this.blockList.add(block);
    }

    public Block getBlockNoVerif(int x, int y, int z){
        //if(!this.isLoaded()) return null;
        return blocks[x][y][z];
    }

    public Block get(int x, int y, int z) {
        if (y < 0 || y >= this.getWorld().getHeigth()) {
            return null;
        }

        int offsetX = Math.floorDiv(x, CHUNK_SIZE);
        int offsetZ = Math.floorDiv(z, CHUNK_SIZE);
        int localX = Math.floorMod(x, CHUNK_SIZE);
        int localZ = Math.floorMod(z, CHUNK_SIZE);

        Chunk target = this;
        if (offsetX != 0 || offsetZ != 0) {
            target = this.getWorld().getChunk(this.getX() + offsetX, this.getZ() + offsetZ, true, false);
        }
        if (target == null || target.blocks == null) {
            return null;
        }

        return target.blocks[localX][y][localZ];
    }

    public Block getAt(int x, int y, int z) {
        if (y < 0 || y >= this.getWorld().getHeigth()) {
            return null;
        }

        int relX = x - this.getX() * CHUNK_SIZE;
        int relZ = z - this.getZ() * CHUNK_SIZE;

        int offsetX = Math.floorDiv(relX, CHUNK_SIZE);
        int offsetZ = Math.floorDiv(relZ, CHUNK_SIZE);
        int localX = Math.floorMod(relX, CHUNK_SIZE);
        int localZ = Math.floorMod(relZ, CHUNK_SIZE);

        Chunk target = this;
        if (offsetX != 0 || offsetZ != 0) {
            target = this.getWorld().getChunk(this.getX() + offsetX, this.getZ() + offsetZ, false);
        }

        if (target == null || target.blocks == null) {
            return null;
        }

        return target.blocks[localX][y][localZ];
    }

    public void setBlock(int x, int y, int z, Material mat, Model model){
        this.blocks[x][y][z].setMaterial(mat);
        this.blocks[x][y][z].setModel(model);
    }

    public Block getHighestBlock(int x, int z, boolean countLiquids){
        for(int y=this.getWorld().getHeigth()-1; y>=0; y--){
            Block block = blocks[x][y][z];
            if(block.getMaterial() != null && (countLiquids || !block.getMaterial().isLiquid())) return block;
        }
        return blocks[x][0][z];
    }


    public void setBlock(int x, int y, int z, Material mat){
        this.blocks[x][y][z].setMaterial(mat);
    }

    public String toString(){
        return "Chunk : [ " + this.getX() + " : " + this.getZ() + " ]";
    }

    public synchronized void unload() {
        unload(true);
    }

    public synchronized void unload(boolean asyncSave) {
        if (!this.isLoaded()) return;

        if (!this.isGenerated()) {
            this.setToUnload(true);
            return;
        }

        // mark chunk as no longer loaded so further unload calls don't schedule
        // additional saves while this one is in progress
        this.setLoaded(false);

        if (asyncSave) {
            SaveManager.saveChunk(this, this::deleteChunk);
        } else {
            SaveManager.saveChunkSync(this);
            deleteChunk();
        }

        GAME.debug("Unloading chunk " + this.toString());
    }

    public synchronized void deleteChunk() {
        this.setLoaded(false);
        this.getWorld().unload(this);

        if (this.blocks != null) {
            for (int x = 0; x < blocks.length; x++)
                for (int y = 0; y < blocks[x].length; y++)
                    for (int z = 0; z < blocks[x][y].length; z++)
                        this.blocks[x][y][z] = null;
        }

        this.blocks = null;
        if (this.visibleBlock != null) this.visibleBlock.clear();
        if (this.lightningBlocks != null) this.lightningBlocks.clear();
        this.visibleBlock = null;
        this.lightningBlocks = null;

        for (Renderer renderer : this.getRenderer().getRenderers())
            GAME.getGraphicModule().cleanup(renderer);

        this.cleanup();
        getWorld().getChunks().unregisterChunk(this);

//        if(this.isRendererInitialized()){
//            for(Renderer renderer : renderer.getRenderers()) renderer.cleanup();
//            this.renderer = null;
//        }
    }

    public void updateBordersChunks() {
        World world = this.getWorld();
        int X = this.getX();
        int Z = this.getZ();

        Chunk chunk = world.getChunk(X-1, Z, false);
        if(chunk != null) chunk.setToUpdate(true);

        chunk = world.getChunk(X+1, Z, false);
        if(chunk != null) chunk.setToUpdate(true);

        chunk = world.getChunk(X, Z-1, false);
        if(chunk != null) chunk.setToUpdate(true);

        chunk = world.getChunk(X, Z+1, false);
        if(chunk != null) chunk.setToUpdate(true);
    }

    public void updateAllBordersChunks() {
        World world = this.getWorld();
        int X = this.getX();
        int Z = this.getZ();

        Chunk chunk = world.getChunk(X-1, Z, false);
        if(chunk != null) chunk.updateAllBlock();

        chunk = world.getChunk(X+1, Z, false);
        if(chunk != null) chunk.updateAllBlock();

        chunk = world.getChunk(X, Z-1, false);
        if(chunk != null) chunk.updateAllBlock();

        chunk = world.getChunk(X, Z+1, false);
        if(chunk != null) chunk.updateAllBlock();
    }

    public void debugChunk() {
        StringBuilder sb = new StringBuilder();
        sb.append("---- Chunk Debug Info ----\n");
        sb.append("Position : ").append(this.toString()).append("\n");
        sb.append("RenderID : ").append(this.getRenderID()).append("\n");
        sb.append("Generated : ").append(this.isGenerated()).append("\n");
        sb.append("Loaded : ").append(this.isLoaded()).append("\n");
        sb.append("ToUnload : ").append(this.isToUnload()).append("\n");
        sb.append("ToUpdate : ").append(this.isToUpdate()).append("\n");

        int nonAir = 0;
        int air = 0;
        int total = 0;
        HashMap<Material, Integer> materialCount = new HashMap<>();

        if (blocks != null) {
            for (int x = 0; x < CHUNK_SIZE; x++) {
                for (int y = 0; y < getWorld().getHeigth(); y++) {
                    for (int z = 0; z < CHUNK_SIZE; z++) {
                        Block block = blocks[x][y][z];
                        if (block == null) continue;
                        total++;

                        Material mat = block.getMaterial();
                        if (mat == null) {
                            air++;
                        } else {
                            nonAir++;
                            materialCount.put(mat, materialCount.getOrDefault(mat, 0) + 1);
                        }
                    }
                }
            }
        }

        sb.append("Total blocks : ").append(total).append("\n");
        sb.append("Air blocks   : ").append(air).append("\n");
        sb.append("Solid blocks : ").append(nonAir).append("\n");

        GAME.log(sb.toString());
    }

}
