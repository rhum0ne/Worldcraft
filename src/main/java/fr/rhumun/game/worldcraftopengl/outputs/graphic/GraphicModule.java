package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.*;
import fr.rhumun.game.worldcraftopengl.GameState;
import fr.rhumun.game.worldcraftopengl.controls.*;
import fr.rhumun.game.worldcraftopengl.controls.event.CursorEvent;
import fr.rhumun.game.worldcraftopengl.controls.event.KeyEvent;
import fr.rhumun.game.worldcraftopengl.controls.event.CharEvent;
import fr.rhumun.game.worldcraftopengl.controls.event.MouseClickEvent;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.*;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.EntitiesRenderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.HitboxRenderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.Renderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.ChunkRenderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ui.SelectedBlockShader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.LightningsUtils;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.DebugUtils;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.GLStateManager;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.LightChunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;
import lombok.Setter;
import org.joml.FrustumIntersection;
import org.joml.Vector3f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import static fr.rhumun.game.worldcraftopengl.Game.*;
import static fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.TextureUtils.initTextures;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;  // OpenGL 3.0 pour les VAO
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;
import java.util.*;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.system.MemoryStack.*;
import org.joml.Matrix4f;

@Getter
@Setter
public class GraphicModule {

    // == Core references ==
    private final Game game;
    private final Camera camera;
    private final Player player;
    private World world;

    // == GLFW / Window ==
    private long window;
    private final int startWidth = 1200, startHeight = 800;
    private int width = startWidth, height = startHeight;
    private final CursorEvent cursorEvent;
    private final double[] xCursorPos = new double[1];
    private final double[] yCursorPos = new double[1];

    // == OpenGL / Matrices ==
    private FrustumIntersection frustumIntersection;
    private Matrix4f projectionMatrix;
    private final float[] matrixBuffer = new float[16];

    // == Shaders and rendering ==
    private final List<Shader> renderingShaders = new ArrayList<>();
    private final List<Shader> shaders = new ArrayList<>();
    private final DebugUtils debugUtils = new DebugUtils();
    private final LightningsUtils lightningsUtils;
    private int verticesNumber;
    private float liquidTime = 0;

    // == Modules ==
    private GuiModule guiModule;
    private BlockSelector blockSelector;
    private BreakingBlockRenderer breakingRenderer;
    private final CleanerModule cleaner;
    private EntitiesRenderer entitiesRenderer;
    private MobEntitiesRenderer animatedEntitiesRenderer;
    private HitboxRenderer hitboxRenderer;
    private CelestialRenderer celestialRenderer;
    private final UpdateLoop updateLoop;
    private final ChunkLoader chunkLoader;

    // == Chunk rendering ==
    private boolean areChunksUpdated = false;
    private Set<Chunk> loadedChunks;
    private Set<LightChunk> loadedFarChunks;

    // == State ==
    private boolean isInitialized = false;
    private boolean isPaused = true;
    public boolean isShowingTriangles = false;

    public GraphicModule(Game game) {
        this.game = game;
        this.player = game.getPlayer();
        this.camera = new Camera(player);
        this.cursorEvent = new CursorEvent(camera);
        this.lightningsUtils = new LightningsUtils(this);
        this.cleaner = new CleanerModule(this);
        this.updateLoop = new UpdateLoop(this, game, player);
        this.chunkLoader = new ChunkLoader(this, player);
    }

    public void run() {
        try {
            loop();
        } catch (Exception e) {
            game.errorLog(e);
        }
        cleanup();
        game.setPlaying(false);
        updateLoop.interrupt();
        System.exit(0);
    }

    public void init() {
        initGLFW();
        createWindow();
        setupOpenGL();
        configureShaders();
        loadResources();
        configureUI();
        initWorldGraphics();
        startChunkLoader();
        isInitialized = true;
    }

    public void initWorldGraphics() {
        this.world = game.getWorld();
        if(game.getWorld() == null) return;

        configureLighting();
        ShaderManager.LIQUID_SHADER.setUniform("waterHigh", world.getGenerator().getWaterHigh());
    }

    private void initGLFW() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
    }

    private void createWindow() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 3);

        window = glfwCreateWindow(startWidth, startHeight, "WorldCraft", NULL, NULL);
        if (window == NULL) throw new RuntimeException("Failed to create GLFW window");

        glfwSetMouseButtonCallback(window, new MouseClickEvent(game));
        glfwSetScrollCallback(window, new Scroll(game));
        glfwSetFramebufferSizeCallback(window, new ResizeEvent(this));
        glfwSetKeyCallback(window, new KeyEvent(game, player));
        glfwSetCharCallback(window, new CharEvent(game));
        glfwSetCursorPosCallback(window, cursorEvent);

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
        }

        glfwMakeContextCurrent(window);
        if (ENABLE_VSYNC) glfwSwapInterval(1);
        glfwShowWindow(window);
        GL.createCapabilities();
    }

    private void setupOpenGL() {
        debugUtils.setDebug(GL_DEBUG);
        GLStateManager.enable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        if (ANTIALIASING) GLStateManager.enable(GL_MULTISAMPLE);
        projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(45.0f), (float) startWidth / startHeight, 0.1f, SHOW_DISTANCE * CHUNK_SIZE);
    }

    private void loadResources() {
        initTextures();
    }

    private void configureShaders() {
        ShaderManager.initShaders();
        blockSelector = new BlockSelector(this, player);
        blockSelector.init();
        breakingRenderer = new BreakingBlockRenderer(this, player);

        renderingShaders.addAll(List.of(
                ShaderManager.GLOBAL_SHADERS,
                ShaderManager.LIQUID_SHADER,
                ShaderManager.ENTITY_SHADER,
                ShaderManager.ANIMATED_ENTITY_SHADER,
                ShaderManager.CELESTIAL_SHADER
        ));
        shaders.addAll(List.of(
                ShaderManager.SELECTED_BLOCK_SHADER,
                ShaderManager.PLAN_SHADERS,
                ShaderManager.GLOBAL_SHADERS,
                ShaderManager.LIQUID_SHADER,
                ShaderManager.ENTITY_SHADER,
                ShaderManager.ANIMATED_ENTITY_SHADER,
                ShaderManager.FAR_SHADER,
                ShaderManager.CELESTIAL_SHADER
        ));
        Matrix4f modelMatrix = new Matrix4f().identity();
        for (Shader shader : renderingShaders) updateModelAndProjectionFor(modelMatrix, shader);
        updateModelAndProjectionFor(modelMatrix, ShaderManager.SELECTED_BLOCK_SHADER);
        updateModelAndProjectionFor(modelMatrix, ShaderManager.FAR_SHADER);
    }

    private void configureUI() {
        this.guiModule = new GuiModule(this);
        this.guiModule.init();
        this.guiModule.resize(startWidth, startHeight);
        this.entitiesRenderer = new EntitiesRenderer(this, player);
        this.animatedEntitiesRenderer = new MobEntitiesRenderer(this, player);
        this.hitboxRenderer = new HitboxRenderer(this, player);
        this.hitboxRenderer.init();
        this.celestialRenderer = new CelestialRenderer(this);
        this.celestialRenderer.init();
    }

    private void configureLighting() {
        for (Shader shader : renderingShaders) setSunLight(shader);
        setSunLight(ShaderManager.FAR_SHADER);
        this.guiModule.updateInventory(player);
    }

    private void setSunLight(Shader shader) {
        shader.setUniform("dirLight.direction", world.getLightDirection());
        Vector3f color = new Vector3f(
                (float) world.getLightColor().getRed(),
                (float) world.getLightColor().getGreen(),
                (float) world.getLightColor().getBlue()
        );
        shader.setUniform("dirLight.ambient", color);
        shader.setUniform("dirLight.diffuse", color);
        shader.setUniform("dirLight.specular", new Vector3f(0.25f, 0.2f, 0.2f));
    }

    private void updateModelAndProjectionFor(Matrix4f modelMatrix, Shader shader) {
        shader.setUniformMatrix("projection", projectionMatrix.get(matrixBuffer));
        shader.setUniformMatrix("model", modelMatrix.get(matrixBuffer));
    }

    private void startChunkLoader() {
        Timer timer = new Timer();
        timer.schedule(chunkLoader, 0, 100);
    }

    public void changeLoadedBlocks() {
        this.areChunksUpdated = false;
    }

    private void refreshLoadedChunks() {
        loadedChunks = new LinkedHashSet<>(player.getLoadedChunksManager().getChunksToRender());
        loadedFarChunks = new LinkedHashSet<>(player.getLoadedChunksManager().getChunksToRenderLight());
        areChunksUpdated = true;
    }

    public void cleanup(Renderer renderer) {
        this.cleaner.add(renderer);
    }

    public void setShowingTriangles(boolean b) {
        glPolygonMode(GL_FRONT_AND_BACK, b ? GL_LINE : GL_FILL);
        this.isShowingTriangles = b;
    }

    private void updateWaterTime() {
        liquidTime += 0.005f;
        if (liquidTime > 2 * Math.PI) liquidTime = 0;
        ShaderManager.LIQUID_SHADER.setUniform("time", liquidTime);
    }

    private void setPaused(boolean state) {
        if (state) {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            glfwSetCursorPos(window, width / 2.0, height / 2.0);
        } else {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            glfwGetCursorPos(window, xCursorPos, yCursorPos);
            cursorEvent.setLastX((float) xCursorPos[0]);
            cursorEvent.setLastY((float) yCursorPos[0]);
        }
        this.isPaused = state;
    }

    public void updateViewMatrix() {
        if (projectionMatrix == null) return;

        Matrix4f viewMatrix = new Matrix4f().lookAt(camera.getPos(), camera.getLookPoint(), camera.getUp());

        if (UPDATE_FRUSTRUM && UPDATE_WORLD_RENDER) {
            Matrix4f combinedMatrix = new Matrix4f().mul(projectionMatrix).mul(viewMatrix);
            frustumIntersection = new FrustumIntersection(combinedMatrix);
        }

        viewMatrix.get(matrixBuffer);
        for (Shader shader : renderingShaders)
            shader.setUniformMatrix("view", matrixBuffer);

        ShaderManager.SELECTED_BLOCK_SHADER.setUniformMatrix("view", matrixBuffer);
        ShaderManager.FAR_SHADER.setUniformMatrix("view", matrixBuffer);

    }

    private void loop() {
        while (!glfwWindowShouldClose(window) && game.isPlaying()) {
            game.runMainThreadTasks();
            switch(game.getGameState()) {
                case GameState.RUNNING, GameState.PAUSED -> this.renderGame();
                case GameState.TITLE -> this.renderGuiOnly();
            }

            GLStateManager.useProgram(0);
            if (SHOWING_FPS) debugUtils.calculateFPS();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        debugUtils.checkGLError();
    }

    private void renderGuiOnly() {
        glClearColor(0, 0, 0, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        cleaner.clean();

        if (isShowingTriangles) setShowingTriangles(false);
        this.guiModule.render();
    }

    private void renderGame() {
        glClearColor((float) world.getSkyColor().getRed(), (float) world.getSkyColor().getGreen(), (float) world.getSkyColor().getBlue(), 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        cleaner.clean();

        for (Shader shader : renderingShaders) setSunLight(shader);
        setSunLight(ShaderManager.FAR_SHADER);

        if (game.isPaused() != isPaused) setPaused(game.isPaused());
        if (game.isShowingTriangles() != isShowingTriangles) setShowingTriangles(game.isShowingTriangles());

        Vector3f camPos = getCamera().getPos();
        ShaderManager.CELESTIAL_SHADER.setUniform("camPos", camPos);

        //updateWaterTime();
        updateViewMatrix();

        GLStateManager.useProgram(ShaderManager.CELESTIAL_SHADER.id);
        GLStateManager.disable(GL_DEPTH_TEST);
        GLStateManager.disable(GL_CULL_FACE);
        GLStateManager.enable(GL_BLEND);
        ShaderManager.CELESTIAL_SHADER.setUniform("angle", world.getCelestialAngle());
        celestialRenderer.render();
        GLStateManager.enable(GL_DEPTH_TEST);
        GLStateManager.enable(GL_CULL_FACE);
        GLStateManager.disable(GL_BLEND);
        GLStateManager.useProgram(0);

        GLStateManager.useProgram(ShaderManager.FAR_SHADER.id);
        updateFarChunks();
        GLStateManager.useProgram(0);

        update();

        GLStateManager.useProgram(ShaderManager.ENTITY_SHADER.id);
        entitiesRenderer.render();
        GLStateManager.useProgram(0);

        GLStateManager.useProgram(ShaderManager.ANIMATED_ENTITY_SHADER.id);
        animatedEntitiesRenderer.render();
        GLStateManager.useProgram(0);

        GLStateManager.enable(GL_BLEND);
        GLStateManager.useProgram(ShaderManager.GLOBAL_SHADERS.id);
        breakingRenderer.render();
        GLStateManager.useProgram(0);
        GLStateManager.disable(GL_BLEND);

        GLStateManager.useProgram(ShaderManager.SELECTED_BLOCK_SHADER.id);
        blockSelector.render();
        hitboxRenderer.render();
        GLStateManager.useProgram(0);

        guiModule.render();
    }

    private void update() {
        if (!areChunksUpdated && UPDATE_WORLD_RENDER) {
            refreshLoadedChunks();
            lightningsUtils.updateLights();
            if (SHOWING_RENDERER_DATA) {
                verticesNumber = loadedChunks.stream().mapToInt(c -> c.getRenderer().getVerticesNumber()).sum();
                game.getData().setVerticesCount(verticesNumber);
            }
        }

        if (loadedChunks.isEmpty()) return;
        lightningsUtils.getPointLights().clear();

        GLStateManager.enable(GL_DEPTH_TEST);

        float h = world.getHeigth();

        List<Chunk> visibleChunks = new ArrayList<>();
        for (Chunk chunk : loadedChunks) {
            float x = chunk.getX() * CHUNK_SIZE;
            float z = chunk.getZ() * CHUNK_SIZE;
            if (frustumIntersection.testAab(x, 0f, z, x + CHUNK_SIZE, h, z + CHUNK_SIZE)) {
                visibleChunks.add(chunk);
            }
        }

        for (Chunk chunk : visibleChunks) {
            if (chunk.isToUpdate())
                ((ChunkRenderer) chunk.getRenderer()).update();
        }

        GLStateManager.useProgram(ShaderManager.GLOBAL_SHADERS.id);
        for (Chunk chunk : visibleChunks) {
            ((ChunkRenderer) chunk.getRenderer()).renderOpaque();
        }
        GLStateManager.useProgram(0);

        GLStateManager.enable(GL_BLEND);
        GLStateManager.useProgram(ShaderManager.LIQUID_SHADER.id);
        for (Chunk chunk : visibleChunks) {
            ((ChunkRenderer) chunk.getRenderer()).renderLiquids();
        }
        GLStateManager.disable(GL_BLEND);
        GLStateManager.useProgram(0);

        GLStateManager.enable(GL_BLEND);
        GLStateManager.useProgram(ShaderManager.GLOBAL_SHADERS.id);
        for (Chunk chunk : visibleChunks) {
            ((ChunkRenderer) chunk.getRenderer()).renderTransparent();
        }
        GLStateManager.disable(GL_BLEND);

        GLStateManager.enable(GL_BLEND);
        for (Chunk chunk : visibleChunks) {
            ((ChunkRenderer) chunk.getRenderer()).renderCloseTransparent();
        }
        GLStateManager.disable(GL_BLEND);
        GLStateManager.useProgram(0);
    }

    private void updateFarChunks() {
        if (!areChunksUpdated && UPDATE_WORLD_RENDER) {
            refreshLoadedChunks();
            if (SHOWING_RENDERER_DATA) {
                verticesNumber = loadedFarChunks.stream().mapToInt(c -> c.getRenderer().getVerticesNumber()).sum();
                game.getData().setVerticesCount(verticesNumber);
            }
        }
        if (loadedChunks.isEmpty()) return;

        float h = world.getHeigth();
        for (LightChunk chunk : loadedFarChunks) {
            float x = chunk.getX() * CHUNK_SIZE;
            float z = chunk.getZ() * CHUNK_SIZE;
            if (frustumIntersection.testAab(x, 0f, z, x + CHUNK_SIZE, h, z + CHUNK_SIZE)) {
                chunk.getRenderer().render();
            }
        }
    }

    private void cleanup() {
        guiModule.cleanup();
        renderingShaders.forEach(shader -> glDeleteProgram(shader.id));
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
