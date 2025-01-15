package fr.rhumun.game.worldcraftopengl.content;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.entities.Location;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.types.PointLight;
import fr.rhumun.game.worldcraftopengl.content.materials.types.ForcedModelMaterial;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.Biome;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Block {

    private Model model;
    private Material material;
    private final Chunk chunk;
    private boolean isSurrounded;

    private byte chunkX;
    private short chunkY;
    private byte chunkZ;

    private Biome biome;

    private int state = 0;

    public Block(Chunk chunk, byte chunkX, short chunkY, byte chunkZ) {
        this.chunk = chunk;
        this.model = Model.BLOCK;
        this.chunkY = chunkY;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;

    }

    public World getWorld(){ return this.chunk.getWorld(); }

    public int getX(){ return Game.CHUNK_SIZE*chunk.getX() +  this.chunkX; }
    public int getY(){ return this.chunkY; }
    public int getZ(){ return Game.CHUNK_SIZE*chunk.getZ() +  this.chunkZ; }

    public Location getLocation(){ return new Location(chunk.getWorld(), this.getX(), this.getY(), this.getZ()); }

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

    public boolean isOpaque(){
        return this.material != null && ( this.model.isOpaque() && this.material.getOpacity() == OpacityType.OPAQUE);
    }

    public boolean hasBlockAtFace(float nx, float ny, float nz) {
        //if(!block.isOpaque()) return false;
        //Location loc = this.getLocation();
        int x = this.getX() + Math.round(nx);
        int y = this.getY() + Math.round(ny);
        int z = this.getZ() + Math.round(nz);

        Block face = chunk.getAt(x,y,z);
        return face != null && !this.material.getOpacity().isVisibleWith(face) ;
    }

    public Block setMaterial(Material material){
        if(this.material != null && this.material.getMaterial() instanceof PointLight){
            this.chunk.getLightningBlocks().remove(this);
        }
        this.material = material;

        if(material==null) {
            for (Block block : this.getSideBlocks()){
                if(block==null) continue;
                block.setSurrounded(false);
            }
            chunk.getVisibleBlock().remove(this);
        }
        else {
            if(material.getMaterial() instanceof PointLight){
                this.chunk.getLightningBlocks().add(this);
            }

            if(material.getOpacity()!=OpacityType.OPAQUE)
                for (Block block : this.getSideBlocks()) {
                    if(block==null) continue;
                    block.setSurrounded(false);
                }
            else
                for(Block block : this.getSideBlocks()){
                    if(block==null) continue;
                    block.updateIsSurrounded();
                }
            chunk.getVisibleBlock().add(this);

            if(material.getMaterial() instanceof ForcedModelMaterial fMat){
                this.setModel(fMat.getModel());
            }
        }

        this.chunk.setToUpdate(true);

        return this;
    }

    public Block[] getSideBlocks() {
        Block[] sideBlocks = new Block[6];

        sideBlocks[0] = this.getBlockAtDown(false);
        sideBlocks[1] = this.getBlockAtUp(false);
        sideBlocks[2] = this.getBlockAtNorth(false);
        sideBlocks[3] = this.getBlockAtSouth(false);
        sideBlocks[4] = this.getBlockAtWest(false);
        sideBlocks[5] = this.getBlockAtEast(false);
        return sideBlocks;
    }

    public Block setModel(Model model){
        this.model = model;
        if(!model.isOpaque)
            for (Block block : this.getSideBlocks()) {
                if(block==null) continue;
                block.setSurrounded(false);
            }
        else
            for(Block block : this.getSideBlocks()){
                if(block==null) continue;
                block.updateIsSurrounded();
            }

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
        if(this.getY() == this.chunk.getWorld().getHeigth()-1) return null;
        return chunk.getAt(this.getX(), this.getY() + 1, this.getZ());
    }
    public Block getBlockAtDown(boolean generateIfNull) {
        if(this.getY() == 0) return null;
        return chunk.getAt(this.getX(), this.getY() - 1,this.getZ());
    }
    public Block getBlockAtNorth(boolean generateIfNull) {
        return chunk.getAt(this.getX()+1, this.getY(), this.getZ());
    }
    public Block getBlockAtSouth(boolean generateIfNull) {
        return chunk.getAt(this.getX() - 1, this.getY(), this.getZ());
    }
    public Block getBlockAtEast(boolean generateIfNull) {
        return chunk.getAt(this.getX(), this.getY(),this.getZ() - 1);
    }
    public Block getBlockAtWest(boolean generateIfNull) {
        return chunk.getAt(this.getX(), this.getY(), this.getZ() + 1);
    }

    public boolean isOnTheFloor(){
        if(this.getModel() == Model.SLAB){
            return this.getState() == 0;
        }
        return false;
    }

    public boolean isCliquable() {
        return this.material != null && this.material.getOpacity() != OpacityType.LIQUID;
    }
}
