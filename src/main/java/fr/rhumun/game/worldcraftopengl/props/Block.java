package fr.rhumun.game.worldcraftopengl.props;

import fr.rhumun.game.worldcraftopengl.Location;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;
import lombok.Setter;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

@Getter
@Setter
public class Block {

    private Model model;
    private Material material;
    private final Location location;

    private Block blockAtUp;
    private Block blockAtNorth;
    private Block blockAtSouth;
    private Block blockAtDown;
    private Block blockAtEast;
    private Block blockAtWest;

    public Block(Model model, Material material, int x, int y, int z) {
        this.location = new Location(x, y, z, 0, 0);
        this.model = model;
        this.material = material;
    }

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
        this.material = material;
        return this;
    }

    public Block setModel(Model model){
        this.model = model;
        return this;
    }

    public Block getBlockAtUp() {
        if(blockAtUp == null) blockAtUp = GAME.getWorld().getBlockAt(location.getX(), location.getY()+1, location.getZ());
        return blockAtUp;
    }
    public Block getBlockAtDown() {
        if(blockAtDown == null) blockAtDown = GAME.getWorld().getBlockAt(location.getX(), location.getY()-1, location.getZ());
        return blockAtDown;
    }
    public Block getBlockAtNorth() {
        if(blockAtNorth == null) blockAtNorth = GAME.getWorld().getBlockAt(location.getX()+1, location.getY(), location.getZ());
        return blockAtNorth;
    }
    public Block getBlockAtSouth() {
        if(blockAtSouth == null) blockAtSouth = GAME.getWorld().getBlockAt(location.getX()-1, location.getY(), location.getZ());
        return blockAtSouth;
    }
    public Block getBlockAtEast() {
        if(blockAtEast == null) blockAtEast = GAME.getWorld().getBlockAt(location.getX(), location.getY(), location.getZ()-1);
        return blockAtEast;
    }
    public Block getBlockAtWest() {
        if(blockAtWest == null) blockAtWest = GAME.getWorld().getBlockAt(location.getX(), location.getY(), location.getZ()+1);
        return blockAtWest;
    }
}
