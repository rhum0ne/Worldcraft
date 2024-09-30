package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils;

import org.joml.Vector3f;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static fr.rhumun.game.worldcraftopengl.Game.SHADERS_PATH;
import static org.lwjgl.opengl.GL20.*;

public class ShaderUtils {

    private static int shaders;

    public static int loadShader(String vertexPath, String fragmentPath) throws IOException {
        String vertexCode = new String(Files.readAllBytes(Paths.get(SHADERS_PATH + vertexPath)));
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

        shaders = shaderProgram;

        return shaderProgram;
    }

    private static int compileShader(String code, int type) {
        int shader = glCreateShader(type);
        glShaderSource(shader, code);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Erreur de compilation du shader.");
            System.err.println(glGetShaderInfoLog(shader));
            System.exit(-1);
        }

        return shader;
    }

    // Méthode pour envoyer un vecteur 3D (vec3) au shader
    public static void setUniform(String uniformName, Vector3f vector) {
        int location = glGetUniformLocation(shaders, uniformName);
        if (location != -1) {
            glUniform3f(location, vector.x, vector.y, vector.z);
        }
    }

    // Méthode pour envoyer un float au shader
    public static void setUniform(String uniformName, float value) {
        int location = glGetUniformLocation(shaders, uniformName);
        if (location != -1) {
            glUniform1f(location, value);
        }
    }

    // Méthode pour envoyer un float au shader
    public static void setUniform(String uniformName, int value) {
        int location = glGetUniformLocation(shaders, uniformName);
        if (location != -1) {
            glUniform1i(location, value);
        }
    }
}
