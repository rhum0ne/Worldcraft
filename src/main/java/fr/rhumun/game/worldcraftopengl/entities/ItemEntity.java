package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import lombok.Getter;

@Getter
public class ItemEntity extends Entity{

    private final Material material;
    private int quantity = 1;

    public ItemEntity(Model model, Material material, Location loc, int quantity) {
        super(model, (short) 0, 1, 0.2f, 0.2f, 0, 0, 0, 0, 0, loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        this.material = material;
        this.quantity = quantity;
    }

    public ItemEntity(ItemStack selectedItem, Location location) {
        this(selectedItem.getModel(), selectedItem.getMaterial(), location, 1);
    }
}
