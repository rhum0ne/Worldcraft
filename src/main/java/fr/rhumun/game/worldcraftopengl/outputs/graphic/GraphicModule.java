package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.*;
import fr.rhumun.game.worldcraftopengl.blocks.*;
import fr.rhumun.game.worldcraftopengl.controls.*;
import fr.rhumun.game.worldcraftopengl.controls.event.CursorEvent;
import fr.rhumun.game.worldcraftopengl.controls.event.KeyEvent;
import fr.rhumun.game.worldcraftopengl.controls.event.MouseClickEvent;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.GlobalRenderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.Renderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.GlobalShader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.DebugUtils;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderUtils;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;
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

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.system.MemoryStack.*;
import org.joml.Matrix4f;

public class GraphicModule{

    private final Game game;
    @Getter
    private final Camera camera;
    private final Player player;

    private boolean areChunksUpdated = false;
    private List<Chunk> loadedChunks;

    // The window handle
    @Getter
    private long window;
    @Getter
    public int VAO;

    private FrustumIntersection frustumIntersection;
    Matrix4f projectionMatrix;
    private List<Block> pointLights = new ArrayList<>();

    @Getter
    private final List<Shader> shaders = new ArrayList<>();

    private final DebugUtils debugUtils = new DebugUtils();
    private final UpdateLoop updateLoop;
    //private final GuiModule guiModule;

    public GraphicModule(Game game){
        this.game = game;
        player = game.getPlayer();
        camera = new Camera(player);
        updateLoop = new UpdateLoop(this);
        //this.guiModule = new GuiModule(this);
    }


    public void run() {
        init();
        loop();


        // Free the window callbacks and destroy the window
        this.cleanup();
    }

    private void init() {

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        //glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE); //Pour macOS
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(1200, 800, "WorldCraft OpenGL", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        GLFW.glfwSetMouseButtonCallback(window, new MouseClickEvent(game));
        GLFW.glfwSetScrollCallback(window, new Scroll());
        GLFW.glfwSetFramebufferSizeCallback(window, new ResizeEvent(this));
        GLFW.glfwSetKeyCallback(window, new KeyEvent(game,player));
        GLFW.glfwSetCursorPosCallback(window, new CursorEvent(camera));

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
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);


        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        VAO = glGenVertexArrays();
        //VBO = glGenBuffers();
        //EBO = glGenBuffers();
//        glEnable(GL_CULL_FACE);
//        glCullFace(GL_BACK);

    }

    void updateViewMatrix() {
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

        for(Shader shader : this.shaders) {
            int viewLoc = glGetUniformLocation(shader.id, "view");
            glUniformMatrix4fv(viewLoc, false, viewMatrix.get(new float[16]));
        }
    }

    private void loop() {
//        glEnable(GL_CULL_FACE);
//        glCullFace(GL_BACK);
//        glFrontFace(GL_CCW);  // GL_CW pour le sens horaire si vos triangles sont définis différemment


        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Délier le VAO (facultatif, mais utile pour éviter des modifications accidentelles)
        //glBindVertexArray(0);

        glEnable(GL_DEPTH_TEST);

        try {
            Shader globalShader = new GlobalShader();
            this.shaders.add(globalShader);

//            this.globalRenderer = new GlobalRenderer(this, globalShader.id);
//            this.transparentBlocksRenderer = new GlobalRenderer(this, globalShader.id);
//            this.renderers.add(globalRenderer);
//            this.renderers.add(transparentBlocksRenderer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        for(Renderer renderer : this.renderers) {
//            renderer.init();
//        }

        Matrix4f modelMatrix = new Matrix4f().identity(); // Matrice modèle, ici une identité (sans transformation)
        projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(45.0f), (float) 1200 / 800, 0.1f, 100.0f);

        for(Shader shader : this.shaders) {
            int projectionLoc = glGetUniformLocation(shader.id, "projection");
            int modelLoc = glGetUniformLocation(shader.id, "model");

// Assurez-vous que le programme de shaders est actif avant de passer les matrices
            glUseProgram(shader.id);

// Passer la matrice de projection au shader
            glUniformMatrix4fv(projectionLoc, false, projectionMatrix.get(new float[16]));

// Passer la matrice de modèle au shader
            glUniformMatrix4fv(modelLoc, false, modelMatrix.get(new float[16]));
        }

        updateViewMatrix();
        initTextures();

        debugUtils.checkGLError();

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.

        World world = player.getLocation().getWorld();

        for(Shader shader : shaders){
            shader.setUniform("dirLight.direction", new Vector3f(0, -1, 0));
            shader.setUniform("dirLight.ambient", new Vector3f((float) world.getLightColor().getRed(), (float) world.getLightColor().getGreen(), (float) world.getLightColor().getBlue()));
            shader.setUniform("dirLight.diffuse", new Vector3f((float) world.getLightColor().getRed(), (float) world.getLightColor().getGreen(), (float) world.getLightColor().getBlue()));
            shader.setUniform("dirLight.specular", new Vector3f(0, 0, 0));
        }

        while ( !glfwWindowShouldClose(window) ) {
            //glClearColor(0.5f, 0.7f, 1.0f, 1.0f);
            glClearColor((float) world.getSkyColor().getRed(), (float) world.getSkyColor().getGreen(), (float) world.getSkyColor().getBlue(), 1.0f);

            update();

            //guiModule.renderGui();

            // Calculer les FPS
            if(SHOWING_FPS) debugUtils.calculateFPS();

            // Swap des buffers et gestion des événements
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        debugUtils.checkGLError();
    }

    public void render(Chunk chunk) {
        glUseProgram(shaders.get(0).id);
        chunk.getRenderer().render();
        //System.out.println("Rendering chunk " + chunk);
    }

    public void updateLights(){
        pointLights.clear();
        for(Chunk chunk : loadedChunks) {
            if(chunk.getLightningBlocks().isEmpty()) continue;
            pointLights.addAll(chunk.getLightningBlocks());
        }
        sendLight();
    }

    private void sendLight(){
        // Dessiner les éléments existants
        for(Shader shader : shaders){
            glUseProgram(shader.id);

            // Envoie la position de la caméra
            shader.setUniform("viewPos", camera.getPos());

            // Envoie le nombre réel de lumières
            shader.setUniform("numPointLights", pointLights.size());
            //System.out.println("There are " + pointLights.size() + " lights");

            for (int i = 0; i < this.pointLights.size(); i++) {
                PointLight pointLight = (PointLight) pointLights.get(i).getMaterial().getMaterial();

                String uniformName = "pointLights[" + i + "]";
                shader.setUniform(uniformName + ".position", pointLights.get(i).getLocation().getPositions());
                shader.setUniform(uniformName + ".ambient", pointLight.ambient);
                shader.setUniform(uniformName + ".diffuse", pointLight.diffuse);
                shader.setUniform(uniformName + ".specular", pointLight.specular);
                shader.setUniform(uniformName + ".constant", pointLight.constant);
                shader.setUniform(uniformName + ".linear", pointLight.linear);
                shader.setUniform(uniformName + ".quadratic", pointLight.quadratic);
            }
        }
    }

    private void update(){

        updateLoop.run();
        //if(!UPDATE_FRUSTRUM) return;

        if(!areChunksUpdated) {
            loadedChunks = new ArrayList<>(game.getPlayer().getSavedChunksManager().getChunksToRender());
            this.areChunksUpdated = true;
            updateLights();
        }

        if(loadedChunks.isEmpty()) return;
        this.pointLights.clear();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        float h = loadedChunks.get(0).getWorld().getHeigth();

        glBindVertexArray(VAO);

        for(Chunk chunk : loadedChunks) {
            float x = chunk.getX()*16;
            float z = chunk.getZ()*16;
            if (frustumIntersection.testAab(x, 0f, z, x+16, h , z+16)) {
                render(chunk);
            }
        }

        glBindVertexArray(0);
    }


    private void cleanup() {
        // Supprimer les buffers et VAOs existants
        glDeleteVertexArrays(VAO);
        //glDeleteBuffers(VBO);
        for(Shader shader : this.shaders)
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

}