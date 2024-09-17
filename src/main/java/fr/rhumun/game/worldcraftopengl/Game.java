package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.controls.Controls;
import fr.rhumun.game.worldcraftopengl.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.graphic.Models;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

@Getter
public class Game {

    public static Game GAME;

    public static String SHADERS_PATH = "C:\\Users\\eletu\\IdeaProjects\\WorldCraftOpenGL\\src\\main\\java\\fr\\rhumun\\game\\worldcraftopengl\\graphic\\shaders\\";
    public static String TEXTURES_PATH = "C:\\Users\\eletu\\IdeaProjects\\WorldCraftOpenGL\\src\\main\\resources\\";
    public static int SHOW_DISTANCE = 4;

    GraphicModule graphicModule;
    GameLoop gameLoop;

    World world;
    Player player;

    List<Controls> pressedKeys = new ArrayList<>();
    public static void main(String[] args) {
        new Game();
    }

    public Game(){
        GAME = this;
        Controls.init();
        Models.init();

        this.world = new World();

        this.player = new Player(this, 8, 22, 8);

        Timer timer = new Timer();
        timer.schedule(gameLoop = new GameLoop(this, player), Date.from(Instant.now()), 20);

        graphicModule = new GraphicModule(this);
        graphicModule.run();

    }
}
