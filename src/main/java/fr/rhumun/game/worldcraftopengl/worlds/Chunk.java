package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.graphic.Model;
import fr.rhumun.game.worldcraftopengl.graphic.Models;
import fr.rhumun.game.worldcraftopengl.props.Material;
import fr.rhumun.game.worldcraftopengl.props.Block;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Chunk {

    Block[][][] blocks = new Block[16][32][16];
    private final List<Block> blockList = new ArrayList<>();

    private int X;
    private int Z;
    public Chunk(int X, int Z){
        this.X = X;
        this.Z = Z;

        for (int x = 0; x < blocks.length; x++)
            for (int y = 0; y<blocks[x].length; y++) {
                for(int z = 0; z<blocks[x][y].length; z++){
                    this.addBlock(x, z, new Block(Models.BLOCK, null, X*16 + x, y, Z*16 + z));
                }
            }

        for(int x=0; x<16; x++){
            for(int z=0; z<16; z++){
                for(int y=10; y<20; y++) {
                    this.blocks[x][y][z].setMaterial(Material.COBBLE);
                }
                if((x+z*Z)%5==0 && (X*x*z+x)%7==0){
                    this.setBlock(x, 20, z, Material.LOG, Models.HEXAGON);
                    this.setBlock(x, 21, z, Material.LOG, Models.HEXAGON);
                    this.setBlock(x, 22, z, Material.LOG, Models.HEXAGON);
                }
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

    public void setBlock(int x, int y, int z, Material mat, Models model){
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
