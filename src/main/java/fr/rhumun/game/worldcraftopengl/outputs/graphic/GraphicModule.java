package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.*;
import fr.rhumun.game.worldcraftopengl.controls.*;
import fr.rhumun.game.worldcraftopengl.controls.event.CursorEvent;
import fr.rhumun.game.worldcraftopengl.controls.event.KeyEvent;
import fr.rhumun.game.worldcraftopengl.controls.event.MouseClickEvent;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.LightningsUtils;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderUtils;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.DebugUtils;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;
import lombok.Setter;
import org.joml.FrustumIntersection;
import org.joml.Vector3f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import static fr.rhumun.game.worldcraftopengl.Game.*;
import static fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.DebugUtils.*;
import static fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.TextureUtils.initTextures;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;  // OpenGL 3.0 pour les VAO
import static org.lwjgl.opengl.GL43C.GL_DEBUG_OUTPUT;
import static org.lwjgl.opengl.GL43C.glDebugMessageCallback;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;
import java.time.Instant;
import java.util.*;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.system.MemoryStack.*;
import org.joml.Matrix4f;

@Getter
@Setter
public class GraphicModule{

    private final Game game;
    private final Camera camera;
    private final Player player;

    private boolean areChunksUpdated = false;
    private Set<Chunk> loadedChunks;
    private final UpdateLoop updateLoop;
    private final Stack<Chunk> chunkToLoad = new Stack<>();
    private final ChunkLoader chunkLoader;

    private long window;
    private final int startWidth = 1200, startHeight = 800;
    private int width = startWidth;
    private int height = startHeight;
    private final CursorEvent cursorEvent;

    private FrustumIntersection frustumIntersection;
    private Matrix4f projectionMatrix;

    private final List<Shader> renderingShaders = new ArrayList<>();
    private final List<Shader> shaders = new ArrayList<>();

    private final DebugUtils debugUtils = new DebugUtils();
    private final LightningsUtils lightningsUtils;
    private GuiModule guiModule;
    private BlockSelector blockSelector;

    private boolean isInitialized = false;
    private boolean isPaused = false;
    public boolean isShowingTriangles = false;


    private int verticesNumber;
    private float liquidTime = 0;
    private double[] xCursorPos = new double[1];
    private double[] yCursorPos = new double[1];

    public GraphicModule(Game game){
        this.game = game;
        player = game.getPlayer();
        this.lightningsUtils = new LightningsUtils(this);
        camera = new Camera(player);
        updateLoop = new UpdateLoop(this, game, player);
        chunkLoader = new ChunkLoader(this, player);
        cursorEvent = new CursorEvent(camera);
    }


    public void run() {
        init();
        loop();


        // Free the window callbacks and destroy the window
        this.cleanup();
        game.getGameLoop().cancel();
        this.updateLoop.interrupt();

        System.exit(0);
    }

    private void init() {

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        Timer timer = new Timer();
        timer.schedule(chunkLoader, Date.from(Instant.now()), 100);

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        //glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE); //Pour macOS
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(startWidth, startHeight, "WorldCraft OpenGL", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");


        GLFW.glfwSetMouseButtonCallback(window, new MouseClickEvent(game));
        GLFW.glfwSetScrollCallback(window, new Scroll(game));
        GLFW.glfwSetFramebufferSizeCallback(window, new ResizeEvent(this));
        GLFW.glfwSetKeyCallback(window, new KeyEvent(game,player));
        GLFW.glfwSetCursorPosCallback(window, cursorEvent);

        // Pour cacher le curseur et activer le mode "FPS"
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            assert vidmode != null;
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        if(Game.ENABLE_VSYNC)
            glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        GL.createCapabilities();
        //this.setDebug(true);
    }

    public void setDebug(boolean state){
        if(state) {
            glEnable(GL_DEBUG_OUTPUT);
            glDebugMessageCallback((source, type, id, severity, length, message, userParam) -> {
                System.err.println("GL CALLBACK: " + GLDebugSeverityToString(severity) + " - " + GLDebugMessageTypeToString(type) + ": " + GLDebugSourceToString(source) + " - " + GLMessageToString(message, length));
            }, NULL);
        }else{
            glDisable(GL_DEBUG_OUTPUT);
        }
    }

    public void updateViewMatrix() {
        if(projectionMatrix == null) return;

        // Mise à jour de la matrice de vue à chaque frame
        Matrix4f viewMatrix = new Matrix4f().lookAt(
                camera.getPos(),       // Position de la caméra mise à jour
                camera.getLookPoint(), // Point de regard mis à jour
                camera.getUp()         // Vecteur "up" pour la caméra
        );

        if(UPDATE_FRUSTRUM) {
            Matrix4f combinedMatrix = new Matrix4f().mul(projectionMatrix).mul(viewMatrix);
            frustumIntersection = new FrustumIntersection(combinedMatrix);
        }

        for(Shader shader : this.renderingShaders) {
            glUseProgram(shader.id);
            int viewLoc = glGetUniformLocation(shader.id, "view");
            glUniformMatrix4fv(viewLoc, false, viewMatrix.get(new float[16]));
        }

        Shader shader = ShaderUtils.SELECTED_BLOCK_SHADER;
        glUseProgram(shader.id);
        int viewLoc = glGetUniformLocation(shader.id, "view");
        glUniformMatrix4fv(viewLoc, false, viewMatrix.get(new float[16]));
    }

    private void loop() {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Délier le VAO (facultatif, mais utile pour éviter des modifications accidentelles)
        //glBindVertexArray(0);

        ShaderUtils.initShaders();

        //glEnable(GL_PROGRAM_POINT_SIZE);

        this.blockSelector = new BlockSelector(this, player);
        blockSelector.init();

        this.renderingShaders.add(ShaderUtils.GLOBAL_SHADERS);
        this.renderingShaders.add(ShaderUtils.LIQUID_SHADER);
        this.shaders.add(ShaderUtils.SELECTED_BLOCK_SHADER);
        this.shaders.add(ShaderUtils.PLAN_SHADERS);
        this.shaders.add(ShaderUtils.GLOBAL_SHADERS);
        this.shaders.add(ShaderUtils.LIQUID_SHADER);

        this.guiModule = new GuiModule(this);
        initTextures();
        this.guiModule.init();

        Matrix4f modelMatrix = new Matrix4f().identity(); // Matrice modèle, ici une identité (sans transformation)
        projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(45.0f), (float) startWidth / startHeight, 0.1f, Game.SHOW_DISTANCE *CHUNK_SIZE);

        this.guiModule.resize(startWidth, startHeight);


        for(Shader shader : this.renderingShaders) {
            updateModelAndProjectionFor(modelMatrix, shader);
        }
        updateModelAndProjectionFor(modelMatrix, ShaderUtils.SELECTED_BLOCK_SHADER);

        updateViewMatrix();

        debugUtils.checkGLError();

        World world = player.getLocation().getWorld();

        for(Shader shader : renderingShaders){
            shader.setUniform("dirLight.direction", new Vector3f(0, -1, 1));
            shader.setUniform("dirLight.ambient", new Vector3f((float) world.getLightColor().getRed(), (float) world.getLightColor().getGreen(), (float) world.getLightColor().getBlue()));
            shader.setUniform("dirLight.diffuse", new Vector3f((float) world.getLightColor().getRed(), (float) world.getLightColor().getGreen(), (float) world.getLightColor().getBlue()));
            shader.setUniform("dirLight.specular", new Vector3f(0.25f, 0.2f, 0.2f));
        }

        this.guiModule.updateInventory(player);

        this.isInitialized = true;

        while ( !glfwWindowShouldClose(window) ) {
            //glClearColor(0.5f, 0.7f, 1.0f, 1.0f);
            glClearColor((float) world.getSkyColor().getRed(), (float) world.getSkyColor().getGreen(), (float) world.getSkyColor().getBlue(), 1.0f);

            if(game.isPaused() != this.isPaused) this.setPaused(game.isPaused());
            if(game.isShowingTriangles() != this.isShowingTriangles) this.setShowingTriangles(game.isShowingTriangles());

            updateWaterTime();

            game.getGraphicModule().updateViewMatrix();
            glUseProgram(ShaderUtils.GLOBAL_SHADERS.id);
            update();

            this.guiModule.render();

            glUseProgram(ShaderUtils.SELECTED_BLOCK_SHADER.id);
            this.blockSelector.render();

            glUseProgram(0);

            // Calculer les FPS
            if(SHOWING_FPS) debugUtils.calculateFPS();

            // Swap des buffers et gestion des événements
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        debugUtils.checkGLError();
    }

    private void updateModelAndProjectionFor(Matrix4f modelMatrix, Shader shader) {
        int projectionLoc = glGetUniformLocation(shader.id, "projection");
        int modelLoc = glGetUniformLocation(shader.id, "model");

// Assurez-vous que le programme de shaders est actif avant de passer les matrices
        glUseProgram(shader.id);

// Passer la matrice de projection au shader
        glUniformMatrix4fv(projectionLoc, false, projectionMatrix.get(new float[16]));

// Passer la matrice de modèle au shader
        glUniformMatrix4fv(modelLoc, false, modelMatrix.get(new float[16]));
    }

    private void updateWaterTime() {
        this.liquidTime += 0.005f;
        if(liquidTime>2*Math.PI) this.liquidTime=0;

        ShaderUtils.LIQUID_SHADER.setUniform("time", this.liquidTime);
    }

    private void update(){

        updateLoop.run();
        //if(!UPDATE_FRUSTRUM) return;

        if(!areChunksUpdated) {
            loadedChunks = new LinkedHashSet<>(game.getPlayer().getSavedChunksManager().getChunksToRender());
            this.areChunksUpdated = true;
            this.lightningsUtils.updateLights();
            if(SHOWING_RENDERER_DATA){
                this.verticesNumber = 0;
                for(Chunk chunk : loadedChunks){
                    this.verticesNumber += chunk.getRenderer().getVerticesNumber();
                }
                game.log("Total vertices: " + this.verticesNumber);
            }
        }

        //loadOneChunk(); TOUT CHARGE MEME SANS CETTE METHODE, A VOIR POURQUOI

        if(loadedChunks.isEmpty()) return;
        this.lightningsUtils.getPointLights().clear();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        float h = game.getWorld().getHeigth();

        for(Chunk chunk : loadedChunks) {
            float x = chunk.getX()*CHUNK_SIZE;
            float z = chunk.getZ()*CHUNK_SIZE;
            if (frustumIntersection.testAab(x, 0f, z, x+CHUNK_SIZE, h , z+CHUNK_SIZE)) {

                chunk.getRenderer().render();
            }
        }
    }

    private void loadOneChunk() {
        if(chunkToLoad.isEmpty()) return;
        Chunk chunk = chunkToLoad.pop();
        //chunk.setToUpdate(true); //To load it.
        //if(!loadedChunks.contains(chunk)) loadOneChunk();
        System.out.println("Loaded chunk " + chunk);
    }


    private void cleanup() {

        this.guiModule.cleanup();

        for(Shader shader : this.renderingShaders)
            glDeleteProgram(shader.id);

        glfwFreeCallbacks(window);
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();


        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void changeLoadedBlocks() {
        this.areChunksUpdated = false;
    }

    public void addChunkToLoad(final Chunk chunk){ this.chunkToLoad.add(chunk); }

    private void setPaused(boolean state) {
        if (state) {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            glfwSetCursorPos(window, width / 2.0, height / 2.0); // Place le curseur au centre de la fenêtre
        } else {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            glfwGetCursorPos(window, this.xCursorPos, this.yCursorPos);
            this.cursorEvent.setLastX((float) this.xCursorPos[0]);
            this.cursorEvent.setLastY((float) this.yCursorPos[0]);
        }
        this.isPaused = state;
    }

    public void setShowingTriangles(boolean b) {
        if(b){
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }else{
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }
        this.isShowingTriangles = b;
    }
}