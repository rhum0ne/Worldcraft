package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.graphic.Model;
import fr.rhumun.game.worldcraftopengl.graphic.Models;
import lombok.Getter;

@Getter
public class Block {

    private final Model model = Models.BLOCK.get();
    private Location location;

    public Block(){ this(0,0,0); }

    public Block(int x, int y, int z){
        this.location = new Location(x, y ,z, 0, 0);


    }

}
