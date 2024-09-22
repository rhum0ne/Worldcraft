package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.*;
import fr.rhumun.game.worldcraftopengl.controls.Controls;
import fr.rhumun.game.worldcraftopengl.props.Material;
import fr.rhumun.game.worldcraftopengl.props.Block;
import fr.rhumun.game.worldcraftopengl.props.Model;
import lombok.Getter;
import org.joml.FrustumIntersection;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import static fr.rhumun.game.worldcraftopengl.Game.*;
import static fr.rhumun.game.worldcraftopengl.outputs.graphic.TextureUtils.initTextures;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.opengl.GL30.*;  // OpenGL 3.0 pour les VAO
import static org.lwjgl.system.MemoryUtil.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.nuklear.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.time.Instant;
import java.util.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.system.MemoryStack.*;
import org.joml.Matrix4f;

public class GraphicModule{

    private final Game game;
    @Getter
    private final Camera camera;
    private final Player player;

    private boolean areBlocksLoaded = false;
    private List<Block> loadedBlocks;

    private float lastX = 600.0f, lastY = 400.0f;  // Centre de l'écran
    private boolean firstMouse = true;
    protected static int shaders = 0;

    // The window handle
    private long window;
    private int VAO, VBO, EBO;

    private FrustumIntersection frustumIntersection;
    private Matrix4f projectionMatrix;

    private final List<float[]> vertices = new ArrayList<>();
    private float[] verticesArray = new float[0];

    private final List<Integer> indices = new ArrayList<>();
    int[] indicesArray = new int[0];


    private DebugUtils debugUtils = new DebugUtils();
    private UpdateLoop updateLoop;
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
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(1200, 800, "WorldCraft OpenGL", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        GLFW.glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
                Controls.LEFT_CLICK.press(game.getPlayer());
            }
            if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_PRESS) {
                Controls.RIGHT_CLICK.press(game.getPlayer());
            }
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
                Controls.LEFT_CLICK.release(game.getPlayer());
            }
        });


        // Set scroll callback
        glfwSetScrollCallback(window, new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                game.getPlayer().setSelectedMaterial(game.getMaterials().get((game.getMaterials().indexOf(game.getPlayer().getSelectedMaterial())+1)%game.getMaterials().size()));
            }
        });

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            glViewport(0, 0, width, height);

            // Recalculer la matrice de projection
            projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(45.0f), (float) width / height, 0.1f, 100.0f);

            // Mettre à jour la matrice de projection dans le shader
            glUseProgram(shaders);
            int projectionLoc = glGetUniformLocation(shaders, "projection");
            glUniformMatrix4fv(projectionLoc, false, projectionMatrix.get(new float[16]));
        });


        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            Player player = game.getPlayer();
            if (action == GLFW_PRESS) {
                List<Controls> pressedKeys = game.getPressedKeys();
                if(Controls.exists(key) && !pressedKeys.contains(Controls.get(key))){
                    Controls control = Controls.get(key);
                    pressedKeys.add(control);
                }
            }

            if (action == GLFW_RELEASE) {
                List<Controls> pressedKeys = game.getPressedKeys();
                if(Controls.exists(key) && pressedKeys.contains(Controls.get(key))){
                    Controls control = Controls.get(key);
                    pressedKeys.remove(control);
                    control.release(player);
                }
            }
        });

        // Configuration du callback pour le mouvement de la souris
        glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
            if (firstMouse) {
                lastX = (float) xpos;
                lastY = (float) ypos;
                firstMouse = false;
            }

            float xOffset = (float) xpos - lastX;
            float yOffset = lastY - (float) ypos;  // Inverser l'offset vertical pour correspondre aux conventions OpenGL

            lastX = (float) xpos;
            lastY = (float) ypos;

            // Appel à la méthode pour ajuster la caméra en fonction des mouvements de la souris
            processMouseMovement(xOffset, yOffset);
        });

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

    }

    void updateViewMatrix() {
        // Mise à jour de la matrice de vue à chaque frame
        Matrix4f viewMatrix = new Matrix4f().lookAt(
                camera.getPos(),       // Position de la caméra mise à jour
                camera.getLookPoint(), // Point de regard mis à jour
                camera.getUp()         // Vecteur "up" pour la caméra
        );

        Matrix4f combinedMatrix = new Matrix4f().mul(projectionMatrix).mul(viewMatrix);
        frustumIntersection = new FrustumIntersection(combinedMatrix);

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
        glBufferData(GL_ARRAY_BUFFER, verticesArray, GL_STATIC_DRAW);

// Attribut de position (3 floats)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES + Float.BYTES, 0);
        glEnableVertexAttribArray(0);

// Attribut de coordonnées de texture (2 floats)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES + Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

// Attribut de l'ID de texture (1 int)
        glVertexAttribPointer(2, 1, GL_FLOAT, false, 5 * Float.BYTES + Float.BYTES, 5 * Float.BYTES);
        glEnableVertexAttribArray(2);

        // Délier le VAO
        glBindVertexArray(0);

        debugUtils.checkGLError();

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            glClearColor(0.5f, 0.7f, 1.0f, 1.0f);

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

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, verticesArray, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesArray, GL_STATIC_DRAW);

        // Dessiner les éléments existants
        glUseProgram(shaders);
        glBindVertexArray(VAO);
        glDrawElements(GL_TRIANGLES, indicesArray.length, GL_UNSIGNED_INT, 0);

    }

    private void update(){

        updateLoop.run();
        //updateViewMatrix();
        //System.out.println("updating");
        if(!areBlocksLoaded) {
            loadedBlocks = new ArrayList<>(game.getPlayer().getSavedChunksManager().getLoadedBlocks());
            this.areBlocksLoaded = true;
        }

        this.vertices.clear();
        this.indices.clear();

        int blockShowDistance = 16*SHOW_DISTANCE;
        for(Block block : loadedBlocks){
            if(block == null || block.getMaterial() == null) continue;
            Location loc = block.getLocation();

            Model model = block.getModel();
            if(model == null) continue;
            MeshArrays mesh = model.get();
            if (mesh == null || mesh.getNumVertices() == 0) continue;


            if(loc.getDistanceFrom(player.getLocation()) > blockShowDistance) continue;
            if (frustumIntersection.testAab((float) loc.getX()+mesh.getMinX(), (float) (loc.getY()+mesh.getMinY()), (float) loc.getZ()+mesh.getMinZ(),
                    (float) (loc.getX())+mesh.getMaxX(), (float) loc.getY()+mesh.getMaxY(), (float) (loc.getZ())+mesh.getMaxZ())) {
                // Le bloc est dans le frustum, on peut le rasteriser
                if(block.isSurrounded()) continue;
                raster(block, mesh);
            }

        }

        toArrays();
    }

    private void toArrays(){
        verticesArray= new float[vertices.size()*6];
        int index = 0;
        for (float[] vertex : vertices) {
            for (float v : vertex) {
                verticesArray[index++] = v;
                //if(j%5 == 0 && j!=0) System.out.println("Texture ID " + vertices.get(i)[j]);
            }
        }

        indicesArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
    }

    private void raster(Block block, MeshArrays mesh) {

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

            float u = texCoordsBuffer.get(i * 2);
            float v = texCoordsBuffer.get(i * 2 + 1);

            addVertex(new float[]{vx, vy, vz, u, v, block.getMaterial().getId()});
        }
    }


    private void addVertex(float[] vertexData) {
        // On vérifie que les données du sommet sont bien au format attendu (6 éléments)
        // - 3 éléments pour les coordonnées (x, y, z)
        // - 2 éléments pour les coordonnées de texture (u, v)
        // - 1 élément pour l'ID de la texture (id)
        if (vertexData.length != 6) {
            throw new IllegalArgumentException("Le sommet doit contenir exactement 6 éléments (x, y, z, u, v, textureID)");
        }

        // Ajout des données du sommet dans la liste vertices
        vertices.add(vertexData);

        // L'index du sommet est simplement l'index actuel dans la liste
        // Par exemple, si c'est le 4e sommet qu'on ajoute, son index sera 3
        int index = vertices.size() - 1;

        // Ajouter cet index à la liste des indices
        indices.add(index);
    }

    // Lorsqu'un mouvement de souris est détecté, ajustez les angles de la caméra
    public void processMouseMovement(float xOffset, float yOffset) {
        float sensitivity = 0.1f;  // Sensibilité de la souris
        xOffset *= sensitivity;
        yOffset *= sensitivity;

        camera.setYaw(camera.getYaw() + xOffset);   // Modifier le "yaw" avec l'offset horizontal
        camera.setPitch(camera.getPitch() + yOffset);  // Modifier le "pitch" avec l'offset vertical
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
        this.areBlocksLoaded = false;
    }

}