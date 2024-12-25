package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.Game.SHADERS_PATH;
import static org.lwjgl.opengl.GL20.*;

public class ShaderUtils {

    public static Shader GLOBAL_SHADERS;
    public static Shader PLAN_SHADERS;

    public static void initShaders(){
        GLOBAL_SHADERS = new GlobalShader();
        PLAN_SHADERS = new HUDShader();
    }

    public static int loadShader(String vertexPath, String fragmentPath){
        System.out.println("Loading shader " + vertexPath + " and " + fragmentPath);
        String vertexCode = null;
        try {
            vertexCode = new String(Files.readAllBytes(Paths.get(SHADERS_PATH + vertexPath)));
            String fragmentCode = new String(Files.readAllBytes(Paths.get(SHADERS_PATH + fragmentPath)));

            int vertexShader = compileShader(vertexCode, GL_VERTEX_SHADER);
            int fragmentShader = compileShader(fragmentCode, GL_FRAGMENT_SHADER);

            // Créer un programme shader
            int shaderProgram = glCreateProgram();
            glAttachShader(shaderProgram, vertexShader);
            glAttachShader(shaderProgram, fragmentShader);
            glLinkProgram(shaderProgram);

            // Vérifier les erreurs de linkage
            if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
                System.err.println("Erreur lors du linkage des shaders.");
                System.err.println(glGetProgramInfoLog(shaderProgram));
                System.exit(-1);
            }

            // Nettoyer
            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);

            return shaderProgram;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int compileShader(String code, int type) {
        int shader = glCreateShader(type);
        glShaderSource(shader, code);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            String errorMessage = GL20.glGetShaderInfoLog(shader, 1024);
            GAME.errorLog("Shader Compilation Failed: " + errorMessage);
            throw new RuntimeException("Shader compilation failed.");
        }

        return shader;
    }
}
