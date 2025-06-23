package fr.rhumun.game.worldcraftopengl.content.materials.items.types;

import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.ToolType;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import lombok.Getter;

/**
 * Item representing a tool used for breaking blocks faster.
 */
@Getter
public class ToolItemMaterial extends ItemMaterial {
    private final int level;
    private final ToolType type;

    public ToolItemMaterial(Texture texture, int level, ToolType type) {
        super(texture);
        this.level = level;
        this.type = type;
    }
}
