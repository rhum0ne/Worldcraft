package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.content.Mesh;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.entities.*;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.BlockUtil;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.SlabUtils;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.StairsUtils;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.WallUtils;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.DoorUtils;
import org.lwjgl.opengl.GL30C;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class EntitiesRenderer extends GlobalRenderer {

    private final Player player;

    public EntitiesRenderer(GraphicModule graphicModule, Player player) {
        super(graphicModule, ShaderManager.ENTITY_SHADER);
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

        glDrawElements(GL_TRIANGLES, this.getIndicesArray().length, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        this.getIndices().clear();
        this.getVertices().clear();
    }

    public void update() {
        this.getVertices().clear();
        this.getIndices().clear();
        Iterator<Entity> it = player.getLocation().getWorld().getEntities().iterator();

        while (it.hasNext()) {
            Entity e = it.next();

            //if (e instanceof MobEntity mE && mE.isAnimated() && mE.getAnimator() != null) continue;
            if (e == player) continue;
            Model model = e.getModel();
            if (model == null) continue;

            if (e instanceof ItemEntity i) {
                if (model == Model.BLOCK) {
                    BlockUtil.rasterDroppedBlockItem(e.getLocation(), i.getMaterial(), this.getVertices(), this.getIndices());
                    continue;
                }
                else if(model == Model.SLAB) {
                    SlabUtils.rasterDroppedSlabItem(e.getLocation(), i.getMaterial(), this.getVertices(), this.getIndices());
                    continue;
                }
                else if(model == Model.STAIRS) {
                    StairsUtils.rasterDroppedStairsItem(e.getLocation(), i.getMaterial(), this.getVertices(), this.getIndices());
                    continue;
                }
                else if(model == Model.WALL) {
                    WallUtils.rasterDroppedWallItem(e.getLocation(), i.getMaterial(), this.getVertices(), this.getIndices());
                    continue;
                }
                else if(model == Model.DOOR) {
                    DoorUtils.rasterDroppedDoorItem(e.getLocation(), i.getMaterial(), this.getVertices(), this.getIndices());
                    continue;
                }
            }

            raster(e, model);
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
        IntBuffer boneBuffer = obj.getBoneIDsBuffer() != null ? obj.getBoneIDsBuffer().duplicate() : null;

        MobEntity animated = null;
        if (entity instanceof MobEntity m && m.isAnimated() && m.getAnimator() != null && boneBuffer != null) {
            animated = m;
        }

        float yawRad = (float) Math.toRadians(entity.getLocation().getYaw());
        float cosYaw = (float) Math.cos(yawRad + Math.PI/2);
        float sinYaw = (float) Math.sin(yawRad + Math.PI/2);

        double x = entity.getLocation().getX();
        double y = entity.getLocation().getY();
        double z = entity.getLocation().getZ();

        while (indicesBuffer.hasRemaining()) {
            int vertexIndex = indicesBuffer.get();

            float nx = normalsBuffer.get(vertexIndex * 3);
            float ny = normalsBuffer.get(vertexIndex * 3 + 1);
            float nz = normalsBuffer.get(vertexIndex * 3 + 2);

            float relX = verticesBuffer.get(vertexIndex * 3);
            float relY = verticesBuffer.get(vertexIndex * 3 + 1);
            float relZ = verticesBuffer.get(vertexIndex * 3 + 2);

            if (animated != null) {
                int boneId = boneBuffer.get(vertexIndex);
                var mat = animated.getAnimator().getBoneMatrix(boneId);
                if (mat != null) {
                    org.joml.Vector3f vec = new org.joml.Vector3f(relX, relY, relZ);
                    mat.transformPosition(vec);
                    relX = vec.x;
                    relY = vec.y;
                    relZ = vec.z;
                }
            }

            float rotX = cosYaw * relX - sinYaw * relZ;
            float rotZ = sinYaw * relX + cosYaw * relZ;

            float vx = (float) (x + rotX);
            float vy = (float) (y + relY);
            float vz = (float) (z + rotZ);

            float rnx = cosYaw * nx - sinYaw * nz;
            float rnz = sinYaw * nx + cosYaw * nz;

            int texture = entity.getTextureID();
            float u = texCoordsBuffer.get(vertexIndex * 2);
            float v = texCoordsBuffer.get(vertexIndex * 2 + 1);

            float[] vertexData = new float[]{vx, vy, vz, u, v, texture, rnx, ny, rnz};
            this.addVertex(vertexData);
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
