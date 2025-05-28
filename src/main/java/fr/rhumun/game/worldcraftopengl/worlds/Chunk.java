package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.Block;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.ChunkRenderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.Renderer;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.Biome;
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

    @Setter
    private boolean toUpdate = false;
    private boolean toUnload = false;

    @Setter
    private boolean loaded = false;

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
    }

    private void updateAllBlock(){
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

    public boolean generate() {
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

            if(toUnload) unload();
            return true;
        } catch (Exception e) {
            GAME.errorLog("Error during generating chunk " + this.toString());
            GAME.errorLog(e.getMessage());
            GAME.errorLog(e.getStackTrace().toString());
        }
        return false;
    }


    private void addBlock(int x, int y, int z, Block block) {
        this.blocks[x][y][z] = block;
        //this.blockList.add(block);
    }

    public Block getBlockNoVerif(int x, int y, int z){
        //if(!this.isLoaded()) return null;
        return blocks[x][y][z];
    }

    public Block get(int x, int y, int z){
        if(y<0 || y>= this.getWorld().getHeigth())
            return null;

        Chunk target = this;

        int xO = 0;
        int zO = 0;
        while(x>=CHUNK_SIZE){
            x-=CHUNK_SIZE;
            xO++;
        }

        while(z>=CHUNK_SIZE){
            z-=CHUNK_SIZE;
            zO++;
        }

        while(x<0){
            x+=CHUNK_SIZE;
            xO--;
        }

        while(z<0){
            z+=CHUNK_SIZE;
            zO--;
        }

        if(xO!=0 || zO!=0) target = this.getWorld().getChunk(target.getX()+xO, target.getZ()+zO, false);
        if(target==null) return null;
        if(target.blocks == null) return null;
        //System.out.println("x: "+(x)+", z: "+(z));
        //System.out.println("X: "+(this.X*16+x)+", Z: "+(this.Z*16+z));
        return target.blocks[x][y][z];
    }

    public Block getAt(int x, int y, int z){
        if(y<0 || y>= this.getWorld().getHeigth()){
            //System.out.println("Y too high.");
            return null;
        }

        x-=this.getX()*CHUNK_SIZE;
        z-=this.getZ()*CHUNK_SIZE;

        Chunk target = this;

        int xO = 0;
        int zO = 0;
        while(x>=CHUNK_SIZE){
            x-=CHUNK_SIZE;
            xO++;
        }

        while(z>=CHUNK_SIZE){
            z-=CHUNK_SIZE;
            zO++;
        }

        while(x<0){
            x+=CHUNK_SIZE;
            xO--;
        }

        while(z<0){
            z+=CHUNK_SIZE;
            zO--;
        }

        if(xO!=0 || zO!=0) target = this.getWorld().getChunk(target.getX()+xO, target.getZ()+zO, false);
        if(target==null) return null;
        if(target.blocks == null) return null;
        //System.out.println("x: "+(x)+", z: "+(z));
        //System.out.println("X: "+(this.X*16+x)+", Z: "+(this.Z*16+z));
        return target.blocks[x][y][z];
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

    public void unload(){
        if(!this.isGenerated()) {
            this.toUnload = true;
            return;
        }

        if(!this.isLoaded()) return;

        GAME.debug("Unloading chunk " + this.toString());
        this.loaded = false;
        this.getWorld().unload(this);

        if (this.blocks != null) {
            for (int x = 0; x < blocks.length; x++)
                for (int y = 0; y < blocks[x].length; y++) {
                    for (int z = 0; z < blocks[x][y].length; z++) {
                        this.blocks[x][y][z] = null;
                    }
                }
        }

        this.blocks = null;
        this.visibleBlock.clear();
        this.lightningBlocks.clear();
        this.visibleBlock = null;
        this.lightningBlocks = null;

        for(Renderer renderer : this.getRenderer().getRenderers())
            GAME.getGraphicModule().cleanup(renderer);

        this.cleanup();

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
}
