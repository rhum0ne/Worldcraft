package fr.rhumun.game.worldcraftopengl.content;

import fr.rhumun.game.worldcraftopengl.content.models.*;
import fr.rhumun.game.worldcraftopengl.content.models.AbstractModel;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;

import de.javagl.obj.*;
import org.joml.Vector3f;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.Game.TEXTURES_PATH;

@Getter
public enum Model {

    BLOCK(new BlockModel(), (byte) 0),
    SLAB(new SlabModel(), (byte) 1),
    CYLINDER(new CylinderModel(), (byte) 2),
    CROSS(new CrossModel(), (byte) 3),
    OTTER(new OtterModel(), (byte) 4),
    ROCKY(new RockyModel(), (byte) 5),
    NINJA_SKELETON(new NinjaSkeletonModel(), (byte) 6);
    TRAPDOOR(new TrapdoorModel(), (byte) 5);

    final AbstractModel model;
    final byte id;

    static Model[] MODELS;

    Model(AbstractModel model, byte id){
        this.model = model;
        this.id = id;
    }
    public Mesh get(){ return model.getModel(); }

    public static void init(){
        MODELS = Model.values().clone();
    }

    public static Model getById(byte id){
        return MODELS[id];
    }

    public static Mesh load(final String name){
        try {
            //return MeshObjectLoader.loadModelMeshFromStream(new FileInputStream(TEXTURES_PATH + name));
            return new Mesh(ObjUtils.convertToRenderable(ObjReader.read(new FileInputStream(TEXTURES_PATH + "models\\" + name))));
        } catch (IOException e) {
            GAME.errorLog(e);
            throw new RuntimeException(e);
        }
    }

    public void setBlockDataOnPlace(Block block, Vector3f hitPosition, Vector3f direction) {
        model.setBlockDataOnPlace(block, hitPosition, direction);
    }

    public boolean isOpaque() {
        return model.isOpaque();
    }

    public AbstractModel getModelObject() {
        return model;
    }

}
