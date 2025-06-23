package fr.rhumun.game.worldcraftopengl.content.items;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.types.ForcedModelMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter @Setter
public class ItemStack {

    private final Material material;
    private final Model model;
    private int quantity;

    public ItemStack(Material material) {this(material, (material instanceof ForcedModelMaterial fmm) ? fmm.getModel() : Model.BLOCK, 1);}
    public ItemStack(Material material, Model model){ this(material, model, 1); }
    public ItemStack(Material material, int quantity){ this(material, Model.BLOCK, quantity); }

    public ItemStack(Material material, Model model, int quantity){
        this.material = material;
        this.model = model;
        this.quantity = quantity;
    }

    public boolean isFull(){ return this.quantity >= 64; }
    public boolean isEmpty(){ return this.quantity <= 0; }

    public boolean isSame(ItemStack item){
        return this.material == item.material &&
                this.model == item.model;
    }

    public void remove(int quantity){
        this.quantity -= quantity;
        if(this.quantity < 0) this.quantity = 0;
    }
    public void add(int quantity){ this.quantity += quantity; }

    public int getMaxStackSize(){ return 64; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemStack item = (ItemStack) o;
        return Objects.equals(material, item.material) &&
                Objects.equals(model, item.model) &&
                quantity == item.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, model);
    }

    @Override
    public String toString() {
        return "Item{" + "material=" + material + ", model=" + model + ", quantity=" + quantity + '}';
    }
}
