package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.props.Model;
import fr.rhumun.game.worldcraftopengl.props.Material;
import fr.rhumun.game.worldcraftopengl.props.Block;
import fr.rhumun.game.worldcraftopengl.worlds.structures.Structure;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

@Getter
public class Chunk {

    Block[][][] blocks = new Block[16][32][16];
    private final List<Block> blockList = new ArrayList<>();

    private int X;
    private int Z;

    private World world;
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

    public void generate(){
        if(generated) return;
        for(int x=0; x<16; x++){
            for(int z=0; z<16; z++){
                for(int y=1; y<10; y++) {
                    if(y==9) this.setBlock(x, y, z, Material.GRASS);
                    else if(y==8 || y==7) this.setBlock(x, y, z, Material.DIRT);
                    else this.setBlock(x, y, z, Material.STONE);
                }
                if((1+x+z+Z)%5==0 && (X+x+2*z)%7==0){
                    //world.spawnStructure(Structure.TREE, 16*X + x, 20, 16*Z + z);
                }
            }
        }

        this.generated = true;
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
