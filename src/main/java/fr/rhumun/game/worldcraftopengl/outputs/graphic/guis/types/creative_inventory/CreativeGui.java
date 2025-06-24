package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.creative_inventory;

import fr.rhumun.game.worldcraftopengl.content.GuiTypes;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Slot;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.TextComponent;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.items_containers.CreativeItems;

import static fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.creative_inventory.CreativePlayerInventoryGui.CREATIVE_WIDTH;
import static fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.creative_inventory.CreativePlayerInventoryGui.ratio;

public class CreativeGui extends Gui {

    private final TextComponent title;

    public CreativeGui(Gui container) {
        super(0, 0, CREATIVE_WIDTH, CREATIVE_WIDTH, Texture.CREATIVE_INVENTORY, container);

        this.setItemContainer(new CreativeItems());

        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 9; x++) {
                int id = 9 * y + x;
                this.createCreativeSlot(getCreativeX(id), getCreativeY(id),
                        (int) Math.ceil(ratio * Slot.DEFAULT_SIZE));
            }
        }
        this.title = this.addText(77, 10, "Blocks");

        int i = 0;
        for(GuiTypes type : GuiTypes.values()){
            if(i>=11) break;
            this.addButton(new GuiTypeButton(3 , 10 + 52 * i, type, this));
            i++;
        }
        this.addButton(new ModelButton(74, 472, Model.BLOCK, this));
        this.addButton(new ModelButton(123, 472, Model.SLAB, this));
        this.addButton(new ModelButton(172, 472, Model.STAIRS, this));
        this.addButton(new ModelButton(221, 472, Model.WALL, this));
        this.addButton(new ModelButton(270, 472, Model.CYLINDER, this));
    }

    public void setType(GuiTypes type){
        ((CreativeItems)this.getItemContainer()).setType(type);
        this.title.setText(type.getName());
    }


    private int getCreativeX(int slot) {
        return (int) (77 * ratio + Math.ceil(slot % 9 * 40 * ratio));
    }

    private int getCreativeY(int slot) {
        return (int) (34 * ratio + Math.ceil(slot / 9 * 39 * ratio));
    }
}
