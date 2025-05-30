package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.items.Item;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.controls.Controls;
import fr.rhumun.game.worldcraftopengl.entities.Entity;
import fr.rhumun.game.worldcraftopengl.entities.OtterEntity;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.audio.AudioManager;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter @Setter
public class Game {

    public static Game GAME;

    public static String GAME_PATH = "C:\\Users\\eletu\\IdeaProjects\\Worldcraft\\";
    //public static String GAME_PATH = "E:\\Devellopement\\Games\\Worldcraft\\";
    public static int SIMULATION_DISTANCE = 5;
    public static int SHOW_DISTANCE = 15;
    public static int CHUNK_SIZE = 16;
    public static boolean ANTIALIASING = false;
    public static boolean SHOWING_GUIS = true;
    public static boolean SHOWING_FPS = false;
    public static boolean SHOWING_RENDERER_DATA = false;
    public static int GUI_ZOOM = 2;
    public static boolean GENERATION = true;
    public static boolean UPDATE_FRUSTRUM = true;
    public static boolean ENABLE_VSYNC = false;
    public static boolean GREEDY_MESHING = true;
    public static boolean GL_DEBUG = false;
    public static boolean DEBUG = false;
    public static final long LAG_SPIKE_LIMIT = 100;

    public static String SHADERS_PATH = GAME_PATH + "src\\main\\java\\fr\\rhumun\\game\\worldcraftopengl\\outputs\\graphic\\shaders\\";
    public static String TEXTURES_PATH = GAME_PATH + "src\\main\\resources\\assets\\";

    final GraphicModule graphicModule;
    final AudioManager audioManager;
    final Data data;
    final GameLoop gameLoop;

    boolean isPaused = false;
    boolean isPlaying = true;
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
        Material.init();
        Model.init();

        this.data = new Data(this);

        audioManager = new AudioManager(this);
        audioManager.init();


        this.player = new Player(this);
        this.world = new World();
        this.world.load();

        while(!world.isLoaded()){ }

        this.world.spawnPlayer(player);

        //Timer timer = new Timer();

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

        player.playSound(Sound.STONE1);

        //timer.schedule(gameLoop = new GameLoop(this, player), Date.from(Instant.now()), 20);
        gameLoop = new GameLoop(this, player);
        gameLoop.start();
        graphicModule = new GraphicModule(this);
        graphicModule.run();
    }

    public void setPaused(boolean b) {
        this.isPaused = b;
    }

    public void closeGame(){
        this.isPlaying = false;
    }

    public void sendMessage(Player player, String message){
        this.graphicModule.getGuiModule().getChat().println(player.getName() + ": " + message);
    }

    public void processCommand(String cmd){
        log("processing command " + cmd);
        world.addEntity(new OtterEntity(this, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 0, 0));
    }

    public void errorLog(String log){
        System.err.println("Thread : " + Thread.currentThread().getName());
        System.err.println(Arrays.toString(Thread.currentThread().getStackTrace()));
        System.err.println("[ERROR MESSAGE] - " + log);
    }
    public void errorLog(Exception e){
        System.err.println("Thread : " + Thread.currentThread().getName());
        System.err.println(Arrays.toString(Thread.currentThread().getStackTrace()));

        System.err.println("[ERROR] - " + e.getMessage());
        System.err.println("[ERROR] - " + e.getCause().getMessage());
        System.err.println("[ERROR] - " + Arrays.toString(e.getStackTrace()));
    }
    public void log(String log){ System.out.println("INFO: " + log); }
    public void debug(String s) {if(DEBUG) System.out.println("[DEBUG] - " + s);}
    public void warn(String s) { System.out.println("[WARNING] - " + s);}
}
