package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis;

import fr.rhumun.game.worldcraftopengl.ItemContainer;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Gui {

    private final GuiRenderer renderer;

    private final List<Component> components = new ArrayList<>();
    private List<Slot> slots = new ArrayList<>();
    @Setter
    private ItemContainer itemContainer;
    private final float x;
    private final float y;
    private final float x2;
    private final float y2;
    private final Texture texture;

    public Gui(float x, float y, float x2, float y2, Texture texture){
        this.renderer = new GuiRenderer(x, y, x2, y2, texture);

        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.texture = texture;
    }

    public void render(){
        this.renderer.render();
        for(Component component : this.components) component.render();
    }

    public void createSlot(float x, float y){
        Slot slot = new Slot(this.x + x, this.y+y, slots.size(), this);

        this.slots.add(slot);
        this.components.add(slot);
    }

    public void createButton(float x, float y){
        Button button = new Button(this.x + x, this.y+y, this.x + x+0.1f, this.y+y-0.1f, Texture.GRASS_BLOCK);

        this.components.add(button);
    }

}
