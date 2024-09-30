package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.blocks.Material;
import fr.rhumun.game.worldcraftopengl.controls.Controls;
import fr.rhumun.game.worldcraftopengl.outputs.audio.AudioManager;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;

import java.time.Instant;
import java.util.*;

@Getter
public class Game {

    public static Game GAME;

    public static String SHADERS_PATH = "C:\\Users\\eletu\\IdeaProjects\\WorldCraft\\src\\main\\java\\fr\\rhumun\\game\\worldcraftopengl\\outputs\\graphic\\shaders\\";
    public static String TEXTURES_PATH = "C:\\Users\\eletu\\IdeaProjects\\WorldCraft\\src\\main\\resources\\assets\\";
    public static int SHOW_DISTANCE = 5;
    public static boolean SHOWING_FPS = true;
    public static boolean GENERATION = true;
    public static boolean UPDATE_FRUSTRUM = true;

    GraphicModule graphicModule;
    AudioManager audioManager;
    GameLoop gameLoop;

    World world;
    Player player;

    List<Controls> pressedKeys = new ArrayList<>();

    List<Material> materials;
    public static void main(String[] args) {
        new Game();
    }

    public Game(){
        GAME = this;
        Controls.init();

        audioManager = new AudioManager();
        audioManager.init();
        //Model.init();

        this.world = new World();

        this.player = new Player(this, 8, world.getChunk(0, 0, true).getHighestBlock(8, 8).getLocation().getY()+2, 8);

        Timer timer = new Timer();
        timer.schedule(gameLoop = new GameLoop(this, player), Date.from(Instant.now()), 20);

        materials = new ArrayList<>(Arrays.asList(Material.values()));

        player.setSelectedMaterial(Material.PURPLE_LAMP);

        graphicModule = new GraphicModule(this);
        graphicModule.run();
    }
}
