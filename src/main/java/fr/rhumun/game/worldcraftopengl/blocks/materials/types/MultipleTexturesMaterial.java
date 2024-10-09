package fr.rhumun.game.worldcraftopengl.blocks.materials.types;

import fr.rhumun.game.worldcraftopengl.blocks.Block;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;

public interface MultipleTexturesMaterial {
    Texture getTexture(Block block);
}
