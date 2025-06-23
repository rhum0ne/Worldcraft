package fr.rhumun.game.worldcraftopengl.content.materials.blocks;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.ForcedModelMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;

import static fr.rhumun.game.worldcraftopengl.content.textures.Texture.BOOKSHELF_TOP;
import fr.rhumun.game.worldcraftopengl.content.GuiTypes;

public class BookshelfMaterial extends Material implements PlaceableMaterial, ForcedModelMaterial {
    public BookshelfMaterial() {
        super(Texture.BOOKSHELF);
        this.addToType(GuiTypes.CONSTRUCTION);
        this.setTopTexture(BOOKSHELF_TOP);
        this.setBottomTexture(BOOKSHELF_TOP);
    }

    @Override
    public Sound getPlaceSound() {
        return SoundPack.WOOD.getRandom();
    }
    @Override
    public Sound getBreakSound() {
        return SoundPack.WOOD.getRandom();
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.OPAQUE;
    }

    @Override
    public Model getModel() {
        return Model.BLOCK;
    }
}
