package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.*;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.game.EntityShader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.game.GlobalShader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.game.LiquidShader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ui.HUDShader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ui.SelectedBlockShader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ui.TextShader;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.Game.SHADERS_PATH;
import static org.lwjgl.opengl.GL20.*;

public class ShaderUtils {

    public static Shader SELECTED_BLOCK_SHADER;
    public static Shader ENTITY_SHADER;
    public static Shader GLOBAL_SHADERS;
    public static Shader PLAN_SHADERS;
    public static Shader LIQUID_SHADER;
    public static Shader TEXT_SHADER;

    public static void initShaders(){
        GLOBAL_SHADERS = new GlobalShader();
        PLAN_SHADERS = new HUDShader();
        TEXT_SHADER = new TextShader();
        LIQUID_SHADER = new LiquidShader();
        SELECTED_BLOCK_SHADER = new SelectedBlockShader();
        ENTITY_SHADER = new EntityShader();
    }

    public static int loadShader(String vertexPath, String fragmentPath){
        GAME.debug("Loading shader " + vertexPath + " and " + fragmentPath);
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
                GAME.errorLog("Erreur lors du linkage des shaders.");
                GAME.errorLog(glGetProgramInfoLog(shaderProgram));
                System.exit(-1);
            }

            // Nettoyer
            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);

            return shaderProgram;
        } catch (IOException e) {
            GAME.errorLog(e.getLocalizedMessage());
        }
        return -1;
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
