package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.content.Mesh;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.entities.MobEntity;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;
import org.lwjgl.opengl.GL30C;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class MobEntitiesRenderer extends GlobalRenderer {

    private final Player player;

    public MobEntitiesRenderer(GraphicModule graphicModule, Player player) {
        super(graphicModule, ShaderManager.ANIMATED_ENTITY_SHADER, true);
        this.player = player;
        this.init();
    }

    public void render() {
        update();

        glBindVertexArray(this.getVAO());
        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        fillBuffers();
        glBufferData(GL_ARRAY_BUFFER, this.getVerticesBuffer(), GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.getIndicesBuffer(), GL_DYNAMIC_DRAW);
//
        glDrawElements(GL_TRIANGLES, this.getIndicesArray().length, GL_UNSIGNED_INT, 0);
//
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
//
        this.getIndices().clear();
        this.getVertices().clear();
    }

    public void update() {
        this.getVertices().clear();
        this.getIndices().clear();

        Iterator<MobEntity> it = player.getLocation().getWorld().getEntities().stream()
                .filter(e -> e instanceof MobEntity)
                .map(e -> (MobEntity) e)
                .iterator();

        while (it.hasNext()) {
            MobEntity mob = it.next();
            Model model = mob.getModel();
            if (model == null || mob.getAnimator() == null) continue;

            ShaderManager.ANIMATED_ENTITY_SHADER.use();
            mob.getAnimator().sendToShader(ShaderManager.ANIMATED_ENTITY_SHADER);

            raster(mob, model);
        }

        this.toArrays();
    }

    private void raster(MobEntity entity, Model model) {
        Mesh obj = model.get();
        if (obj == null) return;

        FloatBuffer vBuf = obj.getVerticesBuffer().duplicate();
        FloatBuffer nBuf = obj.getNormalsBuffer().duplicate();
        FloatBuffer tBuf = obj.getTexCoordsBuffer().duplicate();
        IntBuffer iBuf = obj.getIndicesBuffer().duplicate();
        IntBuffer boneBuf = obj.getBoneIDsBuffer().duplicate();

        float yawRad = (float) Math.toRadians(entity.getLocation().getYaw());
        float cosYaw = (float) Math.cos(yawRad);
        float sinYaw = (float) Math.sin(yawRad);

        double x = entity.getLocation().getX();
        double y = entity.getLocation().getY();
        double z = entity.getLocation().getZ();

        while (iBuf.hasRemaining()) {
            int idx = iBuf.get();

            float relX = vBuf.get(idx * 3);
            float relY = vBuf.get(idx * 3 + 1);
            float relZ = vBuf.get(idx * 3 + 2);

            float rotX = cosYaw * relX - sinYaw * relZ;
            float rotZ = sinYaw * relX + cosYaw * relZ;

            float vx = (float) (x + rotX);
            float vy = (float) (y + relY);
            float vz = (float) (z + rotZ);

            float nx = nBuf.get(idx * 3);
            float ny = nBuf.get(idx * 3 + 1);
            float nz = nBuf.get(idx * 3 + 2);

            float rnx = cosYaw * nx - sinYaw * nz;
            float rnz = sinYaw * nx + cosYaw * nz;

            float u = tBuf.get(idx * 2);
            float v = tBuf.get(idx * 2 + 1);

            int boneID = boneBuf.get(idx);
            int texture = entity.getTextureID();

            float[] vertex = new float[]{vx, vy, vz, u, v, texture, rnx, ny, rnz, boneID};
            this.addVertex(vertex);
        }
    }

    private void addVertex(float[] vertexData) {
        this.getVertices().add(vertexData);
        this.addIndice();
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }
}
