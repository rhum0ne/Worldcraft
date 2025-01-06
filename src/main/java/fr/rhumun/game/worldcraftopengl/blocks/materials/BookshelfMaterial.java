package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

import static fr.rhumun.game.worldcraftopengl.blocks.textures.Texture.BOOKSHELF_TOP;

public class BookshelfMaterial extends AbstractMaterial {
    public BookshelfMaterial() {
        super(Texture.BOOKSHELF);
        this.setTopTexture(BOOKSHELF_TOP);
        this.setBottomTexture(BOOKSHELF_TOP);
    }

    @Override
    public Sound getSound() {
        return Sound.WOOD;
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.OPAQUE;
    }
}
