package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.content.Mesh;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.entities.MobEntity;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;
import org.joml.Matrix4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
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
        Iterator<MobEntity> it = player.getLocation().getWorld().getEntities().stream()
                .filter(e -> e instanceof MobEntity)
                .map(e -> (MobEntity) e)
                .iterator();

        while (it.hasNext()) {
            MobEntity mob = it.next();
            Model model = mob.getModel();
            if (model == null || mob.getAnimator() == null) continue;

            mob.getAnimator().update(1f / 60f);

            this.getVertices().clear();
            this.getIndices().clear();

            raster(mob, model);
            this.toArrays();

            Matrix4f transform = new Matrix4f()
                    .translate((float) mob.getLocation().getX(), (float) mob.getLocation().getY(), (float) mob.getLocation().getZ())
                    .rotateY((float) Math.toRadians(mob.getLocation().getYaw()))
                    .scale(0.05f);

            ShaderManager.ANIMATED_ENTITY_SHADER.use();
            mob.getAnimator().sendToShader(ShaderManager.ANIMATED_ENTITY_SHADER, transform);

            glBindVertexArray(this.getVAO());
            glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
            fillBuffers();
            glBufferData(GL_ARRAY_BUFFER, this.getVerticesBuffer(), GL_DYNAMIC_DRAW);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.getIndicesBuffer(), GL_DYNAMIC_DRAW);
            glDrawElements(GL_TRIANGLES, this.getIndicesArray().length, GL_UNSIGNED_INT, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }
    }

    private void raster(MobEntity entity, Model model) {
        Mesh obj = model.get();
        if (obj == null) return;

        FloatBuffer vBuf = obj.getVerticesBuffer().duplicate();
        FloatBuffer nBuf = obj.getNormalsBuffer().duplicate();
        FloatBuffer tBuf = obj.getTexCoordsBuffer().duplicate();
        IntBuffer iBuf = obj.getIndicesBuffer().duplicate();
        IntBuffer boneBuf = obj.getBoneIDsBuffer().duplicate();

        while (iBuf.hasRemaining()) {
            int idx = iBuf.get();

            float relX = vBuf.get(idx * 3);
            float relY = vBuf.get(idx * 3 + 1);
            float relZ = vBuf.get(idx * 3 + 2);
            float vx = relX;
            float vy = relY;
            float vz = relZ;

            float nx = nBuf.get(idx * 3);
            float ny = nBuf.get(idx * 3 + 1);
            float nz = nBuf.get(idx * 3 + 2);

            float u = tBuf.get(idx * 2);
            float v = tBuf.get(idx * 2 + 1);

            int boneID = boneBuf.get(idx);
            int texture = entity.getTextureID();

            float[] vertex = new float[]{vx, vy, vz, u, v, texture, nx, ny, nz, boneID};
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
