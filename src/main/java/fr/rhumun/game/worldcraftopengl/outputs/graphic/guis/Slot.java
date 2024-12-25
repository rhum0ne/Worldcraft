package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis;

import fr.rhumun.game.worldcraftopengl.Item;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Slot extends Component {

    private static final float SIZE = 0.1f;

    private final List<float[]> verticesList = new ArrayList<>();
    private int indice;

    private final int id;
    private final Gui gui;

    private Item showedItem;

    public Slot(float x, float y, int id, Gui gui) {
        super(x, y, x+SIZE, y+SIZE, null);
        this.id = id;
        this.gui = gui;
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

    private void updateVertices(Item item){
//        if(item.getMaterial() == null) return;
//        Material mat = item.getMaterial();
//        System.out.println("Adding item " + mat + " in " + id);
//        MeshArrays mesh = Model.BLOCK.get();
//        if(mat.getMaterial() instanceof ForcedModelMaterial forcedModelMaterial) mesh = forcedModelMaterial.getModel().get();
//        int numVertices = mesh.getNumVertices();
//
//        FloatBuffer verticesBuffer = mesh.getVertices();
//        FloatBuffer texCoordsBuffer = mesh.getTexCoords();
//
////        double x = block.getLocation().getX();
////        double y = block.getLocation().getY();
////        double z = block.getLocation().getZ();
//
//        for (int i = 0; i < numVertices; i++) {
//            float vx = (float) (verticesBuffer.get(i * 3) / 12 - 0.44 + 0.11*id);
//            float vy = (float) (verticesBuffer.get(i * 3 + 1) / 10 - 0.95);
//            float vz = (float) (verticesBuffer.get(i * 3 + 2) / 12);
//
//            vx = verticesBuffer.get(i * 3)/10 + this.getX();
//            vy = verticesBuffer.get(i * 3 + 1)/10+this.getY();
//            vz = verticesBuffer.get(i * 3 + 2)/10;
//
////            float dx = vx;
////            float dz = vz;
////            vx += vz/2;
////            vz+= dx/2;
////
////            vy+=dx/4+dz/4;
//
//            float u = texCoordsBuffer.get(i * 2);
//            float v = texCoordsBuffer.get(i * 2 + 1);
//
//            addVertex(new float[]{vx, vy, vz, u, v, item.getMaterial().getTextureID()});
//        }
//        toArrays();
//        updateVAO();
//
//        this.verticesList.clear();
//        System.out.println("DONE");
    }

    private void addVertex(float[] vertexData) {
        this.getVerticesList().add(vertexData);
        this.indice++;
    }

    public void toArrays(){
        this.setVertices(new float[verticesList.size()*10]);
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
