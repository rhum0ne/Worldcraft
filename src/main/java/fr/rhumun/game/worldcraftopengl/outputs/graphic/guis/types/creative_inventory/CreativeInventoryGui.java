package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.creative_inventory;

import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Slot;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.items_containers.CreativeItems;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.InventoryGUI;

public class CreativeInventoryGui extends CenteredGUI {

    private final InventoryGUI inventory;

    private final int width = 528;
    private final int height = 528;
    private static final float ratio = (float) 1;
    public CreativeInventoryGui() {
        super((int) (528*ratio), (int) (528*ratio), Texture.CREATIVE_INVENTORY);

        this.inventory = this.addInventory((int) (74*ratio), (int) (478*ratio), ratio);
        this.setItemContainer(new CreativeItems());

        for(int y=0; y<11; y++)
            for(int x=0; x<9; x++){
                int id = 9*y+x;
                this.createCreativeSlot(getXForSlot(id), getYForSlot(id), (int) Math.ceil(ratio* Slot.DEFAULT_SIZE));
            }

        this.addText(77,10, "Inventaire");
        this.addButton(new SlabButton(0, 10, this));
    }

    private int getXForSlot(int slot){
        return (int) (77*ratio + Math.ceil(slot%9 * 40 * ratio));
    }

    private int getYForSlot(int slot){
        return (int) (34*ratio + Math.ceil(slot/9 * 39 * ratio));
    }
}
