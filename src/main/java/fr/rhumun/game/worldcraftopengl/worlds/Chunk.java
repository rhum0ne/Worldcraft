package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.Block;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.ChunkRenderer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static fr.rhumun.game.worldcraftopengl.Game.CHUNK_SIZE;
import static fr.rhumun.game.worldcraftopengl.Game.GAME;

@Getter
public class Chunk {

    Block[][][] blocks;
    //private final List<Block> blockList = new ArrayList<>();
    private List<Block> visibleBlock = new ArrayList<>();
    private List<Block> lightningBlocks = new ArrayList<>();

    private final int X;
    private final int Z;

    private final World world;

    @Setter
    private boolean toUpdate = false;
    private Thread generator;
    private ChunkRenderer renderer;

    @Setter
    private boolean generated = false;
    @Setter
    private boolean loaded = false; //For the first chunk render

    public Chunk(World world, int X, int Z){
        this.X = X;
        this.Z = Z;
        this.world = world;

        blocks = new Block[CHUNK_SIZE][world.getHeigth()][CHUNK_SIZE];

        for (int x = 0; x < blocks.length; x++)
            for (int y = 0; y<blocks[x].length; y++) {
                for(int z = 0; z<blocks[x][y].length; z++){
                    this.addBlock(x, z, new Block(this, (byte) x, (short) y, (byte) z));
                }
            }
    }

    public void generate(){
        if(this.isGenerated()) return;

        this.generator = new ChunkGenerator(this);
        this.generator.start();
    }

    private void addBlock(int x, int z, Block block) {
        this.blocks[x][(int) block.getLocation().getY()][z] = block;
        //this.blockList.add(block);
    }

    public Block getBlockNoVerif(int x, int y, int z){
        if(!this.isLoaded()) return null;
        return blocks[x][y][z];
    }

    public Block get(int x, int y, int z){
        if(y<0 || y>= world.getHeigth())
            return null;


        if(x<0) x+=CHUNK_SIZE;
        if(z<0) z+=CHUNK_SIZE;

        if(x>=CHUNK_SIZE || z>=CHUNK_SIZE || x<0 || z<0){
            Chunk chunk = world.getChunkAt(this.X*CHUNK_SIZE+x, this.Z*CHUNK_SIZE+z, false);
            if(chunk == null) return null;
            int xInput = x%CHUNK_SIZE;
            int zInput = z%CHUNK_SIZE;

            return chunk.get(xInput,y,zInput);
        }

        return blocks[x][y][z];
    }

    public Block getAt(int x, int y, int z){
        if(y<0 || y>= world.getHeigth()){
            //System.out.println("Y too high.");
            return null;
        }
        x-=X*CHUNK_SIZE;
        z-=Z*CHUNK_SIZE;

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

        if(xO!=0 || zO!=0) target = world.getChunk(target.X+xO, target.Z+zO, false);
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

    public Block getHighestBlock(int x, int z, boolean b){
        for(int y=world.getHeigth()-1; y>=0; y--){
            Block block = blocks[x][y][z];
            if(block.getMaterial() != null && (b || !block.getMaterial().isLiquid())) return block;
        }
        return blocks[x][0][z];
    }


    public void setBlock(int x, int y, int z, Material mat){
        this.blocks[x][y][z].setMaterial(mat);
    }

    public String toString(){
        return "Chunk : [ " + X + " : " + Z + " ]";
    }

    public void unload(){
        GAME.log("Unloading chunk " + this.toString());
        this.loaded = false;
        this.getWorld().unload(this);
        this.blocks = null;
        this.visibleBlock = null;
        this.lightningBlocks = null;

//        if(this.isRendererInitialized()){
//            for(Renderer renderer : renderer.getRenderers()) renderer.cleanup();
//            this.renderer = null;
//        }
    }

    public boolean isRendererInitialized() {
        return this.renderer != null;
    }

    public ChunkRenderer getRenderer(){
        return (this.renderer == null) ? this.renderer = new ChunkRenderer(this) : this.renderer;
    }

    public void updateBordersChunks() {
        Chunk chunk = world.getChunk(X-1, Z, false);
        if(chunk != null) chunk.setToUpdate(true);

        chunk = world.getChunk(X+1, Z, false);
        if(chunk != null) chunk.setToUpdate(true);

        chunk = world.getChunk(X, Z-1, false);
        if(chunk != null) chunk.setToUpdate(true);

        chunk = world.getChunk(X, Z+1, false);
        if(chunk != null) chunk.setToUpdate(true);
    }
}
