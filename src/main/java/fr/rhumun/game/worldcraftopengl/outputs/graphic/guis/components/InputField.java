package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.Player;

/**
 * Simple text input box based on {@link Button}.
 * The displayed text is left-aligned and an optional click
 * action can be provided.
 */
public class InputField extends Button {

    private Runnable onClick;

    public InputField(int x, int y, int width, Gui container) {
        super(x, y, width, 40, Texture.DEFAULT_BUTTON, container);
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    @Override
    public void onClick(Player player) {
        if (onClick != null) onClick.run();
    }

    /**
     * Returns the current value displayed in the field.
     */
    public String getValue() {
        return this.getText().getText();
    }

    /**
     * Sets the value displayed in the field.
     */
    public void setValue(String value) {
        this.getText().setText(value);
    }

    @Override
    public void update() {
        super.update();
        // Left align the text with a small padding
        this.getText().set2DCoordinates(-this.getWidth() / 2 + 10, 0);
    }
}
