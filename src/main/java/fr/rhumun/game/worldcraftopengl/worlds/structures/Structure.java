package fr.rhumun.game.worldcraftopengl.worlds.structures;

import lombok.Getter;

@Getter
public enum Structure {

    TREE(new TreeStructure());

    final AbstractStructure structure;
    Structure(AbstractStructure s){
        this.structure = s;
    }
}
