package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.creative_inventory;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.items_containers.CreativeItems;

public class SlabButton extends Button {

    public SlabButton(int x, int y, Gui container) {
        super(x, y, 48, 46, Texture.SQUARE_BUTTON, container);
    }

    @Override
    public void update() {

    }

    @Override
    public void onClick(Player player) {
        CreativeGui gui = (CreativeGui) this.getContainer();
        CreativeItems items = (CreativeItems) gui.getItemContainer();

        if(items.getShowedModel() != Model.SLAB)
            items.setModel(Model.SLAB);
        else items.reset();
    }
}
