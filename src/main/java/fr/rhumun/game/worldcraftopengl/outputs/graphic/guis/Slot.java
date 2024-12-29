package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis;

import fr.rhumun.game.worldcraftopengl.Item;
import fr.rhumun.game.worldcraftopengl.blocks.Mesh;
import fr.rhumun.game.worldcraftopengl.blocks.Model;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.ForcedModelMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.Material;
import lombok.Getter;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static fr.rhumun.game.worldcraftopengl.Game.GUI_ZOOM;

@Getter
public class Slot extends Component {

    public static final int DEFAULT_SIZE = 30;

    private final List<float[]> verticesList = new ArrayList<>();
    private int indice;

    private final int id;
    private final Gui gui;

    private Item showedItem;

    public Slot(int x, int y, int size, int id, Gui gui) {
        super(x, y, size, size, null);
        this.id = id;
        this.gui = gui;
    }

    public Slot(int x, int y, int id, Gui gui){
        this(x, y, DEFAULT_SIZE, id, gui);
    }

    public Item getItem(){
        return gui.getItemContainer().getItems()[id];
    }

    @Override
    public void update(){
        Item item = getItem();
        if(showedItem==item) return;

        this.setTexture(item.getMaterial().getMaterial().getTexture());
        this.updateVertices(item);
        this.showedItem = item;
    }

    private void updateVertices(Item item) {
        this.getVerticesList().clear();
        this.indice = 0;

        if (item.getMaterial() == null) return;
        Material mat = item.getMaterial();
        Mesh mesh = Model.BLOCK.get();
        if (mat.getMaterial() instanceof ForcedModelMaterial forcedModelMaterial)
            mesh = forcedModelMaterial.getModel().get();

        FloatBuffer verticesBuffer = mesh.getVerticesBuffer().duplicate();
        FloatBuffer texCoordsBuffer = mesh.getTexCoordsBuffer().duplicate();
        IntBuffer indicesBuffer = mesh.getIndicesBuffer().duplicate();

        while (indicesBuffer.hasRemaining()) {
            int vertexIndex = indicesBuffer.get();

            // Position du sommet
            float vx = verticesBuffer.get(vertexIndex * 3)*GUI_ZOOM*this.getWidth() + this.getX();
            float vy = verticesBuffer.get(vertexIndex * 3 + 1)*GUI_ZOOM*this.getHeigth()+this.getY();
            float vz = verticesBuffer.get(vertexIndex * 3 + 2);

            // Coordonnées de texture
            float u = texCoordsBuffer.get(vertexIndex * 2);
            float v = texCoordsBuffer.get(vertexIndex * 2 + 1);

            addVertex(new float[]{vx, vy, vz, u, v, item.getMaterial().getTextureID()});
        }
        toArrays();
        updateVAO();
    }

    private void addVertex(float[] vertexData) {
        this.getVerticesList().add(vertexData);
        this.indice++;
    }

    public void toArrays(){
        this.setVertices(new float[verticesList.size()*6]);
        int index = 0;
        for (float[] vertex : verticesList) {
            for (float v : vertex) {
                this.getVertices()[index++] = v;
                //if(j%5 == 0 && j!=0) System.out.println("Texture ID " + vertices.get(i)[j]);
            }
        }

        this.setIndices(new int[indice]);
        for (int i = 0; i < indice; i++) {
            this.getIndices()[i] = i;
        }
    }
}
