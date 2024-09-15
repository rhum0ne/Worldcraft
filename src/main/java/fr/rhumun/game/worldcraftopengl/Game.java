package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.controls.Control;
import fr.rhumun.game.worldcraftopengl.controls.Controls;
import fr.rhumun.game.worldcraftopengl.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.graphic.Models;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Game {

    public static String SHADERS_PATH = "E:\\Devellopement\\Games\\Worldcraft\\src\\main\\java\\fr\\rhumun\\game\\worldcraftopengl\\graphic\\shaders\\";
    public static String TEXTURES_PATH = "E:\\Devellopement\\Games\\Worldcraft\\src\\main\\resources\\";
    public static int SHOW_DISTANCE = 16;

    GraphicModule graphicModule;

    Block[][][] blocks = new Block[16][16][16];
    Player player;

    Block cobble = new Block();

    List<Controls> pressedKeys = new ArrayList<>();
    public static void main(String[] args) {
        new Game();
    }

    public Game(){
        Controls.init();
        Models.init();

        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++) {
                this.blocks[x][8][z] = new Block(x, 8, z);
            }

        this.player = new Player(this, 8, 10, 8);
        graphicModule = new GraphicModule(this);

        graphicModule.run();
    }
}
