package fr.rhumun.game.worldcraftopengl.blocks;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Face {
    private Block block;
    @Setter
    private Block faceBlock;

    public Face(Block block) {this(block, null);}
    public Face(Block block, Block faceBlock) {
        this.block = block;
        this.faceBlock = faceBlock;
    }
}
