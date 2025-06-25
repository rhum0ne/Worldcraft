package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.creative_inventory;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.items_containers.CreativeItems;

public class ModelButton extends Button {

    private final Model model;

    public ModelButton(int x, int y, Model model, Gui container) {
        super(x, y, 39, 39, null, container);
        this.model = model;
        this.setAlignCenter(true);
    }

    @Override
    public void update() {

    }

    @Override
    public void onClick(Player player) {
        CreativeGui gui = (CreativeGui) this.getContainer();
        CreativeItems items = (CreativeItems) gui.getItemContainer();

        if(items.getShowedModel() != model)
            items.setModel(model);
        else items.reset();
    }
}
