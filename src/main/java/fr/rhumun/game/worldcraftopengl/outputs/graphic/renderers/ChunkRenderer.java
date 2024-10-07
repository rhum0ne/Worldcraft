package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.Location;
import fr.rhumun.game.worldcraftopengl.blocks.Block;
import fr.rhumun.game.worldcraftopengl.blocks.MeshArrays;
import fr.rhumun.game.worldcraftopengl.blocks.Model;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import lombok.Getter;
import org.joml.Vector3f;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;

@Getter
public class ChunkRenderer {

    private final Chunk chunk;

    private final List<Renderer> renderers = new ArrayList<>();
    private final GlobalRenderer globalRenderer = new GlobalRenderer(GAME.getGraphicModule());
    private final GlobalRenderer transparentBlocksRenderer = new GlobalRenderer(GAME.getGraphicModule());

    public ChunkRenderer(Chunk chunk) {
        this.chunk = chunk;
        this.renderers.add(globalRenderer);
        this.renderers.add(transparentBlocksRenderer);

        for(Renderer renderer : renderers) {
            renderer.init();
        }
    }

    public void render() {
        if(chunk.isToUpdate()) update();

        //System.out.println("Rendering chunk");

        globalRenderer.render();
        if(transparentBlocksRenderer.getIndice() != 0){
            glEnable(GL_BLEND);
            //glDepthMask(false);

            transparentBlocksRenderer.render();

            //glDepthMask(true);
            glDisable(GL_BLEND);
        }
    }

    private void update(){
        getGlobalRenderer().getGraphicModule().updateLights();

        for(Renderer renderer : this.renderers){
            renderer.getVertices().clear();
            renderer.setIndice(0);
        }

        System.out.println("Updating chunk " + chunk);
//int blockShowDistance = 16*SHOW_DISTANCE;
        List<Block> blocks = new ArrayList<>(chunk.getVisibleBlock());
        for (Block block : blocks) {
            if (block == null || block.getMaterial() == null) continue;
            //Location loc = block.getLocation();

            Model model = block.getModel();
            if (model == null) continue;
            MeshArrays mesh = model.get();
            //if (mesh == null || mesh.getNumVertices() == 0) continue;

            if (block.isSurrounded()) continue;
            raster(block, mesh);
        }

        for(Renderer renderer : this.renderers) {
            renderer.toArrays();

            glBindVertexArray(renderer.getVAO());

            glBindBuffer(GL_ARRAY_BUFFER, renderer.getVBO());
            glBufferData(GL_ARRAY_BUFFER, renderer.getVerticesArray(), GL_STATIC_DRAW);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, renderer.getEBO());
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, renderer.getIndicesArray(), GL_STATIC_DRAW);

            glBindVertexArray(0);
        }

        chunk.setToUpdate(false);
    }

    private void raster(Block block, MeshArrays mesh) {

        FloatBuffer verticesBuffer = mesh.getVertices();
        FloatBuffer normalsBuffer = mesh.getNormals();
        FloatBuffer texCoordsBuffer = mesh.getTexCoords();

        int numVertices = mesh.getNumVertices();

        double x = block.getLocation().getX();
        double y = block.getLocation().getY();
        double z = block.getLocation().getZ();

        for (int i = 0; i < numVertices; i++) {
            float vx = (float) (x + verticesBuffer.get(i * 3));
            float vy = (float) (y + verticesBuffer.get(i * 3 + 1));
            float vz = (float) (z + verticesBuffer.get(i * 3 + 2));

            float nx = normalsBuffer.get(i * 3);
            float ny = normalsBuffer.get(i * 3 + 1);
            float nz = normalsBuffer.get(i * 3 + 2);

            Vector3f normal = new Vector3f(nx, ny, nz);

            if(/*!isFaceVisible(normal, new Vector3f(vx, vy, vz)) || */hasBlockAtFace(block, nx, ny, nz)) {
                i++;
                i++;
                continue;
            }

            float u = texCoordsBuffer.get(i * 2);
            float v = texCoordsBuffer.get(i * 2 + 1);

            if(block.isOpaque()) addVertex(new float[]{vx, vy, vz, u, v, block.getMaterial().getTextureID(), nx, ny, nz});
            else addTransparentVertex(new float[]{vx, vy, vz, u, v, block.getMaterial().getTextureID(), nx, ny, nz});
        }
    }

    /*private boolean isFaceVisible(Vector3f normal, Vector3f positions) {
        positions.negate().add(camera.getPos());
        return (positions.dot(normal)>0);
    }*/

    private boolean hasBlockAtFace(Block block, float nx, float ny, float nz) {
        if(!block.isOpaque()) return false;
        Location loc = block.getLocation();
        int x = loc.getXInt() + Math.round(nx);
        int y = loc.getYInt() + Math.round(ny);
        int z = loc.getZInt() + Math.round(nz);

        Block face = loc.getWorld().getBlockAt(x,y,z, false);
        return face != null && face.isOpaque();
    }


    private void addVertex(float[] vertexData) {
        globalRenderer.getVertices().add(vertexData);

        // L'index du sommet est simplement l'index actuel dans la liste
        // Par exemple, si c'est le 4e sommet qu'on ajoute, son index sera 3
        globalRenderer.addIndice();

        //indices.add(index);
    }

    private void addTransparentVertex(float[] vertexData) {
        transparentBlocksRenderer.getVertices().add(vertexData);

        // L'index du sommet est simplement l'index actuel dans la liste
        // Par exemple, si c'est le 4e sommet qu'on ajoute, son index sera 3
        transparentBlocksRenderer.addIndice();

        //indices.add(index);
    }
}
