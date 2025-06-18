package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.content.AnimatedMesh;
import fr.rhumun.game.worldcraftopengl.content.Mesh;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.models.AnimatedModel;
import fr.rhumun.game.worldcraftopengl.entities.Entity;
import fr.rhumun.game.worldcraftopengl.entities.ItemEntity;
import fr.rhumun.game.worldcraftopengl.entities.MobEntity;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.BlockUtil;
import org.lwjgl.opengl.GL30C;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class EntitiesRenderer extends BonesEntityRenderer{

    private boolean useSkinning = false;

    public boolean isUseSkinning() {
        return useSkinning;
    }

    private final Player player;

    public EntitiesRenderer(GraphicModule graphicModule, Player player) {
        super(graphicModule, ShaderManager.ENTITY_SHADER);
        this.player = player;
        this.init();
    }

    public void render(){
        update();
        glBindVertexArray(this.getVAO());

        GL30C.glBindVertexArray(this.getVAO());

        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBufferData(GL_ARRAY_BUFFER, this.getVerticesArray().clone(), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.getIndicesArray().clone(), GL_STATIC_DRAW);

        if(useSkinning){
            float[] mats = new float[16];
            new org.joml.Matrix4f().identity().get(mats);
            ShaderManager.SKINNED_ENTITY_SHADER.setUniformMatrix("boneMatrices", mats);
        }


        glDrawElements(GL_TRIANGLES, this.getIndicesArray().length, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
        this.getIndices().clear();
        this.getVertices().clear();

        glBindVertexArray(0);
    }

    public void update(){
        this.getVertices().clear();
        this.getIndices().clear();
        this.useSkinning = false;
        Iterator<Entity> it = player.getLocation().getWorld().getEntities().iterator();
        Entity e;
        while(it.hasNext()){
            e = it.next();
            if(e == player) continue;
            Model model = e.getModel();
            if(model == null) continue;
            if(model.getModelObject() instanceof AnimatedModel) {
                useSkinning = true;
            }
            if(e instanceof ItemEntity i)
                if(model==Model.BLOCK) BlockUtil.rasterDroppedBlockItem(e.getLocation(), i.getMaterial(), this.getVertices(), this.getIndices());
                else raster(e, model);
            else raster(e, model);
        }
        this.toArrays();
    }

    private void raster(Entity entity, Model model) {
        Mesh obj = model.get();
        if (obj == null) return;

        FloatBuffer verticesBuffer = obj.getVerticesBuffer().duplicate();
        FloatBuffer normalsBuffer = obj.getNormalsBuffer().duplicate();
        FloatBuffer texCoordsBuffer = obj.getTexCoordsBuffer().duplicate();
        IntBuffer indicesBuffer = obj.getIndicesBuffer().duplicate();

        FloatBuffer boneIndices = null;
        FloatBuffer boneWeights = null;
        if (obj instanceof AnimatedMesh am) {
            boneIndices = am.getBoneIndicesBuffer().duplicate();
            boneWeights = am.getBoneWeightsBuffer().duplicate();
        }

        double x = entity.getLocation().getX();
        double y = entity.getLocation().getY();
        float anim = 0f;
        if(entity instanceof MobEntity m) {
            anim = (float) Math.sin(m.getAnimationStep()) * 0.1f;
        }
        double z = entity.getLocation().getZ();

        while (indicesBuffer.hasRemaining()) {
            int vertexIndex = indicesBuffer.get();

            // Normales
            float nx = normalsBuffer.get(vertexIndex * 3);
            float ny = normalsBuffer.get(vertexIndex * 3 + 1);
            float nz = normalsBuffer.get(vertexIndex * 3 + 2);

            // Position du sommet
            float vx = (float) (x + verticesBuffer.get(vertexIndex * 3));
            float vy = (float) (y + verticesBuffer.get(vertexIndex * 3 + 1) + anim);
            float vz = (float) (z + verticesBuffer.get(vertexIndex * 3 + 2));

            int texture = entity.getTextureID();
            // Coordonnées de texture
            float u = texCoordsBuffer.get(vertexIndex * 2);
            float v = texCoordsBuffer.get(vertexIndex * 2 + 1);

            // Ajoute le sommet dans le renderer approprié

            float[] vertexData = new float[17];
            vertexData[0] = vx;
            vertexData[1] = vy;
            vertexData[2] = vz;
            vertexData[3] = u;
            vertexData[4] = v;
            vertexData[5] = texture;
            vertexData[6] = nx;
            vertexData[7] = ny;
            vertexData[8] = nz;

            if(boneIndices != null && boneWeights != null){
                for(int j=0;j<4;j++){
                    vertexData[9+j] = boneIndices.get(vertexIndex*4+j);
                    vertexData[13+j] = boneWeights.get(vertexIndex*4+j);
                }
            }
            this.addVertex(vertexData);
        }
    }

    private void addVertex(float[] vertexData) {
        this.getVertices().add(vertexData);
        this.addIndice();
    }
}
