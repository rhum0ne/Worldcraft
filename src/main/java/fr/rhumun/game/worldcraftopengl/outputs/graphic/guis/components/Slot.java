package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components;

import fr.rhumun.game.worldcraftopengl.content.items.Item;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.content.Mesh;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.BlockUtil;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.SlabUtils;
import lombok.Getter;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

@Getter
public class Slot extends Button {

    public static final int DEFAULT_SIZE = 30;

    private final ArrayList<float[]> verticesList = new ArrayList<>();
    private final ArrayList<Integer> indicesList = new ArrayList<>();

    private final int id;

    private Item showedItem;

    public Slot(int x, int y, int size, int id, Gui gui) {
        super(x, y, size, size, null, gui);
        this.id = id;
    }

    public Slot(int x, int y, int id, Gui gui){
        this(x, y, DEFAULT_SIZE, id, gui);
    }

    public Item getItem(){
        return this.getContainer().getItemContainer().getItems()[id];
    }

    public void setItem(Item item){
        this.getContainer().getItemContainer().setItem(id, item);
    }

    @Override
    public void update(){
        Item item = getItem();
        if(showedItem==item) return;

        if(item==null){
            this.setTexture(null);
        }else{
            this.setTexture(item.getMaterial().getMaterial().getTexture());
        }
        this.updateVertices(item);

        this.showedItem = item;
    }

    private void updateVertices(Item item) {
        this.getVerticesList().clear();
        this.getIndicesList().clear();

        if(item == null || item.getMaterial() == null){
            toArrays();
            updateVAO();
            return;
        }
        Material mat = item.getMaterial();

        if(item.getModel() == Model.BLOCK){
            BlockUtil.rasterBlockItem(item, this, this.getVerticesList(), this.getIndicesList());
            toArrays();
            updateVAO();
            return;
        }
        else if(item.getModel() == Model.SLAB){
            SlabUtils.rasterBlockItem(item, this, this.getVerticesList(), this.getIndicesList());
            toArrays();
            updateVAO();
            return;
        }

        Mesh mesh = item.getModel().get();

        FloatBuffer verticesBuffer = mesh.getVerticesBuffer().duplicate();
        FloatBuffer texCoordsBuffer = mesh.getTexCoordsBuffer().duplicate();
        IntBuffer indicesBuffer = mesh.getIndicesBuffer().duplicate();

        while (indicesBuffer.hasRemaining()) {
            int vertexIndex = indicesBuffer.get();

            // Position du sommet
            float vx = (verticesBuffer.get(vertexIndex * 3)+0.5f)*this.getWidth() + this.getX();
            float vy = (verticesBuffer.get(vertexIndex * 3 + 1))*this.getHeigth()+this.getY();
            float vz = (verticesBuffer.get(vertexIndex * 3 + 2));

            // Coordonnées de texture
            float u = texCoordsBuffer.get(vertexIndex * 2);
            float v = 1-texCoordsBuffer.get(vertexIndex * 2 + 1);

            addVertex(new float[]{vx, vy, vz, u, v, item.getMaterial().getTextureID()});
        }
        toArrays();
        updateVAO();
    }

    private void addVertex(float[] vertexData) {
        this.getVerticesList().add(vertexData);
        this.indicesList.add(indicesList.isEmpty() ? 0 : indicesList.getLast()+1);
    }

    public void toArrays(){
        if(verticesList.isEmpty()){
            this.setIndices(new int[0]);
            this.setVertices(new float[0]);
            return;
        }

        this.setVertices(new float[verticesList.size()*verticesList.getFirst().length]);
        int index = 0;
        for (float[] vertex : verticesList) {
            for (float v : vertex) {
                this.getVertices()[index++] = v;
                //if(j%5 == 0 && j!=0) System.out.println("Texture ID " + vertices.get(i)[j]);
            }
        }

        this.setIndices(new int[indicesList.size()]);
        for (int i = 0; i < indicesList.size(); i++) {
            this.getIndices()[i] = indicesList.get(i);
        }
    }

    @Override
    public void onClick(Player player) {}
}
