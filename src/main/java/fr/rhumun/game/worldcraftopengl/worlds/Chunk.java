package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.blocks.Model;
import fr.rhumun.game.worldcraftopengl.blocks.Material;
import fr.rhumun.game.worldcraftopengl.blocks.Block;
import fr.rhumun.game.worldcraftopengl.worlds.structures.Structure;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Chunk {

    Block[][][] blocks = new Block[16][32][16];
    private final List<Block> blockList = new ArrayList<>();

    private int X;
    private int Z;

    private World world;

    @Setter
    private boolean generated = false;

    public Chunk(World world, int X, int Z){
        this.X = X;
        this.Z = Z;
        this.world = world;

        for (int x = 0; x < blocks.length; x++)
            for (int y = 0; y<blocks[x].length; y++) {
                for(int z = 0; z<blocks[x][y].length; z++){
                    this.addBlock(x, z, new Block(Model.BLOCK, null, X*16 + x, y, Z*16 + z));
                }
            }
    }

    private void addBlock(int x, int z, Block block) {
        this.blocks[x][(int) block.getLocation().getY()][z] = block;
        this.blockList.add(block);
    }

    public Block get(int x, int y, int z){
        if(x<0) x+=16;
        if(z<0) z+=16;
        if(x>=16 || y>=32 || z>=16 || x<0 || y<0 || z<0){
            System.out.println("ERROR: searching prop at " + x + " , " + y + " , " + z + " in chunk.");
            return null;
        }

        return blocks[x][y][z];
    }

    public void setBlock(int x, int y, int z, Material mat, Model model){
        this.blocks[x][y][z].setMaterial(mat);
        this.blocks[x][y][z].setModel(model);
    }


    public void setBlock(int x, int y, int z, Material mat){
        this.blocks[x][y][z].setMaterial(mat);
    }

    public String toString(){
        return "Chunk : [ " + X + " : " + Z + " ]";
    }
}
