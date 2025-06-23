package fr.rhumun.game.worldcraftopengl.content;

import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;

import java.util.ArrayList;

public enum GuiTypes {

    NATURAL( Texture.GRASS_BLOCK ),
    CONSTRUCTION( Texture.CALCITE_BRICK ),
    COLOR( Texture.BLUE_WOOL ),
    ITEMS( Texture.DIAMOND ),
    FUNCTIONAL_BLOCKS( Texture.ACACIA_DOOR_ITEM );

    ArrayList<Material> materials = new ArrayList<>();
    final Texture icon;

    GuiTypes(Texture icon) {
        this.icon = icon;
    }

    public void add(Material material) {
        this.materials.add(material);
    }

    public Texture getIcon() {
        return this.icon;
    }

    public ArrayList<Material> getMaterials() {
        return this.materials;
    }

    public String getName() {
        String name = this.name().replace('_', ' ');
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

}
