package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.TextComponent;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.TypingGui;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class CreateWorldGui extends CenteredGUI implements TypingGui {
    private final TextComponent nameField;
    private final TextComponent seedField;
    private boolean typingSeed = false;

    public CreateWorldGui() {
        super(500, 300, Texture.DARK_COBBLE);

        this.addText(0, -120, "Créer un Monde");
        this.addText(-200, -60, "Nom:");
        nameField = this.addText(-50, -60, "");
        this.addText(-200, -20, "Seed:");
        seedField = this.addText(-50, -20, "");

        this.addButton(new Button(0, 60, this, "Créer") {
            @Override
            public void onClick(Player player) {
                String seedText = seedField.getText();
                Seed seed = seedText == null || seedText.isEmpty() ? Seed.random() : Seed.create(seedText);
                String name = nameField.getText();
                if (name == null || name.isEmpty()) name = "World " + seed.getLong();
                GAME.startGame(name, seed);
            }
        });
        this.addButton(new Button(0, 110, this, "Retour") {
            @Override
            public void onClick(Player player) {
                GAME.getGraphicModule().getGuiModule().openGUI(new WorldsGui());
            }
        });

        this.setAlignCenter(true);
    }

    @Override
    public void typeChar(char c) {
        if (c == '\t') {
            typingSeed = !typingSeed;
            return;
        }
        if (typingSeed) {
            seedField.setText(seedField.getText() + c);
        } else {
            nameField.setText(nameField.getText() + c);
        }
    }
}
