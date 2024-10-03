package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.*;
import fr.rhumun.game.worldcraftopengl.blocks.*;
import fr.rhumun.game.worldcraftopengl.controls.*;
import fr.rhumun.game.worldcraftopengl.controls.event.CursorEvent;
import fr.rhumun.game.worldcraftopengl.controls.event.KeyEvent;
import fr.rhumun.game.worldcraftopengl.controls.event.MouseClickEvent;
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
    public static int shaders = 0;

    // The window handle
    @Getter
    private long window;
    private int VAO, VBO, EBO;

    private FrustumIntersection frustumIntersection;
    Matrix4f projectionMatrix;
    private List<Block> pointLights = new ArrayList<>();


    private final BlocksRenderingData blocksRenderingData = new BlocksRenderingData();
    private final BlocksRenderingData transparentBlocksRenderingData = new BlocksRenderingData();


    private final DebugUtils debugUtils = new DebugUtils();
    private final UpdateLoop updateLoop;
    private final GuiModule guiModule = new GuiModule();

    public GraphicModule(Game game){
        this.game = game;
        player = game.getPlayer();
        camera = new Camera(player);
        updateLoop = new UpdateLoop(this);
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
        VBO = glGenBuffers();
        EBO = glGenBuffers();

        glBindVertexArray(VAO);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

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

        int viewLoc = glGetUniformLocation(shaders, "view");
        glUniformMatrix4fv(viewLoc, false, viewMatrix.get(new float[16]));
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
            shaders = ShaderUtils.loadShader("vertex_shader.glsl", "fragment_shader.glsl");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Matrix4f modelMatrix = new Matrix4f().identity(); // Matrice modèle, ici une identité (sans transformation)
        projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(45.0f), (float) 1200 / 800, 0.1f, 100.0f);

        int projectionLoc = glGetUniformLocation(shaders, "projection");
        int modelLoc = glGetUniformLocation(shaders, "model");



// Assurez-vous que le programme de shaders est actif avant de passer les matrices
        glUseProgram(shaders);

// Passer la matrice de projection au shader
        glUniformMatrix4fv(projectionLoc, false, projectionMatrix.get(new float[16]));

// Passer la matrice de modèle au shader
        glUniformMatrix4fv(modelLoc, false, modelMatrix.get(new float[16]));

        updateViewMatrix();
        initTextures();

// Configuration des attributs de sommet pour position, coordonnées de texture et ID de texture
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, blocksRenderingData.getVerticesArray(), GL_STATIC_DRAW);

// Attribut de position (3 floats)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 9 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

// Attribut de coordonnées de texture (2 floats)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 9 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

// Attribut de l'ID de texture (1 int)
        glVertexAttribPointer(2, 1, GL_FLOAT, false, 9 * Float.BYTES, 5 * Float.BYTES);
        glEnableVertexAttribArray(2);


// Attribut des normales
        glVertexAttribPointer(3, 3, GL_FLOAT, false, 9 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(3);

        // Délier le VAO
        glBindVertexArray(0);

        debugUtils.checkGLError();

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.

        World world = player.getLocation().getWorld();
        ShaderUtils.setUniform("dirLight.direction", new Vector3f(0, -1, 0));
        ShaderUtils.setUniform("dirLight.ambient", new Vector3f((float) world.getLightColor().getRed(), (float) world.getLightColor().getGreen(), (float) world.getLightColor().getBlue()));
        ShaderUtils.setUniform("dirLight.diffuse", new Vector3f((float) world.getLightColor().getRed(), (float) world.getLightColor().getGreen(), (float) world.getLightColor().getBlue()));
        ShaderUtils.setUniform("dirLight.specular", new Vector3f(0, 0, 0));

        while ( !glfwWindowShouldClose(window) ) {
            //glClearColor(0.5f, 0.7f, 1.0f, 1.0f);
            glClearColor((float) world.getSkyColor().getRed(), (float) world.getSkyColor().getGreen(), (float) world.getSkyColor().getBlue(), 1.0f);

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            update();

            render();

            // Calculer les FPS
            if(SHOWING_FPS) debugUtils.calculateFPS();

            // Swap des buffers et gestion des événements
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        debugUtils.checkGLError();
    }

    public void render() {

        // Dessiner les éléments existants
        glUseProgram(shaders);

        // Envoie la position de la caméra
        ShaderUtils.setUniform("viewPos", camera.getPos());

        // Envoie le nombre réel de lumières
        ShaderUtils.setUniform("numPointLights", pointLights.size());
        //System.out.println("There are " + pointLights.size() + " lights");

        for (int i = 0; i < this.pointLights.size(); i++) {
            PointLight pointLight = (PointLight) pointLights.get(i).getMaterial().getMaterial();

            String uniformName = "pointLights[" + i + "]";
            ShaderUtils.setUniform(uniformName + ".position", pointLights.get(i).getLocation().getPositions());
            ShaderUtils.setUniform(uniformName + ".ambient", pointLight.ambient);
            ShaderUtils.setUniform(uniformName + ".diffuse", pointLight.diffuse);
            ShaderUtils.setUniform(uniformName + ".specular", pointLight.specular);
            ShaderUtils.setUniform(uniformName + ".constant", pointLight.constant);
            ShaderUtils.setUniform(uniformName + ".linear", pointLight.linear);
            ShaderUtils.setUniform(uniformName + ".quadratic", pointLight.quadratic);
        }

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, blocksRenderingData.getVerticesArray(), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, blocksRenderingData.getIndicesArray(), GL_STATIC_DRAW);

        glBindVertexArray(VAO);
        glDrawElements(GL_TRIANGLES, blocksRenderingData.getIndice(), GL_UNSIGNED_INT, 0);

        if(transparentBlocksRenderingData.getIndice() != 0){
            glEnable(GL_BLEND);
            //glDepthMask(false);

            glBindBuffer(GL_ARRAY_BUFFER, VBO);
            glBufferData(GL_ARRAY_BUFFER, transparentBlocksRenderingData.getVerticesArray(), GL_STATIC_DRAW);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, transparentBlocksRenderingData.getIndicesArray(), GL_STATIC_DRAW);

            // Dessiner les éléments existants
            glUseProgram(shaders);
            glBindVertexArray(VAO);
            glDrawElements(GL_TRIANGLES, transparentBlocksRenderingData.getIndice(), GL_UNSIGNED_INT, 0);

            //glDepthMask(true);
            glDisable(GL_BLEND);
        }

    }

    private void update(){

        updateLoop.run();
        if(!UPDATE_FRUSTRUM) return;

        if(!areChunksUpdated) {
            loadedChunks = new ArrayList<>(game.getPlayer().getSavedChunksManager().getChunksToRender());
            this.areChunksUpdated = true;
        }

        this.blocksRenderingData.getVertices().clear();
        this.blocksRenderingData.setIndice(0);
        this.transparentBlocksRenderingData.getVertices().clear();
        this.transparentBlocksRenderingData.setIndice(0);

        if(loadedChunks.isEmpty()) return;
        this.pointLights.clear();

        float h = loadedChunks.get(0).getWorld().getHeigth();

        for(Chunk chunk : loadedChunks) {
            float x = chunk.getX()*16;
            float z = chunk.getZ()*16;
            if (frustumIntersection.testAab(x, 0f, z, x+16, h , z+16)) {
                loadChunk(chunk);
            }
        }
        blocksRenderingData.toArrays();
        transparentBlocksRenderingData.toArrays();
    }

    public void loadChunk(final Chunk chunk) {
        //int blockShowDistance = 16*SHOW_DISTANCE;
        for(Block block : chunk.getBlockList()){
            if(block == null || block.getMaterial() == null) continue;
            Location loc = block.getLocation();

            Model model = block.getModel();
            if(model == null) continue;
            MeshArrays mesh = model.get();
            if (mesh == null || mesh.getNumVertices() == 0) continue;


            //if(loc.getDistanceFrom(player.getLocation()) > blockShowDistance) continue;
            if (frustumIntersection.testAab((float) loc.getX()+mesh.getMinX(), (float) (loc.getY()+mesh.getMinY()), (float) loc.getZ()+mesh.getMinZ(),
                    (float) (loc.getX())+mesh.getMaxX(), (float) loc.getY()+mesh.getMaxY(), (float) (loc.getZ())+mesh.getMaxZ())) {
                // Le bloc est dans le frustum, on peut le rasteriser
                if(block.isSurrounded()) continue;
                raster(block, mesh);
            }

        }
    }

    private void raster(Block block, MeshArrays mesh) {

        if(block.getMaterial().getMaterial() instanceof PointLight) this.pointLights.add(block);

        FloatBuffer verticesBuffer = mesh.getVertices();
        FloatBuffer normalsBuffer = mesh.getNormals();
        FloatBuffer texCoordsBuffer = mesh.getTexCoords();

        int numVertices = mesh.getNumVertices();

        double x = block.getLocation().getX();
        double y = block.getLocation().getY();
        double z = block.getLocation().getZ();

        for (int i = 0; i < numVertices; i++) {
            float vx = (float) (x + verticesBuffer.get(i * 3));
            float vy = (float) (y + verticesBuffer.get(i * 3 + 1));
            float vz = (float) (z + verticesBuffer.get(i * 3 + 2));

            float nx = normalsBuffer.get(i * 3);
            float ny = normalsBuffer.get(i * 3 + 1);
            float nz = normalsBuffer.get(i * 3 + 2);

            Vector3f normal = new Vector3f(nx, ny, nz);

            if(!isFaceVisible(normal, new Vector3f(vx, vy, vz)) || hasBlockAtFace(block, nx, ny, nz)) {
                i++;
                i++;
                continue;
            }

            float u = texCoordsBuffer.get(i * 2);
            float v = texCoordsBuffer.get(i * 2 + 1);

            if(block.isOpaque()) addVertex(new float[]{vx, vy, vz, u, v, block.getMaterial().getTextureID(), nx, ny, nz});
            else addTransparentVertex(new float[]{vx, vy, vz, u, v, block.getMaterial().getTextureID(), nx, ny, nz});
        }
    }

    private boolean isFaceVisible(Vector3f normal, Vector3f positions) {
        positions.negate().add(camera.getPos());
        return (positions.dot(normal)>0);
    }

    private boolean hasBlockAtFace(Block block, float nx, float ny, float nz) {
        if(!block.isOpaque()) return false;
        int x = Math.round(nx)+1;
        int y = Math.round(ny)+1;
        int z = Math.round(nz)+1;

        Block face = block.getNextBlocks()[x][y][z];
        return face != null && face.isOpaque();
    }


    private void addVertex(float[] vertexData) {
        blocksRenderingData.getVertices().add(vertexData);

        // L'index du sommet est simplement l'index actuel dans la liste
        // Par exemple, si c'est le 4e sommet qu'on ajoute, son index sera 3
        blocksRenderingData.addIndice();

        //indices.add(index);
    }

    private void addTransparentVertex(float[] vertexData) {
        transparentBlocksRenderingData.getVertices().add(vertexData);

        // L'index du sommet est simplement l'index actuel dans la liste
        // Par exemple, si c'est le 4e sommet qu'on ajoute, son index sera 3
        transparentBlocksRenderingData.addIndice();

        //indices.add(index);
    }


    private void cleanup() {
        // Supprimer les buffers et VAOs existants
        glDeleteVertexArrays(VAO);
        glDeleteBuffers(VBO);
        glDeleteProgram(shaders);

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