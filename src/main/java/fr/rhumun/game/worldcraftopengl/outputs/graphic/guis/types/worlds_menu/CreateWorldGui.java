package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.FullscreenTiledGui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Checkbox;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.InputField;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.TypingGui;
import fr.rhumun.game.worldcraftopengl.entities.player.Gamemode;
import fr.rhumun.game.worldcraftopengl.worlds.WorldType;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class CreateWorldGui extends FullscreenTiledGui implements TypingGui {
    private final InputField nameField;
    private final InputField seedField;
    private final Checkbox flatType;
    private final Checkbox creative;
    private InputField activeField;

    public CreateWorldGui() {
        super(Texture.DARK_COBBLE);

        this.addText(0, -150, "Creer un Monde");
        this.addText(-200, -60, "Nom:");
        nameField = new InputField(100, -60, 300, this);
        nameField.setValue("Mon monde");
        nameField.setOnClick(() -> activeField = nameField);
        this.addButton(nameField);

        this.addText(-200, -10, "Seed:");
        seedField = new InputField(100, -10, 300, this);
        seedField.setOnClick(() -> activeField = seedField);
        this.addButton(seedField);
        activeField = nameField;

        this.addText(-200, 40, "Monde plat:");
        flatType = new Checkbox(100, 40, "Flat", this);
        this.addButton(flatType);

        this.addText(-200, 90, "Gamemode Creatif:");
        creative = new Checkbox(100, 90, "Creatif", this);
        this.addButton(creative);

        this.addButton(new Button(110, 180, this, "Creer le monde") {
            @Override
            public void onClick(Player player) {
                String seedText = seedField.getValue();
                Seed seed = seedText == null || seedText.isEmpty() ? Seed.random() : Seed.create(seedText);
                String name = nameField.getValue();
                if (name == null || name.isEmpty()) name = "Mon monde";
                WorldType type = flatType.isChecked() ? WorldType.FLAT : WorldType.NORMAL;
                Gamemode gm = creative.isChecked() ? Gamemode.CREATIVE : Gamemode.SURVIVAL;
                GAME.startGame(name, seed, type, gm);
            }
        });

        this.addButton(new Button(-110, 180, this, "Retour") {
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
            activeField = activeField == nameField ? seedField : nameField;
            return;
        }
        if(c == '\b' || c == 127) {
            String text = activeField.getValue();
            if(text != null && !text.isEmpty()) {
                activeField.setValue(text.substring(0, text.length() - 1));
            }
            return;
        }
        activeField.setValue(activeField.getValue() + c);
    }
}
