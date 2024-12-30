package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types;

import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.Component;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.Gui;

public class Image extends Component {
    public Image(int x, int y, int width, int heigth, Texture texture, Gui container) {
        super(x, y, width, heigth, texture, container);
    }

    @Override
    public void update() {

    }
}
