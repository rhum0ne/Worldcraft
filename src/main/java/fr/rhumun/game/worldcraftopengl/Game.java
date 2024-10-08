package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.blocks.Material;
import fr.rhumun.game.worldcraftopengl.blocks.Texture;
import fr.rhumun.game.worldcraftopengl.controls.Controls;
import fr.rhumun.game.worldcraftopengl.outputs.audio.AudioManager;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ShaderUtils;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;

import java.time.Instant;
import java.util.*;

@Getter
public class Game {

    public static Game GAME;

    public static String SHADERS_PATH = "C:\\Users\\eletu\\IdeaProjects\\WorldCraft\\src\\main\\java\\fr\\rhumun\\game\\worldcraftopengl\\outputs\\graphic\\shaders\\";
    public static String TEXTURES_PATH = "C:\\Users\\eletu\\IdeaProjects\\WorldCraft\\src\\main\\resources\\assets\\";
    public static int SHOW_DISTANCE = 7;
    public static boolean SHOWING_FPS = true;
    public static boolean GENERATION = true;
    public static boolean UPDATE_FRUSTRUM = true;
    public static boolean ENABLE_VSYNC = false;

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
        Texture.init();

        audioManager = new AudioManager();
        audioManager.init();
        //Model.init();

        this.world = new World();

        this.player = new Player(this, 8, world.getChunk(0, 0, true).getHighestBlock(8, 8).getLocation().getY()+10, 8);

        Timer timer = new Timer();

        materials = new ArrayList<>(Arrays.asList(Material.values()));

        player.setSelectedMaterial(Material.WATER);

        graphicModule = new GraphicModule(this);

        timer.schedule(gameLoop = new GameLoop(this, player), Date.from(Instant.now()), 20);
        graphicModule.run();
    }
}
