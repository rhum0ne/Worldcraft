package fr.rhumun.game.worldcraftopengl.content.textures;

import java.util.ArrayList;
import java.util.List;

public enum TextureTypes {
    BLOCKS,
    GUIS,
    TEXT;

    ArrayList<Texture> textures = new ArrayList<>();
    TextureTypes() {}

    public void add(Texture texture) {
        textures.add(texture);
    }

    public List<Texture> get() {
        return textures;
    }
}
