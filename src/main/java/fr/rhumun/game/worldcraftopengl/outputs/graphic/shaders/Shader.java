package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.GLStateManager;
import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static org.lwjgl.opengl.GL20.*;

@Getter
public abstract class Shader {
    public int id;
    protected final Map<String, Integer> uniformLocations = new HashMap<>();

    public Shader(int id){
        this.id = id;
    }

    protected int getLocation(String uniformName) {
        Integer cached = uniformLocations.get(uniformName);
        if (cached == null) {
            int loc = glGetUniformLocation(id, uniformName);
            uniformLocations.put(uniformName, loc);
            return loc;
        }
        return cached;
    }

    // Méthode pour envoyer un vecteur 3D (vec3) au shader
    public void setUniform(String uniformName, Vector3f vector) {
        GLStateManager.useProgram(id);
        int location = getLocation(uniformName);
        if (location != -1) {
            glUniform3f(location, vector.x, vector.y, vector.z);
        }
    }

    // Méthode pour envoyer un float au shader
    public void setUniform(String uniformName, float value) {
        GLStateManager.useProgram(id);
        int location = getLocation(uniformName);
        if (location != -1) {
            glUniform1f(location, value);
        }
    }

    // Méthode pour envoyer un float au shader
    public void setUniform(String uniformName, int value) {
        GLStateManager.useProgram(id);
        int location = getLocation(uniformName);
        if (location != -1) {
            glUniform1i(location, value);
        }
    }

    // Méthode pour envoyer un int[] au shader
    public void setUniform(String uniformName, int[] value) {
        GLStateManager.useProgram(id);
        int location = getLocation(uniformName);
        if (location != -1) {
            glUniform1iv(location, value);
        }
    }

    public abstract void init();

    public void setUniform(String uniformName, float[] value) {
        GLStateManager.useProgram(id);
        int location = getLocation(uniformName);
        if (location != -1) {
            glUniform4fv(location, value);
        }
    }

    public void setUniformMatrix(String uniformName, float[] value) {
        GLStateManager.useProgram(id);
        int location = getLocation(uniformName);
        if (location != -1) {
            glUniformMatrix4fv(location, false, value);
        }
    }

    public void setUniform(String name, Matrix4f matrix) {
        GLStateManager.useProgram(id);
        int location = getLocation(name);
        if (location != -1) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer buffer = stack.mallocFloat(16);
                matrix.get(buffer); // met la matrice dans le buffer
                glUniformMatrix4fv(location, false, buffer);
            }
        }
    }

    public void use() {GLStateManager.useProgram(id);}
}
