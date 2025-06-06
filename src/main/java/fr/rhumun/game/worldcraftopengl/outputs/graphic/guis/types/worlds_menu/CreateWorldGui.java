package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.InputField;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;

/**
 * GUI allowing the user to choose a world name and a seed before starting the game.
 */
public class CreateWorldGui extends CenteredGUI {
    private final InputField nameField;
    private final InputField seedField;
    private InputField active;

    public CreateWorldGui() {
        super(500, 500, Texture.PLANKS);

        this.addText(0, -200, "Create World");
        this.addText(-200, -100, "Name:");
        nameField = new InputField(-50, -110, 300, 40, this);
        this.addComponent(nameField);

        this.addText(-200, -40, "Seed:");
        seedField = new InputField(-50, -50, 300, 40, this);
        this.addComponent(seedField);

        this.addButton(new CreateWorldButton(0, 80, this));
        active = nameField;
    }

    public void handleClick() {
        if (nameField.isCursorIn()) active = nameField;
        else if (seedField.isCursorIn()) active = seedField;
    }

    public void appendChar(char c) {
        if (active != null) active.append(c);
    }

    public void backspace() {
        if (active != null) active.backspace();
    }

    public String getWorldName() {
        return nameField.getText();
    }

    public Seed getSeed() {
        String text = seedField.getText();
        return text.isEmpty() ? Seed.random() : Seed.create(text);
    }

    public void createWorld() {
        Game.GAME.startGame(getSeed(), getWorldName());
    }

    @Override
    public void update() {
        // Nothing specific
    }
}
