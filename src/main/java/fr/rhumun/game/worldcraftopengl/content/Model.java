package fr.rhumun.game.worldcraftopengl.content;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;

import de.javagl.obj.*;

import static fr.rhumun.game.worldcraftopengl.Game.TEXTURES_PATH;

@Getter
public enum Model {

    BLOCK(load("block.obj"), true),
    SLAB(load("slab.obj"), false),
    CYLINDER(load("cylinder.obj"), false),
    CROSS(load("cross-model.obj"), false, 13),;

    final Mesh model;
    final boolean isOpaque;
    final int maxChunkDistance;
    Model(Mesh model, boolean isOpaque) {this(model, isOpaque, -1);}

    Model(Mesh model, boolean isOpaque, int maxChunkDistance){
        this.model = model;
        this.isOpaque = isOpaque;
        this.maxChunkDistance = maxChunkDistance;
    }
    public Mesh get(){ return model; }

    private static Mesh load(final String name){
        try {
            //return MeshObjectLoader.loadModelMeshFromStream(new FileInputStream(TEXTURES_PATH + name));
            return new Mesh(ObjUtils.convertToRenderable(ObjReader.read(new FileInputStream(TEXTURES_PATH + "models\\" + name))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
