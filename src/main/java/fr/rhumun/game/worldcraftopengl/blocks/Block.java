package fr.rhumun.game.worldcraftopengl.blocks;

import fr.rhumun.game.worldcraftopengl.Location;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.PointLight;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.ForcedModelMaterial;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Block {

    private Model model;
    private Material material;
    private final Location location;
    private final Chunk chunk;
    private boolean isSurrounded;

    private int tick = 0;

    //private Block[][][] nextBlocks = new Block[3][3][3];

//    private Block blockAtUp;
//    private Block blockAtNorth;
//    private Block blockAtSouth;
//    private Block blockAtDown;
//    private Block blockAtEast;
//    private Block blockAtWest;

    private List<Block> sideBlocks = new ArrayList<>();

    public Block(World world, Chunk chunk, int x, int y, int z) {
        this.chunk = chunk;
        this.location = new Location(world, x, y, z, 0, 0);
        this.model = Model.BLOCK;

    }

    public void updateIsSurrounded() {
        // Vérifie les 6 directions pour voir si un bloc est présent
        for(Block block : this.getSideBlocks()){
            if(block == null || !block.isOpaque()) {
                this.isSurrounded = false;
                return;
            }
        }
        this.isSurrounded = true;
    }

    public boolean isOpaque(){ return this.material != null && ( this.model.isOpaque() && this.material.isOpaque()); }

    public Block setMaterial(Material material){
        if(this.material != null && this.material.getMaterial() instanceof PointLight){
            this.chunk.getLightningBlocks().remove(this);
        }

        this.material = material;
        this.chunk.setToUpdate(true);

        if(material==null) {
            for (Block block : this.getSideBlocks()) block.setSurrounded(false);
            this.getLocation().getChunk().getVisibleBlock().remove(this);
        }
        else {
            if(material.getMaterial() instanceof PointLight){
                this.chunk.getLightningBlocks().add(this);
            }

            for(Block block : this.getSideBlocks())block.updateIsSurrounded();
            this.getLocation().getChunk().getVisibleBlock().add(this);

            if(material.getMaterial() instanceof ForcedModelMaterial fMat){
                this.setModel(fMat.getModel());
            }
        }
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
        this.chunk.setToUpdate(true);
        return this;
    }

    public Block getBlockAtUp(){ return getBlockAtUp(true); }
    public Block getBlockAtDown(){ return getBlockAtDown(true); }
    public Block getBlockAtNorth(){ return getBlockAtNorth(true); }
    public Block getBlockAtSouth(){ return getBlockAtSouth(true); }
    public Block getBlockAtWest(){ return getBlockAtWest(true); }
    public Block getBlockAtEast(){ return getBlockAtEast(true); }

    public Block getBlockAtUp(boolean generateIfNull) {
        if(location.getYInt() == location.getWorld().getHeigth()-1) return null;
        return this.getLocation().getWorld().getBlockAt(location.getX(), location.getY() + 1, location.getZ(), generateIfNull);
    }
    public Block getBlockAtDown(boolean generateIfNull) {
        if(location.getY() == 0) return null;
        return this.getLocation().getWorld().getBlockAt(location.getX(), location.getY() - 1, location.getZ(), generateIfNull);
    }
    public Block getBlockAtNorth(boolean generateIfNull) {
        return this.getLocation().getWorld().getBlockAt(location.getX()+1, location.getY(), location.getZ(), generateIfNull);
    }
    public Block getBlockAtSouth(boolean generateIfNull) {
        return this.getLocation().getWorld().getBlockAt(location.getX() - 1, location.getY(), location.getZ(), generateIfNull);
    }
    public Block getBlockAtEast(boolean generateIfNull) {
        return this.getLocation().getWorld().getBlockAt(location.getX(), location.getY(), location.getZ() - 1, generateIfNull);
    }
    public Block getBlockAtWest(boolean generateIfNull) {
        return this.getLocation().getWorld().getBlockAt(location.getX(), location.getY(), location.getZ() + 1, generateIfNull);
    }
}
