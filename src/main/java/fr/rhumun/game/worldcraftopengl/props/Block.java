package fr.rhumun.game.worldcraftopengl.props;

import fr.rhumun.game.worldcraftopengl.Location;
import fr.rhumun.game.worldcraftopengl.graphic.Model;
import fr.rhumun.game.worldcraftopengl.graphic.Models;
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

    public Block(Models model, Material material, int x, int y, int z) {
        this.location = new Location(x, y, z, 0, 0);
        this.model = model.get();
        this.material = material;
    }

    public void setModel(Models model){
        this.model = model.get();
    }

    public boolean isSurrounded() {
        // Vérifie les 6 directions pour voir si un bloc est présent
        World world = GAME.getWorld();
        return world.getBlockAt(location.getX()+1, location.getY(), location.getZ()).isOpaque() &&
                world.getBlockAt(location.getX()-1, location.getY(), location.getZ()).isOpaque() &&
                world.getBlockAt(location.getX(), location.getY()+1, location.getZ()).isOpaque() &&
                world.getBlockAt(location.getX(), location.getY()-1, location.getZ()).isOpaque() &&
                world.getBlockAt(location.getX(), location.getY(), location.getZ()+1).isOpaque() &&
                world.getBlockAt(location.getX(), location.getY(), location.getZ()-1).isOpaque();
    }

    public boolean isOpaque(){ return this.material != null; }
}
