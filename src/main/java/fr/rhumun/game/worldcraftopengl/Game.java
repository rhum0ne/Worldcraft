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
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.title_menu.TitleMenuGui;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import fr.rhumun.game.worldcraftopengl.worlds.SaveManager;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter @Setter
public class Game {

    public static Game GAME;

    //public static String GAME_PATH = "C:\\Users\\eletu\\IdeaProjects\\Worldcraft\\";
    public static String GAME_PATH = "E:\\Devellopement\\Games\\Worldcraft\\";
    public static int SIMULATION_DISTANCE = 6;
    public static int SHOW_DISTANCE = 16;
    public static int CHUNK_SIZE = 16;
    public static boolean ANTIALIASING = false;
    public static boolean SHOWING_GUIS = true;
    public static boolean SHOWING_FPS = false;
    public static boolean SHOWING_RENDERER_DATA = true;
    public static int GUI_ZOOM = 2;
    public static boolean GENERATION = true;
    public static boolean UPDATE_FRUSTRUM = true;
    public static boolean UPDATE_WORLD_RENDER = true;
    public static boolean ENABLE_VSYNC = false;
    public static boolean GREEDY_MESHING = true;
    public static boolean GL_DEBUG = false;
    public static boolean DEBUG = false;
    public static int LOD = 4;
    public static final long LAG_SPIKE_LIMIT = 100;

    public static String SHADERS_PATH = GAME_PATH + "src\\main\\java\\fr\\rhumun\\game\\worldcraftopengl\\outputs\\graphic\\shaders\\";
    public static String TEXTURES_PATH = GAME_PATH + "src\\main\\resources\\assets\\";

    GraphicModule graphicModule;
    final AudioManager audioManager;
    final Data data;
    GameLoop gameLoop;

    GameState gameState = GameState.TITLE;
    boolean isShowingTriangles = false;
    World world;
    Player player;

    List<Controls> pressedKeys = new ArrayList<>();

    List<Material> materials;
    public static void main(String[] args) {
        Game game = new Game();
        game.getGraphicModule().getGuiModule().openGUI(new TitleMenuGui());
        game.getGraphicModule().run();
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

        this.world = new World();
        this.player = new Player(this);

        graphicModule = new GraphicModule(this);
        graphicModule.init();
    }

    public void setPaused(boolean b) {
        this.gameState = b ? GameState.PAUSED : GameState.RUNNING;
    }

    public void closeGame(){
        this.gameState = null;
        SaveManager.shutdown();
    }

    public void setPlaying(boolean b){
        if(!b) this.gameState = null;
    }

    public boolean isPaused(){
        return this.gameState != GameState.RUNNING;
    }

    public boolean isPlaying(){
        return this.gameState != null;
    }

    public void startGame(){
        this.world.load();

        while(!world.isLoaded()){ }

        this.world.spawnPlayer(player);

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

        gameLoop = new GameLoop(this, player);
        gameLoop.start();

        this.gameState = GameState.RUNNING;
    }

    public void sendMessage(Player player, String message){
        this.graphicModule.getGuiModule().getChat().println(player.getName() + ": " + message);
    }

    public void processCommand(String cmd){
        log("processing command " + cmd);
        world.addEntity(new OtterEntity(this, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 0, 0));
    }

    public void errorLog(String log){
        System.err.println("ERROR -- at " + getTime());
        System.err.println("Thread : " + Thread.currentThread().getName());
        System.err.println(Arrays.toString(Thread.currentThread().getStackTrace()));
        System.err.println(log + "\n");
    }
    public void errorLog(Exception e) {
        System.err.println("ERROR -- at " + getTime());
        System.err.println("Thread : " + Thread.currentThread().getName());
        e.printStackTrace(); // Meilleur que Arrays.toString(e.getStackTrace())
        System.err.println("\n");
    }
    public void log(String log){ System.out.println("["+ getTime() + "] " + log); }
    public void debug(String s) {if(DEBUG) System.out.println("["+ getTime() + "] [DEBUG] - " + s);}
    public void warn(String s) { System.out.println("[" + getTime() + "] [WARNING] - " + s);}

    private String getTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
