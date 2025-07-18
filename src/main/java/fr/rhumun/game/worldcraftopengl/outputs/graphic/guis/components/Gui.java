package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components;

import fr.rhumun.game.worldcraftopengl.content.items.ItemContainer;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GuiModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.InventoryGUI;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.Game.GUI_ZOOM;

@Getter @Setter
public class Gui extends Component {

    private List<Slot> slots = new ArrayList<>();
    @Setter
    private ItemContainer itemContainer;

    private boolean isClosed = false;

    public Gui(int x, int y, int width, int height, Texture texture, Gui container){
        super(x, y, width, height, texture, container);
    }

    public Gui(int x, int y, int width, int height, Texture texture){
        this(x, y, width, height, texture, null);
    }

    @Override
    public void update() {

    }

    public Image createImage(int x, int y, int width, int height, Texture texture){
        Image image = new Image(x, y, width, height, texture, this);

        this.addComponent(image);
        return image;
    }

    public TextComponent addText(int x, int y, String text) {
        TextComponent textC = new TextComponent(x, y, text, this);

        this.addComponent(textC);
        return textC;
    }


    public void addButton(Button button) {
        this.addComponent(button);
    }

    public Slot createSlot(int x, int y, int size){
        Slot slot = new Slot(x, y, size, slots.size(), this);

        this.slots.add(slot);
        this.addComponent(slot);
        return slot;
    }

    public Slot createSlot(int x, int y){
        Slot slot = new Slot(x, y, slots.size(), this);

        this.slots.add(slot);
        this.addComponent(slot);
        return slot;
    }


    public ClickableSlot createClickableSlot(int x, int y, int size) {
        ClickableSlot slot = new ClickableSlot(x, y, size, slots.size(), this);

        this.slots.add(slot);
        this.addComponent(slot);
        return slot;
    }

    public CreativeSlot createCreativeSlot(int x, int y, int size) {
        CreativeSlot slot = new CreativeSlot(x, y, size, slots.size(), this);

        this.slots.add(slot);
        this.addComponent(slot);
        return slot;
    }

    /**
     * Returns the identifier that should be used for the next slot to be
     * created. This allows subclasses to create custom {@link Slot}
     * implementations while keeping the ordering consistent.
     */
    public int nextSlotId() {
        return slots.size();
    }

    /**
     * Adds an externally created slot to this GUI and registers it so the GUI
     * can manage its lifecycle.
     */
    public void registerSlot(Slot slot) {
        this.slots.add(slot);
        this.addComponent(slot);
    }

    public InventoryGUI addInventory(int x, int y){
        return this.addInventory(x, y, 1);
    }
    public InventoryGUI addInventory(int x, int y, float ratio){
        InventoryGUI gui = new InventoryGUI(GUI_ZOOM*x, GUI_ZOOM*y, ratio, this);

        this.addComponent(gui);
        return gui;
    }

    protected void setCoordinates(Component component, int x, int y) {
        component.set2DCoordinates(x, y);
    }

//    public void createButton(int x, int y){
//        Button button = new Button(this.x + x, this.y+y, this.x + x+0.1f, this.y+y-0.1f, Texture.GRASS_BLOCK);
//
//        this.components.add(button);
//    }

    public void close(){
        this.isClosed = true;
    }
}
