package fr.rhumun.game.worldcraftopengl.content;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;

import de.javagl.obj.*;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.Game.TEXTURES_PATH;

@Getter
public enum Model {

    BLOCK(load("block.obj"), true, (byte) 0),
    SLAB(load("slab.obj"), false, (byte) 1),
    CYLINDER(load("cylinder.obj"), false, (byte) 2),
    CROSS(load("cross-model.obj"), false, 13, (byte) 3),;

    final Mesh model;
    final boolean isOpaque;
    final int maxChunkDistance;
    final byte id;

    static Model[] MODELS;
    Model(Mesh model, boolean isOpaque, byte id) {this(model, isOpaque, -1, id);}

    Model(Mesh model, boolean isOpaque, int maxChunkDistance, byte id){
        this.model = model;
        this.isOpaque = isOpaque;
        this.maxChunkDistance = maxChunkDistance;
        this.id = id;
    }
    public Mesh get(){ return model; }

    public static void init(){
        MODELS = Model.values().clone();
    }

    public static Model getById(byte id){
        return MODELS[id];
    }

    private static Mesh load(final String name){
        try {
            //return MeshObjectLoader.loadModelMeshFromStream(new FileInputStream(TEXTURES_PATH + name));
            return new Mesh(ObjUtils.convertToRenderable(ObjReader.read(new FileInputStream(TEXTURES_PATH + "models\\" + name))));
        } catch (IOException e) {
            GAME.errorLog(e);
            throw new RuntimeException(e);
        }
    }
}
