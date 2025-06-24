package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.workbench.tool_box;

import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;

import java.util.Objects;

/**
 * Simple record storing the two input items of a toolbox recipe.
 */
public class ToolBoxCraft {
    private final ItemStack input1;
    private final ItemStack input2;

    public ToolBoxCraft(ItemStack input1, ItemStack input2) {
        this.input1 = input1;
        this.input2 = input2;
    }

    public ItemStack getInput1() { return input1; }
    public ItemStack getInput2() { return input2; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ToolBoxCraft that)) return false;
        return Objects.equals(input1.getMaterial(), that.input1.getMaterial()) &&
                Objects.equals(input1.getModel(), that.input1.getModel()) &&
                Objects.equals(input2.getMaterial(), that.input2.getMaterial()) &&
                Objects.equals(input2.getModel(), that.input2.getModel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(input1.getMaterial(), input1.getModel(),
                input2.getMaterial(), input2.getModel());
    }
}
