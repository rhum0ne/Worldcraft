package fr.rhumun.game.worldcraftopengl.worlds.generators.utils.trees;

import fr.rhumun.game.worldcraftopengl.worlds.Chunk;

public enum TreeType {

    OAK(new OakTree()),
    BIRCH(new BirchTree());

    private AbstractTreeType treeType;
    private TreeType(AbstractTreeType treeType) {
        this.treeType = treeType;
    }

    public void buildAt(Chunk chunk, int x, int y, int z) {
        this.treeType.buildBasic(chunk, x, y, z);
    }
}
