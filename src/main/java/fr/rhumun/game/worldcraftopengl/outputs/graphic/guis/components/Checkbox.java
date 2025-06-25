package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import lombok.Getter;
import lombok.Setter;

/**
 * Simple checkbox component with a label.
 */
public class Checkbox extends Button {

    @Getter
    @Setter
    private boolean checked;
    private final Texture unchecked;
    private final Texture uncheckedHover;
    private final Texture checkedTex;
    private final Texture checkedHover;

    @Setter
    private Runnable onChange;

    public Checkbox(int x, int y, String label, Gui container) {
        super(x, y, 32, 32, Texture.CHECKBOX, Texture.CHECKBOX_HIGHLIGHTED,
                Texture.CHECKBOX, container);
        this.unchecked = Texture.CHECKBOX;
        this.uncheckedHover = Texture.CHECKBOX_HIGHLIGHTED;
        this.checkedTex = Texture.CHECKBOX_SELECTED;
        this.checkedHover = Texture.CHECKBOX_SELECTED_HIGHLIGHTED;
    }

    @Override
    public void onClick(Player player) {
        checked = !checked;
        if (onChange != null) onChange.run();
    }

    @Override
    public void update() {
        if (!isActive()) {
            set2DTexture(unchecked);
            return;
        }

        boolean hover = this.isCursorIn();
        Texture tex;
        if (checked) {
            tex = hover ? checkedHover : checkedTex;
        } else {
            tex = hover ? uncheckedHover : unchecked;
        }
        set2DTexture(tex);
    }
}

