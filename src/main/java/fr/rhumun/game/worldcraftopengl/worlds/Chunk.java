package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.blocks.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.blocks.Model;
import fr.rhumun.game.worldcraftopengl.blocks.Block;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.ChunkRenderer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Chunk {

    Block[][][] blocks;
    //private final List<Block> blockList = new ArrayList<>();
    private List<Block> visibleBlock = new ArrayList<>();
    private List<Block> lightningBlocks = new ArrayList<>();

    private int X;
    private int Z;

    private World world;

    @Setter
    private boolean toUpdate = false;
    private ChunkRenderer renderer;

    @Setter
    private boolean generated = false;
    @Setter
    private boolean loaded = false; //For the first chunk render

    public Chunk(World world, int X, int Z){
        this.X = X;
        this.Z = Z;
        this.world = world;

        blocks = new Block[16][world.getHeigth()][16];

        for (int x = 0; x < blocks.length; x++)
            for (int y = 0; y<blocks[x].length; y++) {
                for(int z = 0; z<blocks[x][y].length; z++){
                    this.addBlock(x, z, new Block(world, this, X*16 + x, y, Z*16 + z));
                }
            }

    }

    private void addBlock(int x, int z, Block block) {
        this.blocks[x][(int) block.getLocation().getY()][z] = block;
        //this.blockList.add(block);
    }

    public Block get(int x, int y, int z){
        if(y<0 || y>= world.getHeigth()){
            //System.out.println("Y too high.");
            return null;
        }
        if(x<0) x+=16;
        if(z<0) z+=16;
        //System.out.println("x: "+(x)+", z: "+(z));
        if(x>=16 || z>=16 || x<0 || z<0){
            Chunk chunk = world.getChunkAt(this.X*16+x, this.Z*16+z, false);
            if(chunk == null) return null;
            int xInput = x%16;
            int zInput = z%16;
//            if(xInput<0) xInput+=16;
//            if(zInput<0) zInput+=16;
            return chunk.get(xInput,y,zInput); // blocks est une structure de données représentant le monde
        }
        //System.out.println("x: "+(x)+", z: "+(z));
        //System.out.println("X: "+(this.X*16+x)+", Z: "+(this.Z*16+z));
        return blocks[x][y][z];
    }

    public void setBlock(int x, int y, int z, Material mat, Model model){
        this.blocks[x][y][z].setMaterial(mat);
        this.blocks[x][y][z].setModel(model);
    }

    public Block getHighestBlock(int x, int z){
        for(int y=world.getHeigth()-1; y>=0; y--){
            Block block = blocks[x][y][z];
            if(block.getMaterial() != null) return block;
        }
        return null;
    }


    public void setBlock(int x, int y, int z, Material mat){
        this.blocks[x][y][z].setMaterial(mat);
    }

    public String toString(){
        return "Chunk : [ " + X + " : " + Z + " ]";
    }

    public void unload(){
        this.getWorld().unload(this);
        if(this.isRendererInitialized()){
            //for(Renderer renderer : this.renderer.getRenderers()) renderer.cleanup();
            this.renderer = null;
        }
        this.loaded = false;
//        this.blocks = null;
//        this.visibleBlock = null;
//        this.lightningBlocks = null;
    }

    public boolean isRendererInitialized() {
        return this.renderer != null;
    }

    public ChunkRenderer getRenderer(){
        return (this.renderer == null) ? this.renderer = new ChunkRenderer(this) : this.renderer;
    }
}
