package fr.rhumun.game.worldcraftopengl.blocks;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.MeshObjectLoader;
import lombok.Getter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static fr.rhumun.game.worldcraftopengl.Game.TEXTURES_PATH;

public enum Model {

    BLOCK(load("block.obj"), true),
    SLAB(load("slab.obj"), false),
    CYLINDER(load("cylinder.obj"), false),
    CROSS(load("cross-model.obj"), false),;

    final MeshArrays model;
    @Getter
    final boolean isOpaque;
    Model(MeshArrays model, boolean isOpaque){
        this.model = model;
        this.isOpaque = isOpaque;
    }
    public MeshArrays get(){ return model; }

    private static MeshArrays load(final String name){
        try {
            return MeshObjectLoader.loadModelMeshFromStream(new FileInputStream(TEXTURES_PATH + name));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
