package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils;

import fr.rhumun.game.worldcraftopengl.blocks.Block;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.PointLight;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL20.glUseProgram;

@Getter
public class LightningsUtils {

    private final GraphicModule graphicModule;
    private final List<Block> pointLights = new ArrayList<>();

    public LightningsUtils(GraphicModule graphicModule) {
        this.graphicModule = graphicModule;
    }



    public void updateLights(){
        pointLights.clear();
        for(Chunk chunk : graphicModule.getLoadedChunks()) {
            if(!chunk.isLoaded()) continue;
            if(chunk.getLightningBlocks().isEmpty()) continue;
            for(Block block : chunk.getLightningBlocks()) {
                if(block.getTick()==0) continue;
                pointLights.add(block);
            }
        }
        sendLight();
    }

    private void sendLight(){
        // Dessiner les éléments existants
        for(Shader shader : graphicModule.getRenderingShaders()){
            glUseProgram(shader.id);

            // Envoie la position de la caméra
            shader.setUniform("viewPos", graphicModule.getCamera().getPos());

            // Envoie le nombre réel de lumières
            shader.setUniform("numPointLights", pointLights.size());
            //System.out.println("There are " + pointLights.size() + " lights");


            for (int i = 0; i < this.pointLights.size(); i++) {

                Block block = this.pointLights.get(i);
                PointLight pointLight = (PointLight) block.getMaterial().getMaterial();

                String uniformName = "pointLights[" + i + "]";
                shader.setUniform(uniformName + ".position", block.getLocation().getPositions());
                shader.setUniform(uniformName + ".ambient", pointLight.ambient);
                shader.setUniform(uniformName + ".diffuse", pointLight.diffuse);
                shader.setUniform(uniformName + ".specular", pointLight.specular);
                shader.setUniform(uniformName + ".constant", pointLight.constant);
                shader.setUniform(uniformName + ".linear", pointLight.linear);
                shader.setUniform(uniformName + ".quadratic", pointLight.quadratic);
            }
        }
    }
}
