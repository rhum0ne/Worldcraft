package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.blocks.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.controls.Controls;
import fr.rhumun.game.worldcraftopengl.outputs.audio.AudioManager;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.*;

@Getter @Setter
public class Game {

    public static Game GAME;

    public static String GAME_PATH = "E:\\Devellopement\\Games\\Worldcraft\\";
    public static int SHOW_DISTANCE = 11;
    public static int CHUNK_SIZE = 16;
    public static boolean SHOWING_GUIS = true;
    public static boolean SHOWING_FPS = true;
    public static boolean SHOWING_RENDERER_DATA = true;
    public static int GUI_ZOOM = 2;
    public static boolean GENERATION = true;
    public static boolean UPDATE_FRUSTRUM = true;
    public static boolean ENABLE_VSYNC = false;
    public static boolean NO_CLIP = false;

    public static String SHADERS_PATH = GAME_PATH + "src\\main\\java\\fr\\rhumun\\game\\worldcraftopengl\\outputs\\graphic\\shaders\\";
    public static String TEXTURES_PATH = GAME_PATH + "src\\main\\resources\\assets\\";

    final GraphicModule graphicModule;
    final AudioManager audioManager;
    final GameLoop gameLoop;

    boolean isPaused = false;
    boolean isShowingTriangles = false;
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

        this.world = new World();

        this.player = new Player(this, 0, world.getChunk(0, 0, true).getHighestBlock(0, 0).getLocation().getY()+10, 0);

        Timer timer = new Timer();

        materials = new ArrayList<>(Arrays.asList(Material.values()));

        player.addItem(new Item(Material.SAND));
        player.addItem(new Item(Material.COBBLE));
        player.addItem(new Item(Material.SAPLING));
        player.addItem(new Item(Material.PLANKS));
        player.addItem(new Item(Material.PURPLE_LAMP));
        player.addItem(new Item(Material.CYAN_LAMP));
        player.addItem(new Item(Material.LAMP));
        player.addItem(new Item(Material.LANTERN));
        player.addItem(new Item(Material.STONE_BRICK));

        player.updateInventory();

        graphicModule = new GraphicModule(this);

        timer.schedule(gameLoop = new GameLoop(this, player), Date.from(Instant.now()), 20);
        graphicModule.run();
    }

    public void setPaused(boolean b) {
        this.isPaused = b;
    }

    public void errorLog(String log){
        System.err.println(log);
    }

    public void log(String log){
        System.out.println(log);
    }
}
