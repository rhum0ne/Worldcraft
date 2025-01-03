package fr.rhumun.game.worldcraftopengl.blocks;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;

import de.javagl.obj.*;

import static fr.rhumun.game.worldcraftopengl.Game.TEXTURES_PATH;

public enum Model {

    BLOCK(load("block.obj"), true),
    SLAB(load("slab.obj"), false),
    CYLINDER(load("cylinder.obj"), false),
    CROSS(load("cross-model.obj"), false),;

    final Mesh model;
    @Getter
    final boolean isOpaque;
    Model(Mesh model, boolean isOpaque){
        this.model = model;
        this.isOpaque = isOpaque;
    }
    public Mesh get(){ return model; }

    private static Mesh load(final String name){
        try {
            //return MeshObjectLoader.loadModelMeshFromStream(new FileInputStream(TEXTURES_PATH + name));
            return new Mesh(ObjUtils.convertToRenderable(ObjReader.read(new FileInputStream(TEXTURES_PATH + name))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
