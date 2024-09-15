package fr.rhumun.game.worldcraftopengl.graphic;

import fr.rhumun.game.worldcraftopengl.*;
import fr.rhumun.game.worldcraftopengl.controls.Control;
import fr.rhumun.game.worldcraftopengl.controls.Controls;
import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import static fr.rhumun.game.worldcraftopengl.Game.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;  // OpenGL 3.0 pour les VAO
import static org.lwjgl.system.MemoryUtil.*;
import org.lwjgl.stb.STBImage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.system.MemoryStack.*;
import org.joml.Matrix4f;

public class GraphicModule {

    private Game game;

    private int shaders = 0;
    private int cobble;

    // The window handle
    private long window;
    private int VAO, VBO, EBO;

    private List<float[]> vertices = new ArrayList<float[]>();
    private float[] verticesArray = new float[0];

    List<Integer> indices = new ArrayList<>();
    int[] indicesArray = new int[0];


    public GraphicModule(Game game){
        this.game = game;
    }


    public void run() {
        init();
        loop();


        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
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
        window = glfwCreateWindow(800, 800, "WorldCraft OpenGL", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            Player player = game.getPlayer();
            if (action == GLFW_PRESS) {
                List<Controls> pressedKeys = game.getPressedKeys();
                if(Controls.exists(key) && !pressedKeys.contains(Controls.get(key))){
                    Controls control = Controls.get(key);
                    pressedKeys.add(control);
                    if(!control.isRepeatable()) control.press(player);
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

            for(Controls control : game.getPressedKeys()) control.press(player);


        });

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


        GL.createCapabilities();

        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();

        glBindVertexArray(VAO);

    }

    private void loop() {

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);


        // Définir le format des données de vertex
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Délier le VAO (facultatif, mais utile pour éviter des modifications accidentelles)
        //glBindVertexArray(0);

        glEnable(GL_DEPTH_TEST);

        try {
            shaders = ShaderUtils.loadShader("vertex_shader.glsl", "fragment_shader.glsl");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cobble = loadTexture(TEXTURES_PATH + "cobble.png");

        Matrix4f projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(45.0f), 800f / 800f, 0.1f, 100.0f);
        Matrix4f modelMatrix = new Matrix4f().identity(); // Matrice modèle, ici une identité (sans transformation)
        Matrix4f viewMatrix = new Matrix4f().lookAt(
                new Vector3f(0.0f, 0.0f, 3.0f), // Position de la caméra
                new Vector3f(0.0f, 0.0f, 0.5f), // Point de regard
                new Vector3f(0.0f, 1.0f, 0.0f)  // Vecteur "up" pour la caméra
        );

        int projectionLoc = glGetUniformLocation(shaders, "projection");
        int modelLoc = glGetUniformLocation(shaders, "model");
        int viewLoc = glGetUniformLocation(shaders, "view");

// Assurez-vous que le programme de shaders est actif avant de passer les matrices
        glUseProgram(shaders);

// Passer la matrice de projection au shader
        glUniformMatrix4fv(projectionLoc, false, projectionMatrix.get(new float[16]));

// Passer la matrice de modèle au shader
        glUniformMatrix4fv(modelLoc, false, modelMatrix.get(new float[16]));

// Passer la matrice de vue au shader
        glUniformMatrix4fv(viewLoc, false, viewMatrix.get(new float[16]));

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, cobble);

        // Filtrage lorsque la texture est agrandie
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        // Filtrage lorsque la texture est réduite
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        int textureLoc = glGetUniformLocation(shaders, "ourTexture");
        glUniform1i(textureLoc, 0);  // L'unité de texture 0 est utilisée

        // Attribut de position des vertices
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);


        // Configurer les attributs de position des sommets
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, verticesArray, GL_STATIC_DRAW);

        // Position des sommets
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Coordonnées de texture
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesArray, GL_STATIC_DRAW);

        // Délier le VAO
        //glBindVertexArray(0);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            update();

            glBindBuffer(GL_ARRAY_BUFFER, VBO);
            glBufferData(GL_ARRAY_BUFFER, verticesArray, GL_STATIC_DRAW);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesArray, GL_STATIC_DRAW);

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            render();

            // Swap des buffers et gestion des événements
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public void render() {
        // Utiliser les shaders
        glUseProgram(shaders);

        // Activer l'unité de texture et lier la texture
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, cobble);

        // Lier le VAO avant de dessiner
        glBindVertexArray(VAO);

        // Dessiner les éléments (triangle, carré, etc.)
        glDrawElements(GL_TRIANGLES, indicesArray.length, GL_UNSIGNED_INT, 0);

        // Délier le VAO après le dessin
        //glBindVertexArray(0);
    }

    public int loadTexture(String path) {
        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Charger l'image
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        ByteBuffer image = STBImage.stbi_load(path, width, height, comp, 4);

        if (image != null) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL_TEXTURE_2D);
            STBImage.stbi_image_free(image);
        } else {
            System.err.println("Erreur lors du chargement de la texture " + path);
        }

        return textureID;
    }

    private void update(){
        /*this.vertices = game.getCobble().getModel().getVertices();
        this.indices = game.getCobble().getModel().getIndices();


        verticesArray = new float[]{
                // Positions          // Couleurs
                0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,  // coin supérieur droit
                0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,  // coin inférieur droit
                -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f   // coin inférieur gauche
        };

        indicesArray = new int[]{
                0, 1, 2  // Dessiner un triangle
        };

        if(true) return;*/


        Block[][][] blocks = game.getBlocks();
        Player player = game.getPlayer();

        this.vertices.clear();
        this.indices.clear();

        for(int x=0; x< blocks.length; x++){
            for(int y=0; y<blocks[x].length; y++){
                for(int z=0; z<blocks[y].length; z++){
                    if(blocks[x][y][z] == null) continue;
                    if(blocks[x][y][z].getLocation().getDistanceFrom(player.getLocation()) > SHOW_DISTANCE) continue;
                    raster(blocks[x][y][z], player);
                }
            }
        }

        toArrays();
    }

    private void toArrays(){
        verticesArray= new float[vertices.size()*5];
        int index = 0;
        for(int i=0; i<vertices.size(); i++){
            for(int j=0; j<vertices.get(i).length; j++) {
                verticesArray[index++] = vertices.get(i)[j];
            }
        }

        indicesArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
    }

    private void raster(Block block, Player player){
        for(int id=0; id<block.getModel().getTriangles().size(); id++){
            //System.out.println("rasting a triangle");
            Vector normal = block.getModel().getNormal(id);

            //Cette condition est trop sévère et retirera des triangles normalement visibles
            //if(normal.scalaire(player.getNormal()) > 0) continue;

            Double[][] triangle = block.getModel().getTriangles().get(id);

            double xP= player.getLocation().getX();
            double yP= player.getLocation().getY();
            double zP= player.getLocation().getZ();

            double[] distances = new double[3];
            float[][] points = new float[5][];

            for (int i = 0; i < 3; i++) {
                double x = block.getLocation().getX() + triangle[i][0] - xP;
                double y = block.getLocation().getY() + triangle[i][1] - yP;
                double z = block.getLocation().getZ() + triangle[i][2] - zP;
                double u = triangle[i][3];
                double v = triangle[i][4];

                points[i] = new float[] {
                        (float) x,
                        (float) y,
                        (float) z,
                        (float) u,
                        (float) v
                };
            }
            addTriangle(points);
        }
    }

    private void addTriangle(float[][] points) {
        // Vérifie que le triangle a bien 3 sommets
        //if (points.length != 3) return;

        //System.out.println("new triangle");
        // Ajoute les 3 sommets du triangle à la liste des triangles
        vertices.add(points[0]); // Premier sommet
        vertices.add(points[1]); // Deuxième sommet
        vertices.add(points[2]); // Troisième sommet

        indices.add(indices.size());
        indices.add(indices.size());
        indices.add(indices.size());
    }

    private double getDistance(double x, double y, double z){
        return Math.sqrt(x*x + y*y + z*z);
    }
}