package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis;

import fr.rhumun.game.worldcraftopengl.ItemContainer;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GuiModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.Image;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.InventoryGUI;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.Game.GUI_ZOOM;

@Getter @Setter
public class Gui extends Component{

    private final GuiRenderer renderer;
    private final GuiModule guiModule;
    private Gui container;

    private final List<Component> components = new ArrayList<>();
    private List<Slot> slots = new ArrayList<>();
    @Setter
    private ItemContainer itemContainer;

    private boolean isClosed = false;

    public Gui(int x, int y, int width, int height, Texture texture){
        super(x, y, width, height, texture);
        this.renderer = new GuiRenderer(x, y, x+width, y+height, texture);
        this.guiModule = GAME.getGraphicModule().getGuiModule();
    }

    public void render(){
        this.renderer.render();
        for(Component component : this.components) component.render();
    }

    @Override
    public void update() {

    }

    public boolean hasContainer(){ return this.container != null; }

    public Image createImage(int x, int y, int width, int height, Texture texture){
        Image image = new Image(this.getX() + GUI_ZOOM*x, this.getY()+GUI_ZOOM*y, GUI_ZOOM*width, GUI_ZOOM*height, texture);

        this.components.add(image);
        return image;
    }

    public Slot createSlot(int x, int y, int size){
        Slot slot = new Slot(this.getX() + GUI_ZOOM*x, this.getY()+GUI_ZOOM*y, size, slots.size(), this);

        this.slots.add(slot);
        this.components.add(slot);
        return slot;
    }

    public Slot createSlot(int x, int y){
        Slot slot = new Slot(this.getX() + GUI_ZOOM*x, this.getY()+GUI_ZOOM*y, slots.size(), this);

        this.slots.add(slot);
        this.components.add(slot);
        return slot;
    }

    public InventoryGUI addInventory(int x, int y){
        return this.addInventory(x, y, 1);
    }
    public InventoryGUI addInventory(int x, int y, float ratio){
        InventoryGUI gui = new InventoryGUI(GUI_ZOOM*x, GUI_ZOOM*y, ratio, this);

        this.components.add(gui);
        return gui;
    }

    protected void setCoordinates(Component component, int x, int y) {
        component.set2DCoordinates(this.getX() + GUI_ZOOM*x, this.getY()+GUI_ZOOM*y);
    }

//    public void createButton(int x, int y){
//        Button button = new Button(this.x + x, this.y+y, this.x + x+0.1f, this.y+y-0.1f, Texture.GRASS_BLOCK);
//
//        this.components.add(button);
//    }

    public void close(){
        this.isClosed = true;
    }

    @Override
    public int getX(){ return super.getX() + ((this.hasContainer()) ? this.container.getX() : 0); }
    @Override
    public int getY(){ return super.getY() + ((this.hasContainer()) ? this.container.getY() : 0); }

}
