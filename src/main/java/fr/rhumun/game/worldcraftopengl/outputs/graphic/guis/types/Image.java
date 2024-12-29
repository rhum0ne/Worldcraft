package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types;

import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.Component;

public class Image extends Component {
    public Image(int x, int y, int width, int heigth, Texture texture) {
        super(x, y, width, heigth, texture);
    }

    @Override
    public void update() {

    }
}
