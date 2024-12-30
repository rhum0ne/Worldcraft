package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis;

import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ShaderUtils;
import lombok.Getter;
import lombok.Setter;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

@Getter @Setter
public abstract class Button extends Component{
    private boolean hovered = false;
    private boolean clicked = false;
    public Button(int x, int y, int width, int heigth, Texture texture, Gui container) {
        super(x, y, width, heigth, texture, container);
    }

    public abstract void onClick(Player player) ;
}
