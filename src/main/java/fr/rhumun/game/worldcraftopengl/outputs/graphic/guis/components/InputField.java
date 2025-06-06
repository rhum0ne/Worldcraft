package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.TextComponent;

/**
 * Simple clickable text input field using a TextComponent to display entered characters.
 */
public class InputField extends Component {
    private final TextComponent textComponent;

    public InputField(int x, int y, int width, int height, Component container) {
        super(x, y, width, height, Texture.DEFAULT_BUTTON, container);
        this.textComponent = new TextComponent(5, 5, "", this);
        this.addComponent(textComponent);
    }

    public void append(char c) {
        textComponent.print(String.valueOf(c));
    }

    public void backspace() {
        String txt = textComponent.getText();
        if (!txt.isEmpty()) {
            textComponent.setText(txt.substring(0, txt.length() - 1));
        }
    }

    public String getText() {
        return textComponent.getText();
    }

    @Override
    public void update() {
        // Nothing specific
    }
}
