package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class Button extends Component {
    private boolean hovered = false;
    private boolean clicked = false;
    public Button(int x, int y, int width, int heigth, Texture texture, Gui container) {
        super(x, y, width, heigth, texture, container);
    }

    public abstract void onClick(Player player) ;
}
