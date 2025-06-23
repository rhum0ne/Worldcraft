package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.creative_inventory;

import fr.rhumun.game.worldcraftopengl.content.GuiTypes;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Image;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.items_containers.CreativeItems;

public class GuiTypeButton extends Button {

    private final GuiTypes guiType;

    public GuiTypeButton(int x, int y, GuiTypes type, Gui container) {
        super(x, y, 48, 48, Texture.SQUARE_BUTTON, container);
        this.setAlignCenter(true);

        if(type != null) this.addComponent(new Image(0,0, 32, 32, type.getIcon(), this));

        this.guiType = type;
    }

    @Override
    public void onClick(Player player) {
        CreativeGui gui = (CreativeGui) this.getContainer();
        gui.setType(this.guiType);
    }
}
