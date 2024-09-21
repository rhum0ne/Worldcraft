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

    public Block(Model model, Material material, int x, int y, int z) {
        this.location = new Location(x, y, z, 0, 0);
        this.model = model;
        this.material = material;
    }

    public boolean isSurrounded() {
        // Vérifie les 6 directions pour voir si un bloc est présent
        try {
            World world = GAME.getWorld();
            return world.getBlockAt(location.getX()+1, location.getY(), location.getZ()).isOpaque() &&
                    world.getBlockAt(location.getX()-1, location.getY(), location.getZ()).isOpaque() &&
                    world.getBlockAt(location.getX(), location.getY()+1, location.getZ()).isOpaque() &&
                    world.getBlockAt(location.getX(), location.getY()-1, location.getZ()).isOpaque() &&
                    world.getBlockAt(location.getX(), location.getY(), location.getZ()+1).isOpaque() &&
                    world.getBlockAt(location.getX(), location.getY(), location.getZ()-1).isOpaque();
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
}
