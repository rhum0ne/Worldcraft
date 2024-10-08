package fr.rhumun.game.worldcraftopengl.blocks;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.TextureUtils;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Texture {

    public static Texture COBBLE;
    public static Texture DIRT;
    public static Texture GRASS_BLOCK;
    public static Texture OAK_LOG;
    public static Texture LEAVES;
    public static Texture STONE_BRICKS;
    public static Texture STONE;
    public static Texture PLANKS;
    public static Texture LAMP;
    public static Texture WATER;
    public static Texture GRASS;

    public static void init(){
        COBBLE = new Texture(1, "cobble.png");
        DIRT = new Texture(2, "dirt.png");
        GRASS_BLOCK = new Texture(3, "grass.png");
        OAK_LOG = new Texture(4, "oak_log.png");
        LEAVES = new Texture(5, "leaves.png");
        STONE_BRICKS = new Texture(6, "stone_brick.png");
        STONE = new Texture(7, "stone.png");
        PLANKS = new Texture(8, "planks.png");
        LAMP = new Texture(9, "lamp.png");
        WATER = new Texture(10, "water.png");
        GRASS = new Texture(11, "grass-plant.png");
    }

    public static ArrayList<Texture> textures = new ArrayList<>();

    private String path;
    private int id;

    public Texture(int id, String path){
        this.id = id;
        this.path = path;
        textures.add(this);
    }

    public boolean isAnimated(){return this instanceof AnimatedTexture;}

}
