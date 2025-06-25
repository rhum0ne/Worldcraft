package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.entities.Entity;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.AxisAlignedBB;
import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.Hitbox;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.GLStateManager;
import fr.rhumun.game.worldcraftopengl.worlds.Block;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

/** Renderer displaying block and entity hitboxes as wireframe boxes. */
public class HitboxRenderer extends Renderer {

    private final Player player;

    public HitboxRenderer(GraphicModule graphicModule, Player player) {
        super(graphicModule, ShaderManager.SELECTED_BLOCK_SHADER);
        this.player = player;
    }

    @Override
    public void init() {
        super.init();
        GLStateManager.useProgram(ShaderManager.SELECTED_BLOCK_SHADER.id);
        glBindVertexArray(this.getVAO());
        GLStateManager.enable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBufferData(GL_ARRAY_BUFFER, this.getVerticesArray(), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glBindVertexArray(0);
    }

    @Override
    public void render() {
        if (!Game.SHOWING_HITBOXES) return;
        update();

        glBindVertexArray(this.getVAO());
        GLStateManager.disable(GL_BLEND);

        glDrawElements(GL_LINES, this.getIndicesArray().length, GL_UNSIGNED_INT, 0);

        GLStateManager.enable(GL_BLEND);
        glBindVertexArray(0);
    }

    private void update() {
        this.getVertices().clear();
        this.getIndices().clear();

        addEntityBox(player);
        for (Entity e : player.getLocation().getWorld().getEntities()) {
            if (e != player) addEntityBox(e);
        }

        int radius = 2;
        int minX = (int) Math.floor(player.getLocation().getX()) - radius;
        int maxX = (int) Math.ceil(player.getLocation().getX()) + radius;
        int minY = (int) Math.floor(player.getLocation().getY() - player.getHeight()) - 1;
        int maxY = (int) Math.ceil(player.getLocation().getY()) + 1;
        int minZ = (int) Math.floor(player.getLocation().getZ()) - radius;
        int maxZ = (int) Math.ceil(player.getLocation().getZ()) + radius;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = player.getLocation().getWorld().getBlockAt(x, y, z, false);
                    if (block == null || block.getMaterial() == null) continue;
                    Hitbox hitbox = block.getHitbox();
                    if(hitbox == null) return;
                    AxisAlignedBB bb = hitbox.getWorldBoundingBox(block);
                    addBox(bb);
                }
            }
        }

        this.toArrays();
        updateVAO();
    }

    private void addEntityBox(Entity entity) {
        AxisAlignedBB bb = entity.getBoundingBox();
        addBox(bb);
    }

    private void addBox(AxisAlignedBB bb) {
        float[][] verts = new float[][]{
                {bb.minX, bb.minY, bb.minZ},
                {bb.maxX, bb.minY, bb.minZ},
                {bb.maxX, bb.maxY, bb.minZ},
                {bb.minX, bb.maxY, bb.minZ},
                {bb.minX, bb.minY, bb.maxZ},
                {bb.maxX, bb.minY, bb.maxZ},
                {bb.maxX, bb.maxY, bb.maxZ},
                {bb.minX, bb.maxY, bb.maxZ}
        };
        int start = this.getVertices().size();
        for (float[] v : verts) {
            this.getVertices().add(v);
        }
        int[] edges = new int[]{
                0,1, 1,2, 2,3, 3,0,
                4,5, 5,6, 6,7, 7,4,
                0,4, 1,5, 2,6, 3,7
        };
        for (int idx : edges) {
            this.getIndices().add(start + idx);
        }
    }

    public void updateVAO() {
        glBindVertexArray(this.getVAO());

        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        fillBuffers();
        glBufferData(GL_ARRAY_BUFFER, this.getVerticesBuffer(), GL_DYNAMIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.getIndicesBuffer(), GL_DYNAMIC_DRAW);

        glBindVertexArray(0);
    }

    @Override
    public void cleanup() {
        glDeleteBuffers(this.getVBO());
        glDeleteVertexArrays(this.getVAO());
        glDeleteBuffers(this.getEBO());
        freeBuffers();
    }
}
