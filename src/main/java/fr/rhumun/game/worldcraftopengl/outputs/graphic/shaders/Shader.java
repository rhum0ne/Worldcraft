package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders;

import lombok.Getter;
import org.joml.Vector3f;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static org.lwjgl.opengl.GL20.*;

@Getter
public abstract class Shader {
    public int id;

    public Shader(int id){
        this.id = id;
    }

    // Méthode pour envoyer un vecteur 3D (vec3) au shader
    public void setUniform(String uniformName, Vector3f vector) {
        glUseProgram(id);
        int location = glGetUniformLocation(id, uniformName);
        if (location != -1) {
            glUniform3f(location, vector.x, vector.y, vector.z);
        }
    }

    // Méthode pour envoyer un float au shader
    public void setUniform(String uniformName, float value) {
        glUseProgram(id);
        int location = glGetUniformLocation(id, uniformName);
        if (location != -1) {
            glUniform1f(location, value);
        }
    }

    // Méthode pour envoyer un float au shader
    public void setUniform(String uniformName, int value) {
        glUseProgram(id);
        int location = glGetUniformLocation(id, uniformName);
        if (location != -1) {
            glUniform1i(location, value);
        }else GAME.errorLog("Can't find uniform " + uniformName);
    }

    // Méthode pour envoyer un int[] au shader
    public void setUniform(String uniformName, int[] value) {
        glUseProgram(id);
        int location = glGetUniformLocation(id, uniformName);
        if (location != -1) {
            glUniform1iv(location, value);
        }
        else System.err.println("Warning: uniform " + uniformName + " not found");
    }

    public abstract void init();

    public void setUniform(String uniformName, float[] value) {
        glUseProgram(id);
        int location = glGetUniformLocation(id, uniformName);
        if (location != -1) {
            glUniform4fv(location, value);
        }
        else System.err.println("Warning: uniform " + uniformName + " not found");
    }

    public void setUniformMatrix(String uniformName, float[] value) {
        glUseProgram(id);
        int location = glGetUniformLocation(id, uniformName);
        if (location != -1) {
            glUniformMatrix4fv(location, false, value);
        }
        else System.err.println("Warning: uniform " + uniformName + " not found");
    }
}
