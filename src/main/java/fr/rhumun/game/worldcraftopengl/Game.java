package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.content.materials.Materials;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.controls.Controls;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.commands.Commands;
import fr.rhumun.game.worldcraftopengl.outputs.audio.AudioManager;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.LoadingGui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.title_menu.TitleMenuGui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.ChunkRenderer;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import fr.rhumun.game.worldcraftopengl.worlds.WorldType;
import fr.rhumun.game.worldcraftopengl.worlds.SaveManager;
import fr.rhumun.game.worldcraftopengl.entities.player.Gamemode;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter @Setter
public class Game {

    public static Game GAME;

    public static String GAME_PATH = "C:\\Users\\eletu\\IdeaProjects\\Worldcraft\\";
    //public static String GAME_PATH = "E:\\Devellopement\\Games\\Worldcraft\\";
    public static int SIMULATION_DISTANCE = 5;
    public static int SHOW_DISTANCE = 14;
    public static int CHUNK_SIZE = 16;
    public static boolean ANTIALIASING = false;
    public static boolean SHOWING_GUIS = true;
    public static boolean SHOWING_FPS = false;
    public static boolean SHOWING_RENDERER_DATA = true;
    public static int GUI_ZOOM = 2;
    public static boolean GENERATION = true;
    public static boolean UPDATE_FRUSTRUM = true;
    public static boolean UPDATE_WORLD_RENDER = true;
    public static boolean ENABLE_VSYNC = true;
    public static boolean GREEDY_MESHING = true;
    public static boolean GL_DEBUG = false;
    public static boolean DEBUG = false;
    public static boolean SHOWING_HITBOXES = false;
    public static int LOD = 6;
    public static final long LAG_SPIKE_LIMIT = 100;

    public static String SHADERS_PATH = GAME_PATH + "src\\main\\java\\fr\\rhumun\\game\\worldcraftopengl\\outputs\\graphic\\shaders\\";
    public static String TEXTURES_PATH = GAME_PATH + "src\\main\\resources\\assets\\";

    GraphicModule graphicModule;
    final AudioManager audioManager;
    final Data data;
    GameLoop gameLoop;
    private final java.util.concurrent.ConcurrentLinkedQueue<Runnable> mainThreadTasks = new java.util.concurrent.ConcurrentLinkedQueue<>();

    GameState gameState = GameState.TITLE;
    boolean isShowingTriangles = false;
    World world;
    Player player;

    java.util.concurrent.CopyOnWriteArrayList<Controls> pressedKeys = new java.util.concurrent.CopyOnWriteArrayList<>();

    public void queueTask(Runnable task) { mainThreadTasks.offer(task); }
    public void runMainThreadTasks() { Runnable r; while ((r = mainThreadTasks.poll()) != null) r.run(); }

    public static void main(String[] args) {
        Game game = new Game();
        game.getGraphicModule().getGuiModule().openGUI(new TitleMenuGui());
        game.getGraphicModule().run();
    }

    public Game(){
        GAME = this;
        Controls.init();
        Texture.init();
        Materials.init();
        Model.init();

        this.data = new Data(this);

        audioManager = new AudioManager(this);
        audioManager.init();

        this.player = new Player();

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
        startGame("My World", Seed.random(), WorldType.NORMAL, Gamemode.SURVIVAL);
    }

    public void startGame(Seed seed){
        startGame("My World", seed, WorldType.NORMAL, Gamemode.SURVIVAL);
    }

    public void startGame(String name, Seed seed){
        startGame(name, seed, WorldType.NORMAL, Gamemode.SURVIVAL);
    }

    public void startGame(String name, Seed seed, WorldType type, Gamemode gamemode){
        var guiModule = this.getGraphicModule().getGuiModule();
        LoadingGui loadingGui = new LoadingGui("Chargement...");
        guiModule.openGUI(loadingGui);

        player.reset();
        player.setGamemode(gamemode);

        new Thread(() -> {
            if(name == null) world = new World(seed, null, type);
            else world = new World(seed, name, type);
            world.load();
            while(!world.isLoaded()) Thread.onSpinWait();

            boolean first = !SaveManager.loadPlayer(world, player);
            if(first) {
                world.spawnPlayer(player);
                player.setGamemode(gamemode);
                player.addItem(new ItemStack(Materials.WOODEN_PICKAXE));
                player.addItem(new ItemStack(Materials.SAWMILL));
                player.addItem(new ItemStack(Materials.PLANKS, Model.STAIRS));
                player.addItem(new ItemStack(Materials.BREAD, 5));
            }

            int slotToSelect = player.getSelectedSlot();

            queueTask(() -> {
                graphicModule.initWorldGraphics();
                // Load initial chunk renderers before closing the loading screen
                player.getLoadedChunksManager().updateChunksGradually();
                for (var chunk : player.getLoadedChunksManager().getChunksToRender()) {
                    ((ChunkRenderer) chunk.getRenderer()).update();
                }
                for (var light : player.getLoadedChunksManager().getChunksToRenderLight()) {
                    light.getRenderer().update();
                }

                gameState = GameState.RUNNING;
                loadingGui.close();
                // synchronize inventory and selected slot with the GUI now that
                // the OpenGL context is current
                player.updateInventory();
                player.setSelectedSlot(slotToSelect);
                startGameLoop();
            });
        }, "WorldLoader").start();
    }

    private void startGameLoop() {
        if(gameLoop != null) return;
        gameLoop = new GameLoop(this, player);
        gameLoop.start();
    }

    public void sendMessage(Player player, String message){
        this.graphicModule.getGuiModule().getChat().println(player.getName() + ": " + message);
    }

    public void quitWorld() {
        var guiModule = this.graphicModule.getGuiModule();
        LoadingGui loadingGui = new LoadingGui("Sauvegarde...");
        guiModule.openGUI(loadingGui);

        World toSave = this.world;

        new Thread(() -> {
            toSave.save();
            queueTask(() -> {
                gameState = GameState.TITLE;
                toSave.removeEntity(player);
                world = null;
                loadingGui.close();
                guiModule.openGUI(new TitleMenuGui());
            });
        }, "WorldSaver").start();
    }

    public void processCommand(String cmd){
        log("processing command " + cmd);
        if(!Commands.execute(player, cmd)) {
            sendMessage(player, "Unknown command");
        }
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
