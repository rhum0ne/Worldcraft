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

    /*private short[] skyRGB = new short[3];
    //private short[] rgb = new short[3];
    short red;
    short green;
    short blue;*/

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

        /*this.red = 10;
        this.green = 5;
        this.blue = 30;*/
    }

    /*public short getRedIntensity() {
        Material mat = this.material;
        if(mat != null && mat.material instanceof LightSource ls) return ls.getRed();
        return red;
    }

    public short getGreenIntensity() {
        Material mat = this.material;
        if(mat != null && mat.material instanceof LightSource ls) return ls.getGreen();
        return green;
    }

    public short getBlueIntensity() {
        Material mat = this.material;
        if(mat != null && mat.material instanceof LightSource ls) return ls.getBlue();
        return blue;
    }

    public void update(){
        this.getSideBlocks();
        this.updateLight();
    }

    public void updateLight(){
        Material mat = this.material;
        if(mat != null && mat.material instanceof LightSource) return;
        updateSkyLight();

//        System.out.println("Updating light intensity");
//
//        int r = 0;
//        int g = 0;
//        int b = 0;
//        for(Block block : this.getSideBlocks()){
//            System.out.println("Getting side blocks light intensity");
//            if(block == null) continue;
//            r = Math.max(block.getRedIntensity(), r);
//            g = Math.max(block.getGreenIntensity(), g);
//            b = Math.max(block.getBlueIntensity(), b);
//        }
//
//        r = Math.max(0, r-17);
//        g = Math.max(0, g-17);
//        b = Math.max(0, b-17);
//
//        System.out.println("R: " + r);
//        System.out.println("G: " + g);
//        System.out.println("B: " + b);
//
//        if((rgb[0] == r && rgb[1] == g && rgb[2] == b)) return;
//
//        System.out.println("Changing light intensity");
//        rgb[0] = (short) r;
//        rgb[1] = (short) g;
//        rgb[2] = (short) b;
//        for(Block block : this.sideBlocks) {
//            if(block == null) continue;
//            block.update();
//        }
    }

    private void updateSkyLight() {
        if(this.getLocation().isMaxHeight()){
            //System.out.println("It's max height");
            this.skyRGB[0] = this.getLocation().getWorld().getRed();
            this.skyRGB[1] = this.getLocation().getWorld().getGreen();
            this.skyRGB[2] = this.getLocation().getWorld().getBlue();

            Block block = this.getBlockAtDown();
            while(block != null && !block.isOpaque()){
                block.skyRGB[0] = this.getLocation().getWorld().getRed();
                block.skyRGB[1] = this.getLocation().getWorld().getGreen();
                block.skyRGB[2] = this.getLocation().getWorld().getBlue();
                block = block.getBlockAtDown();
            }
        }
    }*/

    public boolean isSurrounded() {
        // Vérifie les 6 directions pour voir si un bloc est présent
        try {
            return this.getBlockAtNorth().isOpaque() &&
                    this.getBlockAtSouth().isOpaque() &&
                    this.getBlockAtUp().isOpaque() &&
                    this.getBlockAtDown().isOpaque() &&
                    this.getBlockAtWest().isOpaque() &&
                    this.getBlockAtEast().isOpaque();
        } catch (Exception e) {
            return false;
            //A CORRIGER
        }
    }

    public boolean isOpaque(){ return this.material != null && ( this.model.isOpaque() && this.material.isOpaque()); }

    public Block setMaterial(Material material){

        //System.out.println("Setting material to " + material);
        this.material = material;
//        if(material != null && material.getMaterial() instanceof LightSource) {
//            System.out.println("Its a ligthsource");
//            for (Block block : this.getSideBlocks()) {
//                if (block == null) continue;
//                block.updateLight();
//            }
//      }
        //this.update();

        //System.out.println("Done.");
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
