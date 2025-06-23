package fr.rhumun.game.worldcraftopengl.content.materials.blocks.types;

import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;

public interface MultipleTexturesMaterial {
    Texture getTexture(Block block);
}
