package fr.rhumun.game.worldcraftopengl.worlds.structures;

import fr.rhumun.game.worldcraftopengl.content.Block;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.worlds.World;

public class TreeStructure extends AbstractStructure{
    public TreeStructure() {
        super(5,5);
    }

    @Override
    public void buildAt(World world, int x, int y, int z) {
        int h = y;

        world.getBlockAt(x, h++ ,z, true).setMaterial(Material.LOG).setModel(Model.CYLINDER);
        world.getBlockAt(x, h++ ,z, true).setMaterial(Material.LOG).setModel(Model.CYLINDER);
        world.getBlockAt(x, h++ ,z, true).setMaterial(Material.LOG).setModel(Model.CYLINDER);
        world.getBlockAt(x, h++ ,z, true).setMaterial(Material.LOG).setModel(Model.CYLINDER);
        world.getBlockAt(x, h++ ,z, true).setMaterial(Material.LOG).setModel(Model.CYLINDER);
        if(z%3==0) world.getBlockAt(x, h++ ,z, true).setMaterial(Material.LOG).setModel(Model.CYLINDER);
        else if(x%5==0) {
            world.getBlockAt(x, h++, z, true).setMaterial(Material.LOG).setModel(Model.CYLINDER);
            world.getBlockAt(x, h++, z, true).setMaterial(Material.LOG).setModel(Model.CYLINDER);
        }
        for(int c=h-2; c<h; c++){
            for(int a=x-2; a<=x+2; a++)
                for(int b=z-2; b<=z+2; b++){
                    Block block = world.getBlockAt(a, c ,b, true);
                    if(block.getMaterial() == null) block.setMaterial(Material.LEAVES).setModel(Model.BLOCK);
                }
        }

        for(int a=x-1; a<=x+1; a++)
            for(int b=z-1; b<=z+1; b++){
                Block block = world.getBlockAt(a, h ,b, true);
                if(block.getMaterial() == null) block.setMaterial(Material.LEAVES).setModel(Model.BLOCK);
            }
        h++;
        for(int a=x-1; a<=x+1; a++)
            for(int b=z-1; b<=z+1; b++){
                Block block = world.getBlockAt(a, h ,b, true);
                if(block.getMaterial() == null) block.setMaterial(Material.LEAVES).setModel(Model.BLOCK);
            }

    }
}
