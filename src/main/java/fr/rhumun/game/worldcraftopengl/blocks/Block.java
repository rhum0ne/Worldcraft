package fr.rhumun.game.worldcraftopengl.blocks;

import fr.rhumun.game.worldcraftopengl.Location;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

@Getter
@Setter
public class Block {

    private Model model;
    private Material material;
    private final Location location;

    private Block[][][] nextBlocks = new Block[3][3][3];

    private Block blockAtUp;
    private Block blockAtNorth;
    private Block blockAtSouth;
    private Block blockAtDown;
    private Block blockAtEast;
    private Block blockAtWest;

    private List<Block> sideBlocks = new ArrayList<>();

    public Block(World world, int x, int y, int z) {
        this.location = new Location(world, x, y, z, 0, 0);
        this.model = Model.BLOCK;

    }

    public boolean isSurrounded() {
        // Vérifie les 6 directions pour voir si un bloc est présent
        for(Block block : this.getSideBlocks()){
            if(block == null) return false;
            if(!block.isOpaque()) return false;
        }
        return true;
    }

    public boolean isOpaque(){ return this.material != null && ( this.model.isOpaque() && this.material.isOpaque()); }

    public Block setMaterial(Material material){

        this.material = material;
        this.getSideBlocks();
        return this;
    }

    public List<Block> getSideBlocks() {
        this.getBlockAtDown(false);
        this.getBlockAtUp(false);
        this.getBlockAtNorth(false);
        this.getBlockAtSouth(false);
        this.getBlockAtWest(false);
        this.getBlockAtEast(false);
        return sideBlocks;
    }

    public Block setModel(Model model){
        this.model = model;
        return this;
    }

    public Block getBlockAtUp(){ return getBlockAtUp(true); }
    public Block getBlockAtDown(){ return getBlockAtDown(true); }
    public Block getBlockAtNorth(){ return getBlockAtNorth(true); }
    public Block getBlockAtSouth(){ return getBlockAtSouth(true); }
    public Block getBlockAtWest(){ return getBlockAtWest(true); }
    public Block getBlockAtEast(){ return getBlockAtEast(true); }

    public Block getBlockAtUp(boolean generateIfNull) {
        if(location.getY() == location.getWorld().getHeigth()-1) return null;
        if(blockAtUp == null) {
            blockAtUp = this.getLocation().getWorld().getBlockAt(location.getX(), location.getY() + 1, location.getZ(), generateIfNull);
            if (blockAtUp != null) {
                this.sideBlocks.add(blockAtUp);
                this.nextBlocks[1][2][1] = blockAtUp;
            }
        }
        return blockAtUp;
    }
    public Block getBlockAtDown(boolean generateIfNull) {
        if(location.getY() == 0) return null;
        if(blockAtDown == null) {
            blockAtDown = this.getLocation().getWorld().getBlockAt(location.getX(), location.getY() - 1, location.getZ(), generateIfNull);
            if (blockAtDown != null){
                this.sideBlocks.add(blockAtDown);
                this.nextBlocks[1][0][1] = blockAtDown;
            }
        }
        return blockAtDown;
    }
    public Block getBlockAtNorth(boolean generateIfNull) {
        if(blockAtNorth == null) {
            blockAtNorth = this.getLocation().getWorld().getBlockAt(location.getX()+1, location.getY(), location.getZ(), generateIfNull);
            if(blockAtNorth != null){
                this.sideBlocks.add(blockAtNorth);
                this.nextBlocks[2][1][1] = blockAtNorth;
            }
        }
        return blockAtNorth;
    }
    public Block getBlockAtSouth(boolean generateIfNull) {
        if(blockAtSouth == null) {
            blockAtSouth = this.getLocation().getWorld().getBlockAt(location.getX() - 1, location.getY(), location.getZ(), generateIfNull);
            if (blockAtSouth != null){
                this.sideBlocks.add(blockAtSouth);
                this.nextBlocks[0][1][1] = blockAtSouth;
            }
        }
        return blockAtSouth;
    }
    public Block getBlockAtEast(boolean generateIfNull) {
        if(blockAtEast == null) {
            blockAtEast = this.getLocation().getWorld().getBlockAt(location.getX(), location.getY(), location.getZ() - 1, generateIfNull);
            if (blockAtEast != null) {
                this.sideBlocks.add(blockAtEast);
                this.nextBlocks[1][1][0] = blockAtEast;
            }
        }
        return blockAtEast;
    }
    public Block getBlockAtWest(boolean generateIfNull) {
        if(blockAtWest == null) {
            blockAtWest = this.getLocation().getWorld().getBlockAt(location.getX(), location.getY(), location.getZ() + 1, generateIfNull);
            if (blockAtWest != null) {
                this.sideBlocks.add(blockAtWest);
                this.nextBlocks[1][1][2] = blockAtWest;
            }
        }
        return blockAtWest;
    }
}
