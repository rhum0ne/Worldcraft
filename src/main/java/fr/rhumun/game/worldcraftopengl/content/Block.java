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

    private short model;
    private short material;
    private Chunk chunk;
    @Setter
    private boolean isSurrounded= false;

    private byte chunkXZ; // Stocke chunkX et chunkZ ensemble (4 bits chacun)
    private byte chunkY;

    private byte state = 0; //Créer un block data, et une interface BlockDataMaterial

    public Block(Chunk chunk, byte chunkX, int chunkY, byte chunkZ) {
        this.chunk = chunk;
        this.model = Model.BLOCK.getId();
        this.chunkY = (byte) (chunkY - 128);
        this.chunkXZ = (byte) ((chunkX & 0xF) | ((chunkZ & 0xF) << 4)); // Stocker chunkX et chunkZ dans chunkXZ
        this.material = -1;
    }

    // Méthodes pour extraire chunkX et chunkZ de chunkXZ
    public byte getChunkX() {
        return (byte) (chunkXZ & 0xF); // Extraire les 4 bits de poids faible
    }

    public byte getChunkZ() {
        return (byte) ((chunkXZ >> 4) & 0xF); // Extraire les 4 bits de poids fort
    }

    // Récupérer les coordonnées absolues
    public int getX() {
        return Game.CHUNK_SIZE * chunk.getX() + getChunkX();
    }

    public int getY() {
        return this.chunkY + 128;
    }

    public int getZ() {
        return Game.CHUNK_SIZE * chunk.getZ() + getChunkZ();
    }

    public World getWorld() {
        return this.chunk.getWorld();
    }


    public void setBiome(Biome biome){
        this.chunk.setBiome(this, biome);
    }

    public Biome getBiome(){
        return this.chunk.getBiome(this);
    }

    public Material getMaterial(){
        return (this.material == -1) ? null : Material.getById(material);
    }

    public Model getModel(){
        return Model.getById(this.model);
    }

    public Location getLocation(){ return new Location(chunk.getWorld(), this.getX(), this.getY(), this.getZ()); }

    public void updateIsSurrounded(){
        if(!chunk.isGenerated()) return;

        if(!this.isOpaque()) {
            this.isSurrounded = false;
            return;
        }


        this.updateIsSurrounded(this.getSideBlocks());
    }
    public void updateIsSurrounded(Block[] sideBlocks) {
        if(!chunk.isGenerated()) return;

        // Vérifie les 6 directions pour voir si un bloc est présent
        for(Block block : sideBlocks) {
            if(block == null || !block.isOpaque()) {
                this.isSurrounded = false;
                return;
            }
        }
        this.isSurrounded = true;
    }

    public boolean isOpaque(){
        return this.getMaterial() != null && ( this.getModel().isOpaque() && this.getMaterial().getOpacity() == OpacityType.OPAQUE);
    }

    public boolean hasBlockAtFace(float nx, float ny, float nz) {
        //if(!block.isOpaque()) return false;
        //Location loc = this.getLocation();
        int x = this.getX() + Math.round(nx);
        int y = this.getY() + Math.round(ny);
        int z = this.getZ() + Math.round(nz);

        Block face = chunk.getAt(x,y,z);
        return face != null && !this.getMaterial().getOpacity().isVisibleWith(face) ;
    }

    public Block setMaterial(Material material){
        //FAIRE METHODE SET MODEL AND MATERIAL QUI VA EVITER LES REPETITIONS DE GETSIDEBLOCKS QUAND ON VEUT FAIRE LES 2
        if(this.getMaterial() != null && this.getMaterial().getMaterial() instanceof PointLight){
            this.chunk.getLightningBlocks().remove(this);
        }

        if(material==null) {
            this.material = -1;

            chunk.getVisibleBlock().remove(this);

            if(!chunk.isGenerated()) return this;

            Block[] sideBlocks = this.getSideBlocks();
            for (Block block :sideBlocks ){
                if(block==null) continue;
                block.setSurrounded(false);
                if(block.getChunk() != chunk) block.getChunk().setToUpdate(true);
            }
        }
        else {
            this.material = (short) material.getId();

            if(material.getMaterial() instanceof PointLight){
                this.chunk.getLightningBlocks().add(this);
            }
            chunk.getVisibleBlock().add(this);

            if(material.getMaterial() instanceof ForcedModelMaterial fMat){
                this.setModel(fMat.getModel());
            }

            if(chunk.isGenerated()){

                if(material.getOpacity()!=OpacityType.OPAQUE) {

                    Block[] sideBlocks = this.getSideBlocks();
                    for (Block block : sideBlocks) {
                        if (block == null) continue;
                        block.setSurrounded(false);
                        if(block.getChunk() != chunk) block.getChunk().setToUpdate(true);
                    }
                }
                else {
                    Block[] sideBlocks = this.getSideBlocks();
                    for (Block block : sideBlocks) {
                        if (block == null) continue;
                        block.updateIsSurrounded();
                        if(block.getChunk() != chunk) block.getChunk().setToUpdate(true);
                    }
                }
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
        this.model = model.getId();
        Block[] sideBlocks = this.getSideBlocks();

        if(chunk.isGenerated())
            if (!this.getModel().isOpaque)
                for (Block block : sideBlocks) {
                    if (block == null) continue;
                    block.setSurrounded(false);
                }
            else
                for (Block block : sideBlocks) {
                    if (block == null) continue;
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
        return this.getMaterial() != null && this.getMaterial().getOpacity() != OpacityType.LIQUID;
    }

    public void setState(int state) {
        this.state = (byte) state;
    }

}
