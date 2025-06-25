package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class Button extends Component {
    private boolean hovered = false;
    private boolean clicked = false;
    private boolean active = true;

    private Texture normal, hoverTexture, unactiveTexture;
    private final TextComponent text = new TextComponent(0, 0, "", this);

    public Button(int x, int y, Gui container, String text) {
        this(x, y, 200, 40, Texture.DEFAULT_BUTTON, Texture.DEFAULT_BUTTON_HOVERED, Texture.DEFAULT_BUTTON_UNACTIVE, container);
        this.text.setText(text);
    }

    public Button(int x, int y, int width, int height, Gui container, String text) {
        this(x, y, width, height, Texture.DEFAULT_BUTTON, Texture.DEFAULT_BUTTON_HOVERED, Texture.DEFAULT_BUTTON_UNACTIVE, container);
        this.text.setText(text);
    }

    public Button(int x, int y, int width, int height, Gui container) {
        this(x, y, width, height, Texture.DEFAULT_BUTTON, Texture.DEFAULT_BUTTON_HOVERED, Texture.DEFAULT_BUTTON_UNACTIVE, container);
    }

    public Button(int x, int y, int width, int height, Texture texture, Gui container) {
        this(x, y, width, height, texture, texture, texture, container);
    }

    public Button(int x, int y, int width, int height, Texture texture, Texture hovered, Texture unactive, Gui container) {
        super(x, y, width, height, texture, container);
        this.normal = texture;
        this.hoverTexture = hovered;
        this.unactiveTexture = unactive;

        this.setAlignCenter(true);
        this.addComponent(text);
    }

    public void click(Player player){
        player.playSound(this.getClickSound());
        this.onClick(player);
    }

    protected Sound getClickSound() {
        return Sound.CLICK;
    }

    public abstract void onClick(Player player) ;

    @Override
    public void update() {
        if(!isActive()){
            this.set2DTexture(unactiveTexture);
            return;
        }

        boolean isCursorIn = this.isCursorIn();

        if(isCursorIn && !hovered) {
            this.set2DTexture(hoverTexture);
            this.setHovered(true);
        }
        else if(!isCursorIn && hovered){
            this.set2DTexture(normal);
            this.setHovered(false);
        }

    }
}
